package calculator.checker

import calculator.Operation

abstract class Checker {
    abstract fun isValidVariable(string : String) : Boolean
    abstract fun isValidNumber(string : String) : Boolean
    abstract fun isValidOperation(string: String) : Boolean
}