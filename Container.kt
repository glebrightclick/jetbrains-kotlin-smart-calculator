package calculator

import calculator.calculator.Calculator
import calculator.checker.Checker

class Container(private val calculator: Calculator) {
    private val storedVariables : MutableMap<String, Int> = mutableMapOf()
    var inputString : String = ""
        set(inputString) {
            field = format(inputString)
        }

    private fun format(string : String) : String {
        var string = string

        for (operation in Operation.values()) {
            string = string.replace(operation.string, " ${operation.string} ")
        }
        string = string.replace(Regex(" +"), " ")
        string = string.trim()
        do {
            val previousString = string
            string = string
                .replace(Regex("\\+ ?\\+"), "+")
                .replace(Regex("\\* ?\\*"), "**")
                .replace(Regex("/ ?/"), "//")
                .replace(Regex("\\++"), "+")
                .replace(Regex("(\\+ ?-)|(- ?\\+)"), "-")
                .replace(Regex("- ?-"), "+")
                .replace(Regex("\\+([0-9]+)"), fun(matches: MatchResult) = matches.groupValues[1])
        } while (previousString != string)

        return string
    }

    fun processExpression() {
        val expression = inputString.split(Regex(" +"))

        require(checkExpression(expression)) {
            throw InvalidExpressionException()
        }

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
        if (expression.filter {
            it == Operation.LEFT_PARENTHESIS.string
        }.size != expression.filter {
            it == Operation.RIGHT_PARENTHESIS.string
        }.size) {
            return false
        }

        for (it in expression) {
            // if it is variable - should be in stored variable
            if (calculator.checker.isValidVariable(it)) {
                if (!storedVariables.contains(it)) throw UnknownVariableException()
                continue
            }
            // if it is number - it should be valid number
            if (calculator.checker.isValidNumber(it)) {
                if (it != it.toInt().toString()) return false
                continue
            }
            // if it is operation - it is true
            if (calculator.checker.isValidOperation(it)) {
                continue
            }

            return false
        }

        return true
    }
}