import ru.zyablov.otus.otuskotlin.adscar.common.InnerContext
import ru.zyablov.otus.otuskotlin.adscar.common.models.InnerCommand.SEARCH
import ru.zyablov.otus.otuskotlin.adscar.common.models.InnerWorkMode.STUB
import stubs.AdStub

class AdProcessor {
    suspend fun exec(ctx: InnerContext) = run {
        // TODO: Rewrite temporary stub solution with BIZ
        require(ctx.workMode == STUB) {
            "Currently working only in STUB mode."
        }

        when (ctx.command) {
            SEARCH -> {
                ctx.copy(adsResponse = AdStub.prepareSearchList(ctx.adFilterRequest.searchString))
            }
            else -> {
                ctx.copy(adResponse = AdStub.get())
            }
        }
    }
}
