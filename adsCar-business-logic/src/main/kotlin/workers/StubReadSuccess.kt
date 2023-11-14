package workers

import ICorChainDsl
import InnerContext
import models.InnerState
import stubs.AdStub
import stubs.InnerStubs
import worker

fun ICorChainDsl<InnerContext>.stubReadSuccess(title: String) = worker {
    this.title = title
    on { stubCase == InnerStubs.SUCCESS && state == InnerState.RUNNING }
    handle {
        state = InnerState.FINISHING
        val stub = AdStub.prepareResult {
            adRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
        }
        adResponse = stub
    }
}
