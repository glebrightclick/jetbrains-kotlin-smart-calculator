package calculator

import calculator.calculator.CalculatorV6

fun main() {
    val container = Container(CalculatorV6())

    while (true) {
        try {
            container.inputString = readLine()!!.toString()

            when {
                container.inputString.isEmpty() -> continue
                container.inputString.startsWith('/') -> {
                    when (container.inputString) {
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
                container.inputString.contains('=') -> container.processAssignment()
                else -> container.processExpression()
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