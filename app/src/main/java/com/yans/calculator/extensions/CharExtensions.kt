package com.yans.calculator.extensions

fun Char.isDigitOr(vararg chars: Char): Boolean =
    if (isDigit()) true else chars.contains(this)
