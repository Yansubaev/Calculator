package com.yans.calculator.rpn

import com.yans.calculator.exception.OtherException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.pow

class RPNCalculator {
    private val stack: ArrayDeque<String> = ArrayDeque()

    suspend fun calculate(input: List<Pair<String, RPNElementType>>): String =
        withContext(Dispatchers.Default) {
            for (element in input) {
                when (element.second) {
                    RPNElementType.OPERAND -> {
                        stack.add(element.first)
                    }
                    RPNElementType.OPERATION -> {
                        calculateFor(element.first)
                    }
                }
            }
            if(stack.size != 1){
                throw OtherException()
            }
            stack.last()
        }

    private fun calculateFor(operation: String) {
        when (operation) {
            "+" -> {
                val second = stack.removeLast().toDouble()
                val first = stack.removeLast().toDouble()
                stack.add((first + second).toString())
            }
            "-" -> {
                val second = stack.removeLast().toDouble()
                val first = stack.removeLast().toDouble()
                stack.add((first - second).toString())
            }
            "*" -> {
                val second = stack.removeLast().toDouble()
                val first = stack.removeLast().toDouble()
                stack.add((first * second).toString())
            }
            "/" -> {
                val second = stack.removeLast().toDouble()
                val first = stack.removeLast().toDouble()
                stack.add((first / second).toString())
            }
            "^" -> {
                val second = stack.removeLast().toDouble()
                val first = stack.removeLast().toDouble()
                stack.add((first.pow(second)).toString())
            }
            "#" -> {
                val operand = stack.removeLast().toDouble()
                stack.add((operand * -1).toString())
            }
        }
    }
}