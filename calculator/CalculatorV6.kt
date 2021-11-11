package calculator.calculator

import calculator.Operation
import calculator.UnknownVariableException
import calculator.WrongExpressionException
import calculator.checker.Checker

class CalculatorV6(checker: Checker) : Calculator(checker) {
    private fun checkResult(result : Int?, storedOperation : Operation?) : Boolean =
        result !== null && storedOperation === null

    override fun calculate(expression : List<String>, variables : MutableMap<String, Int>) : Int {
        var result = 0
        var operation: Operation? = Operation.ADD

        for (variable in expression) {
            when {
                variable == Operation.ADD.string -> operation = Operation.ADD
                variable == Operation.SUBTRACT.string -> operation = Operation.SUBTRACT
                // variable expression
                variable.matches(Regex("[a-zA-Z]+")) -> {
                    result = operation?.calculate(
                        result,
                        variables[variable] ?: throw UnknownVariableException()
                    ) ?: throw WrongExpressionException()
                    operation = null
                }
                // number expression
                variable.matches(Regex("-?[0-9]+")) -> {
                    result = operation?.calculate(
                        result,
                        variable.toInt()
                    ) ?: throw WrongExpressionException()
                    operation = null
                }
            }
        }

        require(checkResult(result, operation))

        return result
    }
}