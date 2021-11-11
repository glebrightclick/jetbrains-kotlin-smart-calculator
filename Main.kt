package calculator

import calculator.calculator.CalculatorV6
import calculator.calculator.CalculatorV7
import calculator.checker.CheckerV7

fun main() {
    val checker = CheckerV7()
    val container = Container(CalculatorV7(checker))

    while (true) {
        try {
            val inputString = readLine()!!.toString()
            container.inputString = inputString

            when {
                inputString.isEmpty() -> continue
                inputString.startsWith('/') -> {
                    when (inputString) {
                        "/exit" -> {
                            println("Bye!")
                            break
                        }
                        "/help" -> {
                            println(
                                "The program calculates mathematical expressions\n" +
                                        "Only add/subtract operations are supported"
                            )
                        }
                        else -> throw UnknownCommandException()
                    }
                }
                inputString.contains('=') -> container.processAssignment()
                else -> container.processExpression()
            }
        } catch (invalidExpressionException : InvalidExpressionException) {
            println("Invalid Expression")
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
class InvalidExpressionException() : Exception()