package calculator

import kotlin.math.pow

enum class Operation(val string : String, private val function : (Int, Int) -> Int) {
    ADD("+", fun (a, b) = a + b),
    SUBTRACT("-", fun (a, b) = a - b),
    MULTIPLY("*", fun (a, b) = a * b),
    DIVIDE("/", fun (a, b) = if (b != 0) a / b else throw Exception("Division by zero")),
    POWER("^", fun (a, b) = a.toDouble().pow(b).toInt()),

    LEFT_PARENTHESIS("(", fun (_, _) = 0),
    RIGHT_PARENTHESIS(")", fun (_, _) = 0);

    fun calculate(a : Int, b : Int) = this.function(a, b)

    companion object {
        fun getOperationByString(string : String) : Operation? {
            for (operation in values()) {
                if (operation.string == string) return operation
            }

            return null
        }
    }
}