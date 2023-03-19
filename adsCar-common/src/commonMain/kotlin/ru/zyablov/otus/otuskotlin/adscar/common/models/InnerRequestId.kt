package ru.zyablov.otus.otuskotlin.adscar.common.models

@JvmInline
value class InnerRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = InnerRequestId("")
    }
}
