package calculator.checker

import calculator.Operation

class CheckerV7() : Checker() {
    override fun isValidVariable(string: String): Boolean = string.matches(Regex("[a-zA-Z]+"))

    override fun isValidNumber(string: String): Boolean = string.matches(Regex("-?[0-9]+"))

    override fun isValidOperation(string : String): Boolean = string.matches(Regex("\\+|-|\\*|/|\\^|\\)|\\("))
}