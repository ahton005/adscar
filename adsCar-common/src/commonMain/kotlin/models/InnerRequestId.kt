package models

@JvmInline
value class InnerRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = InnerRequestId("")
    }
}
