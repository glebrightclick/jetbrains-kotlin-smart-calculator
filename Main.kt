package calculator

fun main() {
    while (true) {
        try {
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
                    require(isValidInput(input))

                    val string = format(input)
                    var result = 0
                    var operation: Operation? = Operation.ADD
                    val expression = string.split(" ")
                    for (variable in expression) {
                        when (variable) {
                            Operation.ADD.string -> operation = Operation.ADD
                            Operation.SUBTRACT.string -> operation = Operation.SUBTRACT
                            else -> {
                                require(checkInputVariable(result, operation, variable))

                                result = operation!!.calculate(result, variable.toInt())
                                operation = null
                            }
                        }
                    }

                    require(checkResult(result, operation))
                    println(result)
                }
            }
        } catch (unknownCommandException : UnknownCommandException) {
            println("Unknown command")
        } catch (exception : Exception) {
            println("Invalid expression")
        }
    }
}

class UnknownCommandException() : Exception()

fun format(input : String) : String {
    var string = input.replace(Regex(" +"), " ")
    do {
        val previousString = string
        string = string
            .replace(Regex("\\++"), "+")
            .replace(Regex("(\\+-)|(-\\+)"), "-")
            .replace("--", "+")
            .replace(Regex("\\+([0-9]+)"), fun(matches: MatchResult) = matches.groupValues[1])
    } while (previousString != string)

    return string
}

fun isValidInput(input : String) : Boolean {
    if (input.startsWith('/')) throw UnknownCommandException()

    return true
}

fun checkInputVariable(result : Int, storedOperation : Operation?, variable : String) =
    storedOperation !== null
        && variable == variable.toInt().toString()

fun checkResult(result : Int, storedOperation : Operation?) : Boolean =
    storedOperation === null