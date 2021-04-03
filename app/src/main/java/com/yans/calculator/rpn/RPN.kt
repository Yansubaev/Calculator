package com.yans.calculator.rpn

import android.util.Log
import com.yans.calculator.data.Result
import com.yans.calculator.data.code.ActionCode
import com.yans.calculator.data.code.KeyCode
import com.yans.calculator.exception.CalculatorException
import com.yans.calculator.exception.IncorrectInputException
import com.yans.calculator.exception.OtherException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RPN {
    suspend fun calculate(input: String?): Result<String> = withContext(Dispatchers.Default) {
        if (input != null) {
            var replacedInput = input.replace(KeyCode.KEY_PI.value, "3.1415926535")
                .replace(KeyCode.KEY_EXP.value, "2.7182818284")
                .replace(ActionCode.ACTION_PERCENT.value, "*0.01")
                .replace(ActionCode.ACTION_ROOT.value, "^0.5")

            var prevChar: Char? = null
            for (i in replacedInput.indices) {
                if (replacedInput[i] == '-') {
                    if (prevChar == null || prevChar == '(') {
                        replacedInput = replacedInput.replaceRange(i, i + 1, "#")
                    }
                }
                prevChar = replacedInput[i]
            }
            Log.d(this@RPN.javaClass.simpleName, replacedInput)
            try {
                val converted = RPNConverter().convert(replacedInput)
                val calculated = RPNCalculator().calculate(converted)
                val splitted = calculated.split('.')
                if (splitted.last() == "0") {
                    Result.Success(splitted.first())
                } else {
                    Result.Success(calculated)
                }
            } catch (exc: CalculatorException) {
                Result.Failure(exc)
            } catch (thr: Throwable) {
                Result.Failure(OtherException())
            }
        } else {
            Result.Failure(IncorrectInputException("Input string is null"))
        }
    }
}