package ru.zyablov.otus.otuskotlin.adscar.common

import kotlinx.datetime.Instant
import ru.zyablov.otus.otuskotlin.adscar.common.models.InnerAd
import ru.zyablov.otus.otuskotlin.adscar.common.models.InnerAdFilter
import ru.zyablov.otus.otuskotlin.adscar.common.models.InnerCommand
import ru.zyablov.otus.otuskotlin.adscar.common.models.InnerError
import ru.zyablov.otus.otuskotlin.adscar.common.models.InnerRequestId
import ru.zyablov.otus.otuskotlin.adscar.common.models.InnerState
import ru.zyablov.otus.otuskotlin.adscar.common.models.InnerWorkMode
import ru.zyablov.otus.otuskotlin.adscar.common.stubs.InnerStubs

data class InnerContext(
    val command: InnerCommand = InnerCommand.NONE,
    val state: InnerState = InnerState.NONE,
    val errors: List<InnerError> = listOf(),

    val workMode: InnerWorkMode = InnerWorkMode.PROD,
    val stubCase: InnerStubs = InnerStubs.NONE,

    val requestId: InnerRequestId = InnerRequestId.NONE,
    val timeStart: Instant = Instant.NONE,
    val adRequest: InnerAd = InnerAd(),
    val adFilterRequest: InnerAdFilter = InnerAdFilter(),
    val adResponse: InnerAd = InnerAd(),
    val adsResponse: List<InnerAd> = listOf()
)
