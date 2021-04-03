package com.yans.calculator.data

data class HistoryEventData(
    val id: Int,
    val input: CharSequence,
    val result: CharSequence
){
    fun contentEquals(other: HistoryEventData): Boolean {
        if(other.id != id) return false
        if(other.input != input) return false
        if(other.result != result) return false
        return true
    }
}