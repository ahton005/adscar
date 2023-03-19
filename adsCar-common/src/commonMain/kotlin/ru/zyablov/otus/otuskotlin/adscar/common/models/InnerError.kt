package ru.zyablov.otus.otuskotlin.adscar.common.models

data class InnerError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null,
)