package ru.zyablov.otus.otuskotlin.adscar.common.models

@JvmInline
value class InnerUserId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = InnerUserId("")
    }
}
