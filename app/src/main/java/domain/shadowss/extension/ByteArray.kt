package domain.shadowss.extension

fun ByteArray.toHex(separator: String = ":"): String =
    joinToString(separator) { String.format("%02X", it) }