package repo

import AppSettings
import MkplCorSettings
import RepoAdCassandra
import RepoAdCreateTest.Companion.initObjects
import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT
import helpers.addAuth
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.contentType
import io.ktor.serialization.jackson.jackson
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import models.InnerAd
import models.InnerVisibility
import moduleJvm
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.testcontainers.containers.CassandraContainer
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdCreateOrUpdateObject
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdCreateRequest
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdCreateResponse
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdDeleteObject
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdDeleteRequest
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdDeleteResponse
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdReadObject
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdReadRequest
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdReadResponse
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdRequestDebug
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdRequestDebugMode.TEST
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdSearchFilter
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdSearchRequest
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdSearchResponse
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdUpdateRequest
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdUpdateResponse
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdVisibility
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdVisibility.PUBLIC
import stubs.AdStub
import java.time.Duration
import kotlin.test.assertEquals

class V1AdCassandraApiTest {
    @Test
    fun create() = testApplication {
        val client = myClient()
        application { moduleJvm(AppSettings(corSettings = MkplCorSettings(repoTest = repo))) }

        val response = client.post("/v1/ad/create") {
            val requestObj = AdCreateRequest(
                requestId = "1",
                requestType = "create",
                adCreate = testAd,
                debug = AdRequestDebug(mode = TEST)
            )
            addAuth()
            contentType(Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AdCreateResponse?>()
        assertEquals(200, response.status.value)
        assertEquals(testAd.title, responseObj?.ad?.title)
        assertEquals(testAd.description, responseObj?.ad?.description)
    }

    @Test
    fun read() = testApplication {
        val client = myClient()
        application { moduleJvm(AppSettings(corSettings = MkplCorSettings(repoTest = repo))) }

        val responseCreate = client.post("/v1/ad/create") {
            val requestObj = AdCreateRequest(
                requestId = "1",
                requestType = "create",
                adCreate = testAd,
                debug = AdRequestDebug(mode = TEST)
            )
            addAuth()
            contentType(Json)
            setBody(requestObj)
        }.body<AdCreateResponse?>()

        val response = client.post("/v1/ad/read") {
            val requestObj = AdReadRequest(
                requestId = "2",
                adRead = AdReadObject(responseCreate?.ad?.id),
                debug = AdRequestDebug(mode = TEST)
            )
            addAuth()
            contentType(Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AdReadResponse?>()
        assertEquals(200, response.status.value)
        assertEquals(testAd.title, responseObj?.ad?.title)
        assertEquals(testAd.description, responseObj?.ad?.description)
    }

    @Test
    fun update() = testApplication {
        val client = myClient()
        application { moduleJvm(AppSettings(corSettings = MkplCorSettings(repoTest = repo))) }

        val responseCreate = client.post("/v1/ad/create") {
            val requestObj = AdCreateRequest(
                requestId = "1",
                requestType = "create",
                adCreate = testAd,
                debug = AdRequestDebug(mode = TEST)
            )
            addAuth()
            contentType(Json)
            setBody(requestObj)
        }.body<AdCreateResponse?>()

        val newTestAd =
            testAd.copy(id = responseCreate?.ad?.id, title = "ТЕСТ", description = "Тест обновления", lock = "testLock")
        val response = client.post("/v1/ad/update") {
            val requestObj = AdUpdateRequest(
                requestId = "3",
                requestType = "update",
                adUpdate = newTestAd,
                debug = AdRequestDebug(mode = TEST)
            )
            addAuth()
            contentType(Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AdUpdateResponse?>()
        assertEquals(200, response.status.value)
        assertEquals(newTestAd.title, responseObj?.ad?.title)
        assertEquals(newTestAd.description, responseObj?.ad?.description)
    }

    @Test
    fun delete() = testApplication {
        val client = myClient()
        application { moduleJvm(AppSettings(corSettings = MkplCorSettings(repoTest = repo))) }

        val responseCreate = client.post("/v1/ad/create") {
            val requestObj = AdCreateRequest(
                requestId = "1",
                requestType = "create",
                adCreate = testAd,
                debug = AdRequestDebug(mode = TEST)
            )
            addAuth()
            contentType(Json)
            setBody(requestObj)
        }.body<AdCreateResponse?>()

        val response = client.post("/v1/ad/delete") {
            val requestObj = AdDeleteRequest(
                requestId = "4",
                adDelete = AdDeleteObject(responseCreate?.ad?.id, lock = "testLock"),
                debug = AdRequestDebug(mode = TEST)
            )
            addAuth()
            contentType(Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AdDeleteResponse?>()
        assertEquals(200, response.status.value)
        assertEquals(testAd.title, responseObj?.ad?.title)
        assertEquals(testAd.description, responseObj?.ad?.description)
    }

    @Test
    fun search() = testApplication {
        val client = myClient()
        application { moduleJvm(AppSettings(corSettings = MkplCorSettings(repoTest = repo))) }
        AdStub.prepareList().forEach {
            client.post("/v1/ad/create") {
                val requestObj = AdCreateRequest(
                    requestId = "1",
                    requestType = "create",
                    adCreate = it.toTransport(),
                    debug = AdRequestDebug(mode = TEST)
                )
                addAuth()
                contentType(Json)
                setBody(requestObj)
            }
        }
        val response = client.post("/v1/ad/search") {
            val requestObj = AdSearchRequest(
                requestId = "5",
                requestType = "search",
                adFilter = AdSearchFilter("ВАЗ"),
                debug = AdRequestDebug(mode = TEST)
            )
            addAuth()
            contentType(Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AdSearchResponse>()
        assertEquals(200, response.status.value)
        assertEquals("ВАЗ", responseObj.ads?.first()?.description)
        assert(responseObj.ads?.all { it.description?.contains("ВАЗ") ?: false } ?: true)
        assert(responseObj.ads?.all { !(it.description?.contains("ГАЗ") ?: true) } ?: true)
    }

    private fun ApplicationTestBuilder.myClient() = createClient {
        install(ContentNegotiation) {
            jackson {
                disable(FAIL_ON_UNKNOWN_PROPERTIES)

                enable(INDENT_OUTPUT)
                writerWithDefaultPrettyPrinter()
            }
        }
    }

    companion object {
        @BeforeClass
        @JvmStatic
        fun tearUp() {
        }

        @AfterClass
        @JvmStatic
        fun tearDown() {
            container.stop()
        }

        private val container by lazy {
            CassandraContainer("cassandra:3.11.2").apply {
                withExposedPorts(9042)
                withStartupTimeout(Duration.ofMinutes(4))
                start()
            }
        }

        private val repo by lazy {
            RepoAdCassandra(
                keyspaceName = "IntegrationTest",
                host = container.host,
                port = container.getMappedPort(9042),
                initObjects = initObjects,
                testing = true
            )
        }

        private val testAd = AdCreateOrUpdateObject(
            title = "Автомобиль 1",
            description = "БУ. Пробег 0 км",
            visibility = PUBLIC
        )

        private fun InnerAd.toTransport() = AdCreateOrUpdateObject(
            id = this.id.asString(),
            title = this.title,
            description = this.description,
            visibility = this.visibility.toTransportVisibility()
        )

        private fun InnerVisibility.toTransportVisibility() = when (this) {
            InnerVisibility.VISIBLE_PUBLIC -> AdVisibility.PUBLIC
            InnerVisibility.VISIBLE_TO_GROUP -> AdVisibility.REGISTERED_ONLY
            InnerVisibility.VISIBLE_TO_OWNER -> AdVisibility.OWNER_ONLY
            InnerVisibility.NONE -> null
        }
    }
}
