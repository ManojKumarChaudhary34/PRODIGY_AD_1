package com.example.simplecalculator

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.simplecalculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var expression: Expression
    var lastNumeric= false
    var stateError= false
    var lastDot= false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun on_equalTo_click(view: View) {
        onCalculate()
        binding.dataTv.text= binding.resultTv.text.toString().drop(1)
        binding.resultTv.text= ""
    }
    fun on_number_click(view: View) {
        if (stateError){
            binding.dataTv.text= (view as Button).text
            stateError= false
        }else{
            binding.dataTv.append((view as Button).text)
        }
        lastNumeric= true
        onCalculate()
    }
    fun on_operator_click(view: View) {
        if (!stateError && lastNumeric){
            binding.dataTv.append((view as Button).text)
            lastDot= false
            lastNumeric= false
            onCalculate()
        }
    }
    fun on_back_click(view: View) {
        binding.dataTv.text= binding.dataTv.text.toString().dropLast(1)
        try {
            val lasChar= binding.dataTv.text.toString().last()
            if (lasChar.isDigit())
                onCalculate()
        }catch (exception: Exception){
            binding.resultTv.text= ""
            binding.resultTv.visibility= View.GONE
            Log.e("last char error", exception.toString())
        }
    }
    fun on_allClear_click(view: View) {
        binding.dataTv.text=""
        binding.resultTv.text= ""
        stateError= false
        lastNumeric= false
        lastDot= false
        binding.resultTv.visibility= View.GONE
    }
    fun on_clear_click(view: View) {
        binding.dataTv.text=""
        lastNumeric= false
    }

    @SuppressLint("SetTextI18n")
    fun  onCalculate(){
        if (lastNumeric && !stateError){
            val text= binding.dataTv.text.toString()
            expression= ExpressionBuilder(text).build()
            try {
                val result= expression.evaluate()
                binding.resultTv.visibility= View.VISIBLE
                binding.resultTv.text= "=$result"
            }catch (exception:ArithmeticException){
                Log.e("evaluate error", exception.toString())
                binding.resultTv.text= "Error"
                stateError= true
                lastNumeric= false
            }

        }
    }

}