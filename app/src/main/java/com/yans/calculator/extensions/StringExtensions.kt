package com.yans.calculator.extensions

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@ExperimentalContracts
fun String?.isNullOrBlankOr(vararg strings: String) : Boolean{
    contract {
        returns(false) implies (this@isNullOrBlankOr != null)
    }
    return if(isNullOrBlank()) true else strings.contains(this)
}

fun String.isBlankOr(vararg strings: String) : Boolean =
    if(isBlank()) true else strings.contains(this)

