import models.InnerCommand
import models.InnerCommand.SEARCH
import models.InnerState
import models.InnerWorkMode.STUB
import stubs.AdStub

class AdProcessor {
    @Suppress("RedundantSuspendModifier")
    suspend fun exec(ctx: InnerContext) = run {
        // TODO: Rewrite temporary stub solution with BIZ
        require(ctx.workMode == STUB || ctx.command in arrayOf(InnerCommand.INIT, InnerCommand.FINISH)) {
            "Currently working only in STUB mode."
        }

        val newState = ctx.state.takeIf { it != InnerState.NONE } ?: InnerState.RUNNING
        when (ctx.command) {
            SEARCH -> {
                ctx.copy(adsResponse = AdStub.prepareSearchList(ctx.adFilterRequest.searchString), state = newState)
            }
            else -> {
                ctx.copy(adResponse = AdStub.get(), state = newState)
            }
        }
    }
}
