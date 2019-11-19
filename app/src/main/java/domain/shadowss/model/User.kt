package domain.shadowss.model

@Suppress("unused")
enum class User(
    val id: Int
) {
    GUEST(0),
    MANAGER(1),
    DRIVER(2);

    companion object {

        private val map = values().associateBy(User::id)

        fun fromId(id: Int) = map[id] ?: GUEST
    }
}