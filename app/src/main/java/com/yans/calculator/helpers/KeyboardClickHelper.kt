package com.yans.calculator.helpers

import android.view.View
import com.yans.calculator.R
import com.yans.calculator.data.code.ActionCode
import com.yans.calculator.data.code.KeyCode
import com.yans.calculator.databinding.ActivityMainBinding

class KeyboardClickHelper(binding: ActivityMainBinding) : View.OnClickListener {

    private var onKeyClickListener: ((key: KeyCode) -> Unit)? = null

    private var onActionClickListener: ((action: ActionCode) -> Unit)? = null

    init {
        binding.itemKeyboard.key0.setOnClickListener(this)
        binding.itemKeyboard.key1.setOnClickListener(this)
        binding.itemKeyboard.key2.setOnClickListener(this)
        binding.itemKeyboard.key3.setOnClickListener(this)
        binding.itemKeyboard.key4.setOnClickListener(this)
        binding.itemKeyboard.key5.setOnClickListener(this)
        binding.itemKeyboard.key6.setOnClickListener(this)
        binding.itemKeyboard.key7.setOnClickListener(this)
        binding.itemKeyboard.key8.setOnClickListener(this)
        binding.itemKeyboard.key9.setOnClickListener(this)
        binding.itemKeyboard.btnDot.setOnClickListener(this)
        binding.itemKeyboard.keyExp.setOnClickListener(this)
        binding.itemKeyboard.keyPi.setOnClickListener(this)
        binding.itemKeyboard.btnNegative.setOnClickListener(this)
        binding.itemKeyboard.btnPlus.setOnClickListener(this)
        binding.itemKeyboard.btnMinus.setOnClickListener(this)
        binding.itemKeyboard.btnMultiple.setOnClickListener(this)
        binding.itemKeyboard.btnDivide.setOnClickListener(this)
        binding.itemKeyboard.btnPower.setOnClickListener(this)
        binding.itemKeyboard.btnHyper.setOnClickListener(this)
        binding.itemKeyboard.btnRoot.setOnClickListener(this)
        binding.itemKeyboard.btnClear.setOnClickListener(this)
        binding.itemKeyboard.btnClearAll.setOnClickListener(this)
        binding.itemKeyboard.btnEquals.setOnClickListener(this)
        binding.itemKeyboard.btnPercent.setOnClickListener(this)
        binding.itemKeyboard.btnBackspace.setOnClickListener(this)
        binding.itemKeyboard.btnLeftBracket.setOnClickListener(this)
        binding.itemKeyboard.btnRightBracket.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        v?.let {
            when (v.id) {
                R.id.key_0 -> onKeyClickListener?.invoke(KeyCode.KEY_0)
                R.id.key_1 -> onKeyClickListener?.invoke(KeyCode.KEY_1)
                R.id.key_2 -> onKeyClickListener?.invoke(KeyCode.KEY_2)
                R.id.key_3 -> onKeyClickListener?.invoke(KeyCode.KEY_3)
                R.id.key_4 -> onKeyClickListener?.invoke(KeyCode.KEY_4)
                R.id.key_5 -> onKeyClickListener?.invoke(KeyCode.KEY_5)
                R.id.key_6 -> onKeyClickListener?.invoke(KeyCode.KEY_6)
                R.id.key_7 -> onKeyClickListener?.invoke(KeyCode.KEY_7)
                R.id.key_8 -> onKeyClickListener?.invoke(KeyCode.KEY_8)
                R.id.key_9 -> onKeyClickListener?.invoke(KeyCode.KEY_9)
                R.id.key_pi -> onKeyClickListener?.invoke(KeyCode.KEY_PI)
                R.id.key_exp -> onKeyClickListener?.invoke(KeyCode.KEY_EXP)
                R.id.btn_dot -> onKeyClickListener?.invoke(KeyCode.KEY_DOT)
                R.id.btn_plus -> onActionClickListener?.invoke(ActionCode.ACTION_PLUS)
                R.id.btn_minus -> onActionClickListener?.invoke(ActionCode.ACTION_MINUS)
                R.id.btn_multiple -> onActionClickListener?.invoke(ActionCode.ACTION_MULTIPLY)
                R.id.btn_divide -> onActionClickListener?.invoke(ActionCode.ACTION_DIVIDE)
                R.id.btn_power -> onActionClickListener?.invoke(ActionCode.ACTION_POWER)
                R.id.btn_root -> onActionClickListener?.invoke(ActionCode.ACTION_ROOT)
                R.id.btn_hyper -> onActionClickListener?.invoke(ActionCode.ACTION_HYPER)
                R.id.btn_clear -> onActionClickListener?.invoke(ActionCode.ACTION_CLEAR)
                R.id.btn_clear_all -> onActionClickListener?.invoke(ActionCode.ACTION_CLEAR_ALL)
                R.id.btn_backspace -> onActionClickListener?.invoke(ActionCode.ACTION_BACKSPACE)
                R.id.btn_negative -> onActionClickListener?.invoke(ActionCode.ACTION_NEGATIVE)
                R.id.btn_equals -> onActionClickListener?.invoke(ActionCode.ACTION_EQUALS)
                R.id.btn_percent -> onActionClickListener?.invoke(ActionCode.ACTION_PERCENT)
                R.id.btn_left_bracket -> onActionClickListener?.invoke(ActionCode.ACTION_LEFT_BRACKET)
                R.id.btn_right_bracket -> onActionClickListener?.invoke(ActionCode.ACTION_RIGHT_BRACKET)
                else -> {
                }
            }
        }
    }

    fun setOnKeyClickListener(l: (key: KeyCode) -> Unit) {
        onKeyClickListener = l
    }

    fun removeOnKeyClickListener() {
        onKeyClickListener = null
    }

    fun setOnActionClickListener(l: (action: ActionCode) -> Unit) {
        onActionClickListener = l
    }

    fun removeOnActionClickListener() {
        onActionClickListener = null
    }
}