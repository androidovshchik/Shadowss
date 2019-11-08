package domain.shadowss.domain

@Suppress("unused")
enum class MCC(
    val id: Int,
    val lang: String
) {
    C250(250, Language.RU.id),
    C404(404, Language.HI.id),
    C405(405, Language.HI.id),
    C410(410, Language.UR.id);

    companion object {

        private val map = values().associateBy(MCC::id)

        fun fromId(id: Int) = map[id]
    }
}