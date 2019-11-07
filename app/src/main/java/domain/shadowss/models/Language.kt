package domain.shadowss.models

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

        val map = values().associateBy(Language::id)

        fun fromId(id: String) = map[id]
    }
}