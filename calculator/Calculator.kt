package calculator.calculator

abstract class Calculator {
    abstract fun calculate(expression : List<String>, variables : MutableMap<String, Int>) : Int
}