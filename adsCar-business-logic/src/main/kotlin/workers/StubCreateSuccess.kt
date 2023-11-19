package workers

import ICorChainDsl
import InnerContext
import models.InnerState
import models.InnerVisibility
import stubs.AdStub
import stubs.InnerStubs
import worker

fun ICorChainDsl<InnerContext>.stubCreateSuccess(title: String) = worker {
    this.title = title
    on { stubCase == InnerStubs.SUCCESS && state == InnerState.RUNNING }
    handle {
        state = InnerState.FINISHING
        adResponse = AdStub.prepareResult {
            adRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
            adRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
            adRequest.visibility.takeIf { it != InnerVisibility.NONE }?.also { this.visibility = it }
        }
    }
}
