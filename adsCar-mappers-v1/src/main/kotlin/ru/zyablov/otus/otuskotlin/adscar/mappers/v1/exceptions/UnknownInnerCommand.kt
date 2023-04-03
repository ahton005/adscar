package ru.zyablov.otus.otuskotlin.adscar.mappers.v1.exceptions

import ru.zyablov.otus.otuskotlin.adscar.common.models.InnerCommand

class UnknownInnerCommand(command: InnerCommand) : Throwable("Wrong command $command at mapping ru.zyablov.otus.otuskotlin.adscar.mappers.v1.toTransport stage")
