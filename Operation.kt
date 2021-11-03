package calculator

enum class Operation(val string : String, private val function : (Int, Int) -> Int) {
    ADD("+", fun (a, b) = a + b),
    SUBTRACT("-", fun (a, b) = a - b),
    MULTIPLY("*", fun (a, b) = a * b),
    DIVIDE("/", fun (a, b) = if (b != 0) a / b else throw Exception("Division by zero"));

    fun calculate(a : Int, b : Int) = this.function(a, b)
}