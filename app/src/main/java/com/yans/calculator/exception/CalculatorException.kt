package com.yans.calculator.exception

abstract class CalculatorException(override val message: String?) : Throwable() {
}

class IncorrectInputException(message: String? = null)
    : CalculatorException(message ?: "Input is incorrect. Cannot calculate")

class ConverterException(message: String? = null)
    : CalculatorException(message ?: "Unable to convert string to reverse polish notation")

class OtherException(message: String? = null)
    : CalculatorException(message ?: "Some other exception")