package com.yans.calculator.rpn

import com.yans.calculator.exception.ConverterException
import com.yans.calculator.exception.IncorrectInputException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RPNConverter {
    private val output: ArrayDeque<Pair<String, RPNElementType>> = ArrayDeque()
    private val operations: ArrayDeque<String> = ArrayDeque()

    companion object {
        private val operationOrder = mutableMapOf(
            Pair("#", 4),
            Pair("*", 3),
            Pair("/", 3),
            Pair("^", 3),
            Pair("+", 2),
            Pair("-", 2),
            Pair("(", 1)
        )
    }

    suspend fun convert(input: String): List<Pair<String, RPNElementType>> = withContext(Dispatchers.Default) {
        val inpList = convertToList(input)
        for (element in inpList) {
            when (element.second) {
                RPNElementType.OPERAND -> {
                    output.add(element)
                }
                RPNElementType.OPERATION -> {
                    if (element.first != "(" && element.first != ")") {
                        kek(element)
                    } else if (element.first == "(") {
                        operations.add(element.first)
                    } else if (element.first == ")") {
                        kek2()
                    } else {
                        throw IncorrectInputException("IDK Exception")
                    }
                }
            }
        }
        oneLastKek()
        output
    }

    private fun kek(element: Pair<String, RPNElementType>) {
        if (operations.isEmpty() || operationOrder[operations.last()]!! < operationOrder[element.first]!!) {
            operations.add(element.first)
        } else {
            output.add(Pair(operations.removeLast(), RPNElementType.OPERATION))
            kek(element)
        }
    }

    private fun kek2() {
        if (operations.last() != "(") {
            output.add(Pair(operations.removeLast(), RPNElementType.OPERATION))
            kek2()
        } else {
            operations.removeLast()
        }
    }

    private fun oneLastKek(){
        if(operations.isNotEmpty()){
            output.add(Pair(operations.removeLast(), RPNElementType.OPERATION))
            oneLastKek()
        }
    }

    private fun convertToList(input: String): List<Pair<String, RPNElementType>> {
        var number: String? = null
        val output = mutableListOf<Pair<String, RPNElementType>>()
        for (char in input) {
            if (char.isDigit() || char == '.') {
                if (number?.contains('.') == true && char == '.') {
                    throw ConverterException("Found two or more decimal places in a fractional number")
                }
                if (number == null) {
                    number = char.toString()
                } else {
                    number += char
                }
            } else {
                number?.let { output.add(Pair(it, RPNElementType.OPERAND)) }
                output.add(Pair(char.toString(), RPNElementType.OPERATION))
                number = null
            }
        }
        number?.let { output.add(Pair(it, RPNElementType.OPERAND)) }
        return output
    }
}