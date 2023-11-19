package groups

import ICorChainDsl
import InnerContext
import chain
import models.InnerCommand
import models.InnerState.RUNNING

fun ICorChainDsl<InnerContext>.operation(
    title: String,
    command: InnerCommand,
    block: ICorChainDsl<InnerContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == RUNNING }
}
