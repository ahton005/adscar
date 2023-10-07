import ru.zyablov.otus.otuskotlin.adscar.api.v1.apiV1Mapper
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdCreateResponse
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdResponseObject
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdVisibility.PUBLIC
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.Error
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.IResponse
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.ResponseResult.ERROR
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseSerializationTest {
    private val response = AdCreateResponse(
        requestId = "123",
        result = ERROR,
        errors = listOf(Error(code = "500", group = "Internal error", field = "adId", message = "Failed create")),
        ad = AdResponseObject(title = "ad title", description = "ad description", visibility = PUBLIC)
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(response)

        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
        assertContains(json, Regex("\"requestId\":\\s*\"123\""))
        assertContains(json, Regex("\"result\":\\s*\"error\""))
        assertContains(json, Regex("\"errors\""))
        assertContains(json, Regex("\"code\":\\s*\"500\""))
        assertContains(json, Regex("\"group\":\\s*\"Internal error\""))
        assertContains(json, Regex("\"field\":\\s*\"adId\""))
        assertContains(json, Regex("\"message\":\\s*\"Failed create\""))
        assertContains(json, Regex("\"title\":\\s*\"ad title\""))
        assertContains(json, Regex("\"description\":\\s*\"ad description\""))
        assertContains(json, Regex("\"visibility\":\\s*\"public\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as AdCreateResponse

        assertEquals(response, obj)
    }
}
