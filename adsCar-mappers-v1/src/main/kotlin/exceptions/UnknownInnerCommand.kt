package exceptions

import models.InnerCommand

class UnknownInnerCommand(command: InnerCommand) : Throwable("Wrong command $command at mapping toTransport stage")
