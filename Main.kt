package calculator

fun main() {
    val calculator = Calculator()

    while (true) {
        try {
            calculator.inputString = readLine()!!.toString()

            when {
                calculator.inputString.isEmpty() -> continue
                calculator.inputString.startsWith('/') -> {
                    when (calculator.inputString) {
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
                        else -> throw UnknownCommandException()
                    }
                }
                calculator.inputString.contains('=') -> calculator.processAssignment()
                else -> println(calculator.processExpression())
            }
        } catch (unknownVariableException : UnknownVariableException) {
            println("Unknown variable")
        } catch (unknownCommandException : UnknownCommandException) {
            println("Unknown command")
        } catch (invalidAssignmentException : InvalidAssignmentException) {
            println("Invalid assignment")
        } catch (invalidIdentifierException : InvalidIdentifierException) {
            println("Invalid identifier")
        } catch (wrongExpressionException : WrongExpressionException) {
            println("Wrong expression")
        }
    }
}

class UnknownCommandException() : Exception()
class UnknownVariableException() : Exception()
class WrongExpressionException() : Exception()
class InvalidAssignmentException() : Exception()
class InvalidIdentifierException() : Exception()