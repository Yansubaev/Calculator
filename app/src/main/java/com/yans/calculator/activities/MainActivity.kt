package com.yans.calculator.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.yans.calculator.R
import com.yans.calculator.data.MainViewModel
import com.yans.calculator.data.Result
import com.yans.calculator.databinding.ActivityMainBinding
import com.yans.calculator.exception.ConverterException
import com.yans.calculator.exception.IncorrectInputException
import com.yans.calculator.helpers.KeyboardClickHelper
import com.yans.calculator.recycler.HistoryRecyclerViewAdapter
import kotlin.contracts.ExperimentalContracts

@ExperimentalContracts
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var keyboardHelper: KeyboardClickHelper

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        ).get(MainViewModel::class.java)
    }

    private var inputDataObserver: Observer<String> = Observer {
        binding.txtInput.text = it
    }

    private var resultDataObserver: Observer<Result<String>> = Observer {
        when (val res = it) {
            is Result.Success -> {
                binding.txtMainResult.text = res.value
            }
            is Result.Failure -> {
                Log.e(this.javaClass.simpleName, res.exception.message, res.exception)
                when (res.exception){
                    is IncorrectInputException, is ConverterException -> {
                        binding.txtMainResult.text = resources.getString(R.string.input_fail_message)
                    }
                    else -> {
                        binding.txtMainResult.text = resources.getString(R.string.calculate_fail)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        keyboardHelper = KeyboardClickHelper(binding)

        keyboardHelper.setOnKeyClickListener { keyCode ->
            viewModel.keyClicked(keyCode)
        }
        keyboardHelper.setOnActionClickListener { actionCode ->
            viewModel.actionClicked(actionCode)
        }

        binding.itemHistory?.let { itemHistoryBinding ->
            val adapter = HistoryRecyclerViewAdapter(this)
            itemHistoryBinding.recHistory.layoutManager = GridLayoutManager(this, 1)
            itemHistoryBinding.recHistory.adapter = adapter
            viewModel.historyData.observe(this){
                adapter.submitList(it)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.inputData.observe(this, inputDataObserver)
        viewModel.resultData.observe(this, resultDataObserver)
    }

    override fun onStop() {
        super.onStop()
        viewModel.inputData.removeObserver(inputDataObserver)
        viewModel.resultData.removeObserver(resultDataObserver)
    }
}