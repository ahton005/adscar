import com.benasher44.uuid.uuid4
import helpers.errorRepoConcurrency
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import model.AdEntity
import models.InnerAd
import models.InnerAdId
import models.InnerAdLock
import models.InnerError
import models.InnerUserId
import repo.DbAdFilterRequest
import repo.DbAdIdRequest
import repo.DbAdRequest
import repo.DbAdResponse
import repo.DbAdsResponse
import repo.IAdRepository
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class AdRepoInMemory(
    initObjects: Collection<InnerAd> = emptyList(),
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() }
) : IAdRepository {
    private val mutex: Mutex = Mutex()

    private val cache = Cache.Builder<String, AdEntity>()
        .expireAfterWrite(ttl)
        .build()

    init {
        initObjects.forEach {
            save(it)
        }
    }

    private fun save(ad: InnerAd) {
        val entity = AdEntity(ad)
        if (entity.id == null) {
            return
        }
        cache.put(entity.id, entity)
    }

    override suspend fun createAd(rq: DbAdRequest): DbAdResponse {
        val key = randomUuid()
        val ad = rq.ad.copy(id = InnerAdId(key))
        val entity = AdEntity(ad)
        cache.put(key, entity)
        return DbAdResponse(
            data = ad,
            isSuccess = true
        )
    }

    override suspend fun readAd(rq: DbAdIdRequest): DbAdResponse {
        val key = rq.id.takeIf { it != InnerAdId.NONE }?.asString() ?: return resultErrorEmptyId
        return cache.get(key)
            ?.let {
                DbAdResponse(
                    data = it.toInternal(),
                    isSuccess = true
                )
            } ?: resultErrorNotFound
    }

    private suspend fun doUpdate(
        key: String,
        oldLock: String,
        okBlock: (oldAd: AdEntity) -> DbAdResponse
    ): DbAdResponse = mutex.withLock {
        val oldAd = cache.get(key)
        when {
            oldAd == null -> resultErrorNotFound
            oldAd.lock != oldLock -> DbAdResponse(
                data = oldAd.toInternal(),
                isSuccess = false,
                errors = listOf(errorRepoConcurrency(InnerAdLock(oldLock), oldAd.lock?.let { InnerAdLock(it) }))
            )

            else -> okBlock(oldAd)
        }
    }

    override suspend fun updateAd(rq: DbAdRequest): DbAdResponse {
        val key = rq.ad.id.takeIf { it != InnerAdId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = rq.ad.lock.takeIf { it != InnerAdLock.NONE }?.asString() ?: return resultErrorEmptyLock
        val newAd = rq.ad.copy()
        val entity = AdEntity(newAd)
        return doUpdate(key, oldLock) {
            cache.put(key, entity)
            DbAdResponse(
                data = newAd,
                isSuccess = true
            )
        }
    }

    override suspend fun deleteAd(rq: DbAdIdRequest): DbAdResponse {
        val key = rq.id.takeIf { it != InnerAdId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = rq.lock.takeIf { it != InnerAdLock.NONE }?.asString() ?: return resultErrorEmptyLock

        return doUpdate(key, oldLock) { oldAd ->
            cache.invalidate(key)
            DbAdResponse(
                data = oldAd.toInternal(),
                isSuccess = true
            )
        }
    }

    override suspend fun searchAd(rq: DbAdFilterRequest): DbAdsResponse {
        val result = cache.asMap().asSequence()
            .filter { entry ->
                rq.ownerId.takeIf { it != InnerUserId.NONE }?.let {
                    it.asString() == entry.value.ownerId
                } ?: true
            }
            .filter { entry ->
                rq.titleFilter.takeIf { it.isNotBlank() }?.let {
                    entry.value.title?.contains(it) ?: false
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        return DbAdsResponse(
            data = result,
            isSuccess = true
        )
    }

    companion object {
        val resultErrorEmptyId = DbAdResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                InnerError(
                    code = "id-empty",
                    group = "validation",
                    field = "id",
                    message = "Id must not be null or blank"
                )
            )
        )
        val resultErrorNotFound = DbAdResponse(
            isSuccess = false,
            data = null,
            errors = listOf(
                InnerError(
                    code = "not-found",
                    field = "id",
                    message = "Not Found"
                )
            )
        )
        val resultErrorEmptyLock = DbAdResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                InnerError(
                    code = "lock-empty",
                    group = "validation",
                    field = "lock",
                    message = "Lock must not be null or blank"
                )
            )
        )
    }
}
