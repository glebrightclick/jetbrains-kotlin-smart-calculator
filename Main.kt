package calculator

fun main() {
    while (true) {
        when (val input = readLine()!!.toString()) {
            "/exit" -> {
                println("Bye!")
                break
            }
            "/help" -> {
                println(
                    "The program calculates mathematical expressions" +
                    "Only add/subtract operations are supported"
                )
            }
            "" -> continue
            else -> {
                var string = input.replace(Regex(" +"), " ")
                do {
                    val previousString = string
                    string = string
                        .replace(Regex("\\++"), "+")
                        .replace(Regex("(\\+-)|(-\\+)"), "-")
                        .replace("--", "+")
                } while (previousString != string)

                var result = 0
                var action : Operation = Operation.ADD
                val expression = string.split(" ")
                for (variable in expression) {
                    when (variable) {
                        Operation.ADD.string -> action = Operation.ADD
                        Operation.SUBTRACT.string -> action = Operation.SUBTRACT
                        else -> result = action.calculate(result, variable.toInt())
                    }
                }

                println(result)
            }
        }
    }
}
