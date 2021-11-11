package calculator.calculator

import calculator.*
import calculator.checker.Checker

class CalculatorV7(checker : Checker) : Calculator(checker) {
    private val precedenceOfOperations : Map<String, Int> = mapOf(
        Pair(Operation.SUBTRACT.string, 0),
        Pair(Operation.ADD.string, 0),
        Pair(Operation.DIVIDE.string, 1),
        Pair(Operation.MULTIPLY.string, 1),
        Pair(Operation.POWER.string, 2),
        Pair(Operation.LEFT_PARENTHESIS.string, 3),
        Pair(Operation.RIGHT_PARENTHESIS.string, 3),
    )

    override fun calculate(expression: List<String>, variables: MutableMap<String, Int>): Int {
        val operands : Stack<String> = mutableListOf()
        val postfixNotation : MutableList<String> = mutableListOf()

        for (it in expression) {
            when {
                // Add operands (numbers and variables) to the result (postfix notation) as they arrive.
                checker.isValidVariable(it) || checker.isValidNumber(it) -> postfixNotation.push(it)

                // If the stack is empty or contains a left parenthesis on top, push the incoming operator on the stack.
                operands.isEmpty() || operands.peek() == Operation.LEFT_PARENTHESIS.string -> operands.push(it)

                // If the incoming element is a left parenthesis, push it on the stack.
                it == Operation.LEFT_PARENTHESIS.string -> operands.push(it)

                // If the incoming element is a right parenthesis, pop the stack and add operators to the result
                // until you see a left parenthesis. Discard the pair of parentheses.
                it == Operation.RIGHT_PARENTHESIS.string -> {
                    while (operands.peek() != Operation.LEFT_PARENTHESIS.string) {
                        postfixNotation.push(operands.pop()!!)
                    }
                    operands.pop()
                }

                // If the incoming operator has higher precedence than the top of the stack, push it on the stack.
                precedenceOfOperations[it]!! > precedenceOfOperations[operands.peek()]!! -> operands.push(it)

                // If the incoming operator has lower or equal precedence than or to the top of the stack,
                // pop the stack and add operators to the result until you see an operator that has a smaller
                // precedence or a left parenthesis on the top of the stack; then add the incoming operator to the stack.
                else -> {
                    while (operands.isNotEmpty()
                        // && it != operands.peek()!!
                        && precedenceOfOperations[it]!! <= precedenceOfOperations[operands.peek()]!!
                        && (operands.peek() ?: Operation.LEFT_PARENTHESIS.string) != Operation.LEFT_PARENTHESIS.string) {
                        postfixNotation.push(operands.pop()!!)
                    }

                    operands.push(it)
                }
            }
        }

        // At the end of the expression, pop the stack and add all operators to the result.
        if (operands.isNotEmpty()) {
            while (operands.size > 0) {
                val operand = operands.pop()!!

                if (arrayOf(Operation.LEFT_PARENTHESIS.string, Operation.RIGHT_PARENTHESIS.string).contains(operand)) {
                    throw InvalidExpressionException()
                }

                postfixNotation.push(operand)
            }
        }

        // calculate stage
        val resultStack : Stack<Int> = mutableListOf()

        for (it in postfixNotation) {
            when {
                // If the incoming element is a number, push it into the stack (the whole number, not a single digit!).
                checker.isValidNumber(it) -> resultStack.push(it.toInt())
                // If the incoming element is the name of a variable, push its value into the stack.
                checker.isValidVariable(it) -> resultStack.push(variables[it]!!)
                // If the incoming element is an operator, then pop twice to get two numbers and perform the operation; push the result on the stack.
                else -> {
                    val a = resultStack.pop() ?: 0
                    val b = resultStack.pop() ?: 0
                    resultStack.push(Operation.getOperationByString(it)!!.calculate(b, a))
                }
            }
        }

        return resultStack.peek()!!
    }
}