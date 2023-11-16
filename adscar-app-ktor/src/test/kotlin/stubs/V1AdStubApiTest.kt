package stubs

import AppSettings
import MkplCorSettings
import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.contentType
import io.ktor.serialization.jackson.jackson
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import moduleJvm
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
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdRequestDebugMode.STUB
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdRequestDebugStubs.SUCCESS
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdSearchFilter
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdSearchRequest
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdSearchResponse
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdUpdateRequest
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdUpdateResponse
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdVisibility.PUBLIC
import java.math.BigDecimal
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class V1AdStubApiTest {
    @Test
    fun create() = testApplication {
        val client = myClient()
        application { moduleJvm(AppSettings(corSettings = MkplCorSettings())) }

        val response = client.post("/v1/ad/create") {
            val requestObj = AdCreateRequest(
                requestId = "1",
                requestType = "create",
                adCreate = AdCreateOrUpdateObject(
                    title = "Автомобиль 1",
                    description = "БУ. Пробег 0 км",
                    visibility = PUBLIC
                ),
                debug = AdRequestDebug(
                    mode = STUB,
                    stub = SUCCESS
                )
            )
            contentType(Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AdCreateResponse?>()
        println(responseObj)
        assertEquals(200, response.status.value)
        assertEquals("777", responseObj?.ad?.id)
        assertEquals(BigDecimal("100000"), responseObj?.ad?.price)
    }

    @Test
    fun read() = testApplication {
        val client = myClient()
        application { moduleJvm(AppSettings(corSettings = MkplCorSettings())) }
        val response = client.post("/v1/ad/read") {
            val requestObj = AdReadRequest(
                requestId = "2",
                adRead = AdReadObject("666"),
                debug = AdRequestDebug(
                    mode = STUB,
                    stub = SUCCESS
                )
            )
            contentType(Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AdReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals("777", responseObj.ad?.id)
        assertEquals(BigDecimal("100000"), responseObj.ad?.price)
    }

    @Test
    fun update() = testApplication {
        val client = myClient()
        application { moduleJvm(AppSettings(corSettings = MkplCorSettings())) }
        val response = client.post("/v1/ad/update") {
            val requestObj = AdUpdateRequest(
                requestId = "3",
                requestType = "update",
                adUpdate = AdCreateOrUpdateObject(
                    title = "Автомобиль 1",
                    description = "БУ. Пробег 0 км",
                    visibility = PUBLIC
                ),
                debug = AdRequestDebug(
                    mode = STUB,
                    stub = SUCCESS
                )
            )
            contentType(Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AdUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("777", responseObj.ad?.id)
        assertEquals(BigDecimal("100000"), responseObj.ad?.price)
    }

    @Test
    fun delete() = testApplication {
        val client = myClient()
        application { moduleJvm(AppSettings(corSettings = MkplCorSettings())) }
        val response = client.post("/v1/ad/delete") {
            val requestObj = AdDeleteRequest(
                requestId = "4",
                adDelete = AdDeleteObject(
                    id = "777"
                ),
                debug = AdRequestDebug(
                    mode = STUB,
                    stub = SUCCESS
                )
            )
            contentType(Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AdDeleteResponse>()
        assertEquals(200, response.status.value)
        assertEquals("777", responseObj.ad?.id)
        assertEquals(BigDecimal("100000"), responseObj.ad?.price)
    }

    @Test
    fun search() = testApplication {
        val client = myClient()
        application { moduleJvm(AppSettings(corSettings = MkplCorSettings())) }
        val response = client.post("/v1/ad/search") {
            val requestObj = AdSearchRequest(
                requestId = "5",
                adFilter = AdSearchFilter("ВАЗ"),
                debug = AdRequestDebug(
                    mode = STUB,
                    stub = SUCCESS
                )
            )
            contentType(Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AdSearchResponse>()
        assertEquals(200, response.status.value)
        assertEquals("ВАЗ", responseObj.ads?.first()?.description)
        assertEquals("ВАЗ", responseObj.ads?.first()?.description)
        assertEquals("ВАЗ", responseObj.ads?.first()?.description)
        assertContains(listOf("4", "5", "6"), responseObj.ads?.first()?.id)
        assertTrue { !(responseObj.ads?.any { listOf("1", "2", "3").contains(it.id) } ?: false) }
        assert(responseObj.ads?.all { it.description?.contains("ВАЗ") ?: false } ?: true)
        assert(responseObj.ads?.all { it.description?.contains("ВАЗ") ?: false } ?: true)
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
}
