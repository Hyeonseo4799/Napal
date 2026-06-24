package org.burmese.napal.domain

data class Card(
    val byteArray: ByteArray? = null,
    val name: String = "",
    val tag: String = "",
    val message: String = ""
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as Card

        if (byteArray != null) {
            if (other.byteArray == null || !byteArray.contentEquals(other.byteArray)) return false
        } else if (other.byteArray != null){
            return false
        }
        return name == other.name && tag == other.tag && message == other.message
    }

    override fun hashCode(): Int {
        var result = byteArray?.contentHashCode() ?: 0
        result = 31 * result + name.hashCode()
        result = 31 * result + tag.hashCode()
        result = 31 * result + message.hashCode()
        return result
    }
}