package com.yans.calculator.data

import com.yans.calculator.exception.CalculatorException

sealed class Result<out T> {
    class Success<out T>(val value: T) : Result<T>()
    class Failure(val exception: CalculatorException) : Result<Nothing>()
}