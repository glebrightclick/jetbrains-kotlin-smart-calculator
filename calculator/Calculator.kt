package calculator.calculator

import calculator.checker.Checker

abstract class Calculator(val checker : Checker) {
    abstract fun calculate(expression : List<String>, variables : MutableMap<String, Int>) : Int
}