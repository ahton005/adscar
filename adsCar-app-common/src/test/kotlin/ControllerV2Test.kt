import kotlinx.coroutines.test.runTest
import logging.MpLoggerProvider
import mappers.v1.toInnerContext
import mappers.v1.toTransport
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdCreateOrUpdateObject
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdCreateRequest
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdCreateResponse
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdRequestDebug
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdRequestDebugMode.STUB
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdRequestDebugStubs.SUCCESS
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdVisibility.PUBLIC
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.IRequest
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.IResponse
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.ResponseResult
import kotlin.test.Test
import kotlin.test.assertEquals

class ControllerV2Test {

    private val request = AdCreateRequest(
        requestType = "create",
        requestId = "1234",
        adCreate = AdCreateOrUpdateObject(
            title = "some ad",
            description = "some description of some ad",
            visibility = PUBLIC
        ),
        debug = AdRequestDebug(mode = STUB, stub = SUCCESS)
    )

    private val appSettings: IAppSettings = object : IAppSettings {
        override val processor: AdProcessor = AdProcessor()
        override val corSettings: MkplCorSettings = MkplCorSettings()
        override val logger: MpLoggerProvider = MpLoggerProvider()
        override val auth: AuthConfig = AuthConfig.NONE
    }

    class TestApplicationCall(private val request: IRequest) {
        var res: IResponse? = null

        @Suppress("UNCHECKED_CAST")
        fun <T : IRequest> receive(): T = request as T
        fun respond(res: IResponse) {
            this.res = res
        }
    }

    private suspend fun TestApplicationCall.createAdKtor(appSettings: IAppSettings) {
        val resp = appSettings.controllerHelper(
            { receive<AdCreateRequest>().toInnerContext() },
            { toTransport() },
            this::class,
            "Ktor"
        )
        respond(resp)
    }

    @Test
    fun ktorHelperTest() = runTest {
        val testApp = TestApplicationCall(request).apply { createAdKtor(appSettings) }
        val res = testApp.res as AdCreateResponse
        assertEquals(ResponseResult.SUCCESS, res.result)
    }
}
