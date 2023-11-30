import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.jackson.jackson
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import models.InnerAdId
import models.InnerAdLock
import models.InnerVisibility
import org.junit.Test
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
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdRequestDebugMode
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdSearchFilter
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdSearchRequest
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdSearchResponse
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdUpdateRequest
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdUpdateResponse
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdVisibility
import stubs.AdStub
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class V1AdInmemoryApiTest {
    private val uuidOld = "10000000-0000-0000-0000-000000000001"
    private val uuidNew = "10000000-0000-0000-0000-000000000002"
    private val uuidSup = "10000000-0000-0000-0000-000000000003"
    private val initAd = AdStub.prepareResult {
        id = InnerAdId(uuidOld)
        title = "abc"
        description = "abc"
        visibility = InnerVisibility.VISIBLE_PUBLIC
        lock = InnerAdLock(uuidOld)
    }
    private val initAdSupply = AdStub.prepareResult {
        id = InnerAdId(uuidSup)
        title = "abc"
        description = "abc"
        visibility = InnerVisibility.VISIBLE_PUBLIC
    }

    @Test
    fun create() = testApplication {
        val repo = AdRepoInMemory(initObjects = listOf(initAd), randomUuid = { uuidNew })
        application {
            moduleJvm(AppSettings(corSettings = MkplCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val createAd = AdCreateOrUpdateObject(
            title = "Болт",
            description = "КРУТЕЙШИЙ",
            visibility = AdVisibility.PUBLIC
        )

        val response = client.post("/v1/ad/create") {
            val requestObj = AdCreateRequest(
                requestId = "12345",
                adCreate = createAd,
                debug = AdRequestDebug(
                    mode = AdRequestDebugMode.TEST
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AdCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals(uuidNew, responseObj.ad?.id)
        assertEquals(createAd.title, responseObj.ad?.title)
        assertEquals(createAd.description, responseObj.ad?.description)
        assertEquals(createAd.visibility, responseObj.ad?.visibility)
    }

    @Test
    fun read() = testApplication {
        val repo = AdRepoInMemory(initObjects = listOf(initAd), randomUuid = { uuidNew })
        application {
            moduleJvm(AppSettings(corSettings = MkplCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val response = client.post("/v1/ad/read") {
            val requestObj = AdReadRequest(
                requestId = "12345",
                adRead = AdReadObject(uuidOld),
                debug = AdRequestDebug(
                    mode = AdRequestDebugMode.TEST
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AdReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals(uuidOld, responseObj.ad?.id)
    }

    @Test
    fun update() = testApplication {
        val repo = AdRepoInMemory(initObjects = listOf(initAd), randomUuid = { uuidNew })
        application {
            moduleJvm(AppSettings(corSettings = MkplCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val adUpdate = AdCreateOrUpdateObject(
            id = uuidOld,
            title = "Болт",
            description = "КРУТЕЙШИЙ",
            visibility = AdVisibility.PUBLIC,
            lock = initAd.lock.asString()
        )

        val response = client.post("/v1/ad/update") {
            val requestObj = AdUpdateRequest(
                requestId = "12345",
                adUpdate = adUpdate,
                debug = AdRequestDebug(
                    mode = AdRequestDebugMode.TEST
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AdUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals(adUpdate.id, responseObj.ad?.id)
        assertEquals(adUpdate.title, responseObj.ad?.title)
        assertEquals(adUpdate.description, responseObj.ad?.description)
        assertEquals(adUpdate.visibility, responseObj.ad?.visibility)
    }

    @Test
    fun delete() = testApplication {
        val repo = AdRepoInMemory(initObjects = listOf(initAd), randomUuid = { uuidNew })
        application {
            moduleJvm(AppSettings(corSettings = MkplCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val response = client.post("/v1/ad/delete") {
            val requestObj = AdDeleteRequest(
                requestId = "12345",
                adDelete = AdDeleteObject(
                    id = uuidOld,
                    lock = initAd.lock.asString()
                ),
                debug = AdRequestDebug(
                    mode = AdRequestDebugMode.TEST
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AdDeleteResponse>()
        assertEquals(200, response.status.value)
        assertEquals(uuidOld, responseObj.ad?.id)
    }

    @Test
    fun search() = testApplication {
        val repo = AdRepoInMemory(initObjects = listOf(initAd), randomUuid = { uuidNew })
        application {
            moduleJvm(AppSettings(corSettings = MkplCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val response = client.post("/v1/ad/search") {
            val requestObj = AdSearchRequest(
                requestId = "12345",
                adFilter = AdSearchFilter(),
                debug = AdRequestDebug(
                    mode = AdRequestDebugMode.TEST
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AdSearchResponse>()
        assertEquals(200, response.status.value)
        assertNotEquals(0, responseObj.ads?.size)
        assertEquals(uuidOld, responseObj.ads?.first()?.id)
    }

    private fun ApplicationTestBuilder.myClient() = createClient {
        install(ContentNegotiation) {
            jackson {
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                enable(SerializationFeature.INDENT_OUTPUT)
                writerWithDefaultPrettyPrinter()
            }
        }
    }
}
