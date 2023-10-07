package mappers.v1.exceptions

import models.InnerCommand

class UnknownInnerCommand(command: InnerCommand) : Throwable("Wrong command $command at mapping mappers.v1.toTransport stage")
