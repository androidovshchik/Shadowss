package domain.shadowss.models

@Suppress("unused")
enum class MCC(
    val id: Int,
    val lang: String
) {
    RU250(250, "ru"),
    HI404(404, "hi"),
    HI405(405, "hi"),
    UR410(410, "ur");

    companion object {

        private val map = values().associateBy(MCC::id)

        fun fromId(id: Int) = map[id]
    }
}