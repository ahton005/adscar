package models

@JvmInline
value class InnerAdId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = InnerAdId("")
    }
}
