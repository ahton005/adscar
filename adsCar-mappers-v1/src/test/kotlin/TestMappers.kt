import mappers.v1.toInnerContext
import mappers.v1.toTransport
import models.InnerAd
import models.InnerCommand
import models.InnerError
import models.InnerRequestId
import models.InnerState
import models.InnerVisibility.VISIBLE_PUBLIC
import models.InnerWorkMode
import org.junit.Test
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdCreateOrUpdateObject
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdCreateRequest
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdCreateResponse
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdRequestDebug
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdRequestDebugMode.STUB
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdRequestDebugStubs.SUCCESS
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdVisibility.PUBLIC
import stubs.InnerStubs
import kotlin.test.assertEquals

class TestMappers {
    @Test
    fun fromTransport() {
        val images = listOf("image1", "image2")
        val req = AdCreateRequest(
            requestId = "1234",
            debug = AdRequestDebug(mode = STUB, stub = SUCCESS),
            adCreate = AdCreateOrUpdateObject(
                title = "title",
                description = "desc",
                visibility = PUBLIC,
                logos = images
            )
        )

        val context = req.toInnerContext()

        assertEquals(InnerStubs.SUCCESS, context.stubCase)
        assertEquals(InnerWorkMode.STUB, context.workMode)
        assertEquals("title", context.adRequest.title)
        assertEquals("desc", context.adRequest.description)
        assertEquals(VISIBLE_PUBLIC, context.adRequest.visibility)
        assertEquals(images, context.adRequest.logos)
    }

    @Test
    fun toTransport() {
        val context = InnerContext(
            requestId = InnerRequestId("1234"),
            command = InnerCommand.CREATE,
            adResponse = InnerAd(title = "title", description = "desc", visibility = VISIBLE_PUBLIC),
            errors = mutableListOf(
                InnerError(code = "err", group = "request", field = "title", message = "wrong title")
            ),
            state = InnerState.RUNNING
        )

        val req = context.toTransport() as AdCreateResponse

        assertEquals("1234", req.requestId)
        assertEquals("title", req.ad?.title)
        assertEquals("desc", req.ad?.description)
        assertEquals(PUBLIC, req.ad?.visibility)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
        assertEquals("success", req.result?.value)
    }
}
