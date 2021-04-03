package com.yans.calculator.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yans.calculator.data.code.ActionCode
import com.yans.calculator.data.code.KeyCode
import com.yans.calculator.extensions.isDigitOr
import com.yans.calculator.extensions.isNullOrBlankOr
import com.yans.calculator.rpn.RPN
import kotlinx.coroutines.launch
import kotlin.contracts.ExperimentalContracts

@ExperimentalContracts
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val calculator: RPN = RPN()

    private val _inputData: MutableLiveData<String> = MutableLiveData()
    val inputData: LiveData<String>
        get() = _inputData

    private val _resultData: MutableLiveData<Result<String>> = MutableLiveData()
    val resultData: LiveData<Result<String>>
        get() = _resultData

    private val _historyData: MutableLiveData<List<HistoryEventData>> = MutableLiveData()
    val historyData: LiveData<List<HistoryEventData>>
        get() = _historyData

    private var calculableInput: String = ""

    init {
        _inputData.value = "0"
        calculableInput = "0"
    }

    fun keyClicked(key: KeyCode) {
        //appendInput(key.value)
        //appendCalculatableInput(key.value)
        appendKey(key.value)
        calculateQuietly()
    }

    fun actionClicked(actionCode: ActionCode) {
        when (actionCode) {
            ActionCode.ACTION_EQUALS -> {
                calculate()
                return
            }
            ActionCode.ACTION_PLUS -> {
                appendInput(ActionCode.ACTION_PLUS.value)
                appendCalculatableInput(ActionCode.ACTION_PLUS.value)
            }
            ActionCode.ACTION_MINUS -> {
                appendInput(ActionCode.ACTION_MINUS.value)
                appendCalculatableInput(ActionCode.ACTION_MINUS.value)
            }
            ActionCode.ACTION_MULTIPLY -> {
                appendInput(ActionCode.ACTION_MULTIPLY.value)
                appendCalculatableInput(ActionCode.ACTION_MULTIPLY.value)
            }
            ActionCode.ACTION_DIVIDE -> {
                appendInput(ActionCode.ACTION_DIVIDE.value)
                appendCalculatableInput(ActionCode.ACTION_DIVIDE.value)
            }
            ActionCode.ACTION_ROOT -> {
                surroundInput(ActionCode.ACTION_ROOT.value, true)
                surroundCalculatableInput(ActionCode.ACTION_ROOT.value, false)
            }
            ActionCode.ACTION_POWER -> {
                surroundInput("^2", false)
                surroundCalculatableInput("^2", false)
            }
            ActionCode.ACTION_PERCENT -> {
                surroundInput(ActionCode.ACTION_PERCENT.value, false)
                surroundCalculatableInput(ActionCode.ACTION_PERCENT.value, false)
            }
            ActionCode.ACTION_HYPER -> {
                surroundInput("1/", true)
                surroundCalculatableInput("1/", true)
            }
            ActionCode.ACTION_CLEAR -> {
                clearInputs()
            }
            ActionCode.ACTION_CLEAR_ALL -> {
                clearInputs()
                _resultData.value = Result.Success("")
            }
            ActionCode.ACTION_BACKSPACE -> {
                backspace()
            }
            ActionCode.ACTION_NEGATIVE -> {
                surroundInput("(-1)*", true)
                surroundCalculatableInput("(-1)*", true)
            }
            ActionCode.ACTION_LEFT_BRACKET -> {
                appendOpenBracket()
                //(ActionCode.ACTION_LEFT_BRACKET.value)
                appendCalculatableInput(ActionCode.ACTION_LEFT_BRACKET.value)
            }
            ActionCode.ACTION_RIGHT_BRACKET -> {
                appendInput(ActionCode.ACTION_RIGHT_BRACKET.value)
                appendCalculatableInput(ActionCode.ACTION_RIGHT_BRACKET.value)
            }
        }
        calculateQuietly()
    }

    private fun appendInput(sym: String) {
        val inp = _inputData.value
        _inputData.value = if (inp == "0") sym else inp + sym
    }

    private fun surroundInput(sym: String, before: Boolean) {
        val inp = _inputData.value
        if (inp.isNullOrBlankOr("0")) {
            return
        } else {
            _inputData.value = if (before) "$sym($inp)" else "($inp)$sym"
        }
    }

    private fun appendOpenBracket(){
        val inp = _inputData.value
        if(inp.isNullOrBlankOr("0")){
            clearInputs()
            appendInput(ActionCode.ACTION_LEFT_BRACKET.value)
            appendCalculatableInput(ActionCode.ACTION_LEFT_BRACKET.value)
        } else {
            if(inp!!.last().isDigitOr('π', 'e', '√', '%')){
                appendInput("*${ActionCode.ACTION_LEFT_BRACKET.value}")
                appendCalculatableInput("*${ActionCode.ACTION_LEFT_BRACKET.value}")
            } else {
                appendInput("(")
                appendCalculatableInput("(")
            }
        }
    }

    private fun appendKey(key: String){
        val inp = _inputData.value
        if(inp.isNullOrBlankOr("0")){
            clearInputs()
            appendInput(key)
            appendCalculatableInput(key)
        }else{
            if(inp!!.last() == ')') {
                appendInput("*$key")
                appendCalculatableInput("*$key")
            }else {
                appendInput(key)
                appendCalculatableInput(key)
            }
        }
    }

    private fun appendCalculatableInput(sym: String) {
        calculableInput = if (calculableInput == "0") sym else calculableInput + sym
    }

    private fun clearInputs() {
        calculableInput = ""
        _inputData.value = ""
    }

    private fun surroundCalculatableInput(sym: String, before: Boolean) {
        val inp = calculableInput
        calculableInput = if (inp == "0" || inp.isBlank()) {
            return
        } else {
            if (before) "$sym($inp)" else "($inp)$sym"
        }
    }

    private fun backspace() {
        var root = false
        calculableInput.apply {
            if (isNotBlank()) {
                if(last().toString() == ActionCode.ACTION_ROOT.value){
                    root = true
                }
                calculableInput = removeRange(length - 1, length)
            }
        }
        val inp = _inputData.value
        inp?.let {
            if (it.isNotBlank())
                _inputData.value =
                    if(root) it.replaceFirst(ActionCode.ACTION_ROOT.value, "")
                    else it.removeRange(it.length - 1, it.length)
        }
    }

    private fun calculate() = viewModelScope.launch {
        val res = calculator.calculate(calculableInput)
        if (res is Result.Success) {
            addToHistory(_inputData.value ?: "null", res.value)
            _inputData.value = res.value
            calculableInput = res.value
            _resultData.value = Result.Success("")
        } else {
            _resultData.value = res
        }
    }

    private fun calculateQuietly() = viewModelScope.launch {
        val res = calculator.calculate(calculableInput)
        if (res is Result.Success) {
            _resultData.value = res
        }
    }

    private fun addToHistory(input: String, result: String) {
        if (_historyData.value.isNullOrEmpty()) {
            _historyData.value = listOf(
                HistoryEventData(
                    input.hashCode(),
                    input,
                    result
                )
            )
        } else {
            val history = _historyData.value?.toMutableList()
            history?.add(
                HistoryEventData(
                    input.hashCode(),
                    input,
                    result
                )
            )
            _historyData.value = history
        }
    }
}