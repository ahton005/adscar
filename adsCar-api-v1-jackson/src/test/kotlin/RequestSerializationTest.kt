package ru.otus.otuskotlin.marketplace.api.v1

import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.*
import java.math.BigDecimal.ZERO
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestSerializationTest {
    private val request = AdCreateRequest(
        requestId = "123",
        debug = AdRequestDebug(
            mode = AdRequestDebugMode.STUB,
            stub = AdRequestDebugStubs.BAD_TITLE,
        ),
        adCreate = AdCreateOrUpdateObject(
            title = "ad title",
            description = "ad description",
            visibility = AdVisibility.PUBLIC,
            logos = listOf("image0", "image1"),
            ownerId = "11111",
            price = ZERO,
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(request)

        assertContains(json, Regex("\"requestId\":\\s*\"123\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badTitle\""))
        assertContains(json, Regex("\"title\":\\s*\"ad title\""))
        assertContains(json, Regex("\"description\":\\s*\"ad description\""))
        assertContains(json, Regex("\"visibility\":\\s*\"public\""))
        assertContains(json, Regex("\"logos\":\\s*\\[\"image0\""))
        assertContains(json, Regex("\"ownerId\":\\s*\"11111\""))
        assertContains(json, Regex("\"price\":\\s*0"))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as AdCreateRequest

        assertEquals(request, obj)
    }
}