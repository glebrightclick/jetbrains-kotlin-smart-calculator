package calculator

class Calculator() {
    private val storedVariables : MutableMap<String, Int> = mutableMapOf()
    var inputString : String = ""
        set(inputString) {
            field = format(inputString)
        }

    private fun format(string : String) : String {
        var string = string.replace(Regex(" +"), " ").trim()
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

    fun processAssignment() {
        val explodedInput = inputString.split(Regex(" ?= ?"))
        val (variable, expression) = explodedInput

        require(explodedInput.size == 2) {
            throw InvalidAssignmentException()
        }
        require(variable.matches(Regex("[a-zA-Z]+"))) {
            throw InvalidIdentifierException()
        }
        require(checkExpression(expression.split(Regex(" +")))) {
            throw InvalidAssignmentException()
        }

        storedVariables[variable] = calculate(expression)
    }

    fun processExpression() = calculate(inputString)

    private fun checkExpression(expression : List<String>) : Boolean {
        for (it in expression) {
            // if it is variable - should be in stored variable
            if (it.matches(Regex("[a-zA-Z]+"))) {
                if (!storedVariables.contains(it)) throw UnknownVariableException()
                continue
            }
            // if it is number - it should be valid number
            if (it.matches(Regex("-?[0-9]+"))) {
                if (it != it.toInt().toString()) return false
                continue
            }
            // if it is operation - it is true
            if (it.matches(Regex("\\+|-|\\*|/"))) {
                continue
            }

            return false
        }

        return true
    }

    private fun checkResult(result : Int?, storedOperation : Operation?) : Boolean =
        result !== null && storedOperation === null

    private fun calculate(string : String) : Int {
        var result = 0
        var operation: Operation? = Operation.ADD
        val expression = string.split(Regex(" +"))

        require(checkExpression(expression))

        for (variable in expression) {
            when {
                variable == Operation.ADD.string -> operation = Operation.ADD
                variable == Operation.SUBTRACT.string -> operation = Operation.SUBTRACT
                // variable expression
                variable.matches(Regex("[a-zA-Z]+")) -> {
                    result = operation?.calculate(
                        result,
                        storedVariables[variable] ?: throw UnknownVariableException()
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