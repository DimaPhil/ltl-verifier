package console

class ConsoleColorer {
    companion object {
        private const val ESC = "\u001B"
        private const val NORMAL = "$ESC[0"
        private const val WHITE  = "$ESC[0;37m"

        private val foreColors = listOf(
                ";31m" to "red",
                ";32m" to "green",
                ";33m" to "yellow",
                ";34m" to "blue",
                ";35m" to "magenta",
                ";36m" to "cyan",
                ";37m" to "white"
        )

        fun print(text: String, color: String) {
            val prefix = foreColors.find { it.second == color }?.first
            if (prefix == null) {
                println("Unknown color $color")
                return
            }
            print("$NORMAL$prefix$text")
            println(WHITE)
        }
    }
}