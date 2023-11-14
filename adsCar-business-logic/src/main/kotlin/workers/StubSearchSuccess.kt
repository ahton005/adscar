package workers

import ICorChainDsl
import InnerContext
import models.InnerState
import stubs.AdStub
import stubs.InnerStubs
import worker

fun ICorChainDsl<InnerContext>.stubSearchSuccess(title: String) = worker {
    this.title = title
    on { stubCase == InnerStubs.SUCCESS && state == InnerState.RUNNING }
    handle {
        state = InnerState.FINISHING
        adsResponse.addAll(AdStub.prepareSearchList(adFilterRequest.searchString))
    }
}
