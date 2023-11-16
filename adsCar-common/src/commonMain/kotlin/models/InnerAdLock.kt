package models

import kotlin.jvm.JvmInline

@JvmInline
value class InnerAdLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = InnerAdLock("")
    }
}
