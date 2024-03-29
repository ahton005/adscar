import com.benasher44.uuid.uuid4
import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import com.datastax.oss.driver.internal.core.type.codec.extras.enums.EnumNameCodec
import com.datastax.oss.driver.internal.core.type.codec.registry.DefaultCodecRegistry
import helpers.asError
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import model.AdCassandraDTO
import model.AdVisibility
import models.InnerAd
import models.InnerAdId
import models.InnerAdLock
import models.InnerError
import org.slf4j.LoggerFactory
import repo.DbAdFilterRequest
import repo.DbAdIdRequest
import repo.DbAdRequest
import repo.DbAdResponse
import repo.DbAdsResponse
import repo.IAdRepository
import java.net.InetAddress
import java.net.InetSocketAddress
import java.util.concurrent.CompletionStage
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class RepoAdCassandra(
    private val keyspaceName: String,
    private val host: String = "",
    private val port: Int = 9042,
    private val user: String = "cassandra",
    private val pass: String = "cassandra",
    private val testing: Boolean = false,
    private val timeout: Duration = 30.toDuration(DurationUnit.SECONDS),
    private val randomUuid: () -> String = { uuid4().toString() },
    initObjects: Collection<InnerAd> = emptyList()
) : IAdRepository {
    private val log = LoggerFactory.getLogger(javaClass)

    private val codecRegistry by lazy {
        DefaultCodecRegistry("default").apply {
            register(EnumNameCodec(AdVisibility::class.java))
        }
    }

    private val session by lazy {
        CqlSession.builder()
            .addContactPoints(parseAddresses(host, port))
            .withLocalDatacenter("datacenter1")
            .withAuthCredentials(user, pass)
            .withCodecRegistry(codecRegistry)
            .build()
    }

    private val mapper by lazy { CassandraMapper.builder(session).build() }

    private fun createSchema(keyspace: String) {
        session.execute(
            SchemaBuilder
                .createKeyspace(keyspace)
                .ifNotExists()
                .withSimpleStrategy(1)
                .build()
        )
        session.execute(AdCassandraDTO.table(keyspace, AdCassandraDTO.TABLE_NAME))
        session.execute(AdCassandraDTO.titleIndex(keyspace, AdCassandraDTO.TABLE_NAME))
    }

    private val dao by lazy {
        if (testing) {
            createSchema(keyspaceName)
        }
        mapper.adDao(keyspaceName, AdCassandraDTO.TABLE_NAME).apply {
            runBlocking {
                initObjects.map { model ->
                    withTimeout(timeout) {
                        create(AdCassandraDTO(model)).await()
                    }
                }
            }
        }
    }

    private fun errorToAdResponse(e: Exception) = DbAdResponse.error(e.asError())
    private fun errorToAdsResponse(e: Exception) = DbAdsResponse.error(e.asError())

    private suspend inline fun <DbRes, Response> doDbAction(
        name: String,
        crossinline daoAction: () -> CompletionStage<DbRes>,
        okToResponse: (DbRes) -> Response,
        errorToResponse: (Exception) -> Response
    ): Response = doDbAction(
        name,
        {
            val dbRes = withTimeout(timeout) { daoAction().await() }
            okToResponse(dbRes)
        },
        errorToResponse
    )

    private inline fun <Response> doDbAction(
        name: String,
        daoAction: () -> Response,
        errorToResponse: (Exception) -> Response
    ): Response =
        try {
            daoAction()
        } catch (e: Exception) {
            log.error("Failed to $name", e)
            errorToResponse(e)
        }

    override suspend fun createAd(rq: DbAdRequest): DbAdResponse {
        val new = rq.ad.copy(id = InnerAdId(randomUuid()), lock = InnerAdLock(randomUuid()))
        return doDbAction(
            "create",
            { dao.create(AdCassandraDTO(new)) },
            { DbAdResponse.success(new) },
            ::errorToAdResponse
        )
    }

    override suspend fun readAd(rq: DbAdIdRequest): DbAdResponse =
        if (rq.id == InnerAdId.NONE) {
            ID_IS_EMPTY
        } else doDbAction(
            "read",
            { dao.read(rq.id.asString()) },
            { found ->
                if (found != null) DbAdResponse.success(found.toAdModel())
                else ID_NOT_FOUND
            },
            ::errorToAdResponse
        )

    override suspend fun updateAd(rq: DbAdRequest): DbAdResponse {
        val idStr = rq.ad.id.asString()
        val prevLock = rq.ad.lock.asString()
        val new = rq.ad.copy(lock = InnerAdLock(randomUuid()))
        val dto = AdCassandraDTO(new)

        return doDbAction(
            "update",
            {
                val res = dao.update(dto, prevLock).await()
                val isSuccess = res.wasApplied()
                val resultField = res.one()
                    ?.takeIf { it.columnDefinitions.contains(AdCassandraDTO.COLUMN_LOCK) }
                    ?.getString(AdCassandraDTO.COLUMN_LOCK)
                    ?.takeIf { it.isNotBlank() }
                when {
                    // Два варианта почти эквивалентны, выбирайте который вам больше подходит
                    isSuccess -> DbAdResponse.success(new)
                    // res.wasApplied() -> DbAdResponse.success(dao.read(idStr).await()?.toAdModel())
                    resultField == null -> DbAdResponse(null, false, ID_NOT_FOUND.errors)
                    else -> DbAdResponse(
                        dao.read(idStr).await()?.toAdModel(),
                        false,
                        CONCURRENT_MODIFICATION.errors
                    )
                }
            },
            ::errorToAdResponse
        )
    }

    override suspend fun deleteAd(rq: DbAdIdRequest): DbAdResponse {
        return doDbAction(
            "delete",
            {
                val idStr = rq.id.asString()
                val prevLock = rq.lock.asString()
                val oldAd = dao.read(idStr).await()?.toAdModel() ?: return@doDbAction ID_NOT_FOUND
                val res = dao.delete(idStr, prevLock).await()
                val isSuccess = res.wasApplied()
                val resultField = res.one()
                    ?.takeIf { it.columnDefinitions.contains(AdCassandraDTO.COLUMN_LOCK) }
                    ?.getString(AdCassandraDTO.COLUMN_LOCK)
                    ?.takeIf { it.isNotBlank() }
                when {
                    // Два варианта почти эквивалентны, выбирайте который вам больше подходит
                    isSuccess -> DbAdResponse.success(oldAd)
                    resultField == null -> DbAdResponse(null, false, ID_NOT_FOUND.errors)
                    else -> DbAdResponse(
                        dao.read(idStr).await()?.toAdModel(),
                        false,
                        CONCURRENT_MODIFICATION.errors
                    )
                }
            },
            ::errorToAdResponse
        )
    }

    override suspend fun searchAd(rq: DbAdFilterRequest): DbAdsResponse =
        doDbAction(
            "search",
            { dao.search(rq) },
            { found ->
                DbAdsResponse.success(found.map { it.toAdModel() })
            },
            ::errorToAdsResponse
        )

    companion object {
        private val ID_IS_EMPTY = DbAdResponse.error(InnerError(field = "id", message = "Id is empty"))
        private val ID_NOT_FOUND =
            DbAdResponse.error(InnerError(field = "id", code = "not-found", message = "Not Found"))
        private val CONCURRENT_MODIFICATION =
            DbAdResponse.error(InnerError(field = "lock", code = "concurrency", message = "Concurrent modification"))
    }
}

private fun parseAddresses(hosts: String, port: Int): Collection<InetSocketAddress> = hosts
    .split(Regex("""\s*,\s*"""))
    .map { InetSocketAddress(InetAddress.getByName(it), port) }
