data class User(val id: Long, val name: String?)

fun User.displayName(): String = name ?: "Unknown"

fun main() {
    val users = listOf(User(1, "Dustin"), User(2, null))
    val names = users.map { it.displayName() }

    println(names)
}

