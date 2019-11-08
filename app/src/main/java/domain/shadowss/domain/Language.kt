package domain.shadowss.domain

@Suppress("unused")
enum class Language(
    val id: String,
    val desc: String
) {
    EN("en", "English"),
    UR("ur", "اردو"),
    HI("hi", "हिन्दी"),
    AR("ar", "العربية"),
    RU("ru", "Русский"),
    PA("pa", "ਪੰਜਾਬੀ");

    companion object {

        private val map = values().associateBy(Language::id)

        fun fromId(id: String?) = map[id] ?: EN
    }
}