package calculator

import calculator.calculator.Calculator

class Container(private val calculator: Calculator) {
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

    fun processExpression() {
        val expression = inputString.split(Regex(" +"))

        require(checkExpression(expression))

        println(calculator.calculate(expression, storedVariables))
    }

    fun processAssignment() {
        val explodedInput = inputString.split(Regex(" ?= ?"))
        val (variable, expressionString) = explodedInput
        val expression = expressionString.split(Regex(" +"))

        require(explodedInput.size == 2) {
            throw InvalidAssignmentException()
        }
        require(variable.matches(Regex("[a-zA-Z]+"))) {
            throw InvalidIdentifierException()
        }
        require(checkExpression(expression)) {
            throw InvalidAssignmentException()
        }

        storedVariables[variable] = calculator.calculate(expression, storedVariables)
    }

    private fun checkExpression(expression : List<String>) : Boolean {
        for (it in expression) {
            // if it is variable - should be in stored variable
            if (it.matches(Regex("\\(?[a-zA-Z]+\\)?"))) {
                if (!storedVariables.contains(it)) throw UnknownVariableException()
                continue
            }
            // if it is number - it should be valid number
            if (it.matches(Regex("\\(?-?[0-9]+\\)?"))) {
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
}