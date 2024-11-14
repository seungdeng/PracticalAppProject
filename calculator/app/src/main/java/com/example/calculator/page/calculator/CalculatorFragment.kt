package com.example.calculator.page.calculator

import com.example.calculator.databinding.FragmentCalculatorBinding
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.calculator.MainActivity
import com.example.calculator.R
import com.example.calculator.record.RecordAdapter
import java.text.NumberFormat
import java.util.Locale

//import com.example.calculator.R

class CalculatorFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private val tag:String? = this::class.simpleName
    private var binding: FragmentCalculatorBinding? = null

    private var v1: String=""
    private var v2: String=""
    private var symbol: String=""

    private val cal = {
            no1:Double, no2:Double,symbol:String->
        when(symbol){
            "+"->{
                Log.i(tag?:"cal","${no1} + ${no2}")
                no1+no2
            }
            "-"->{
                Log.i(tag?:"cal","${no1} - ${no2}")
                no1-no2
            }
            "*"->{
                Log.i(tag?:"cal","${no1} * ${no2}")
                no1*no2
            }
            "/"->{
                Log.i(tag?:"cal","${no1}/ ${no2}")
                no1/no2
            }
            else ->{
                Log.i(tag?:"cal","else pass")
                0.0
            }
        }
    }

    private lateinit var  parent: MainActivity

//    override fun onCreateView()
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?): View?{
//            binding = FragmentCalculatorBinding.inflate(inflater,container, attachToParent:false)
//        return binding?.root
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalculatorBinding.inflate(inflater,container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parent= requireActivity() as MainActivity
        parent.supportActionBar?.setTitle(R.string.nav_calculator_label)

        binding?.btnClean?.setOnClickListener{inputEventProc("C")}
        binding?.btnDel?.setOnClickListener{inputEventProc("D")}
        binding?.btnNum0?.setOnClickListener{inputEventProc("0")}
        binding?.btnNum1?.setOnClickListener{inputEventProc("1")}
        binding?.btnNum2?.setOnClickListener{inputEventProc("2")}
        binding?.btnNum3?.setOnClickListener{inputEventProc("3")}
        binding?.btnNum4?.setOnClickListener{inputEventProc("4")}
        binding?.btnNum5?.setOnClickListener{inputEventProc("5")}
        binding?.btnNum6?.setOnClickListener{inputEventProc("6")}
        binding?.btnNum7?.setOnClickListener{inputEventProc("7")}
        binding?.btnNum8?.setOnClickListener{inputEventProc("8")}
        binding?.btnNum9?.setOnClickListener{inputEventProc("9")}
        binding?.btnComma?.setOnClickListener{inputEventProc(".")}
        binding?.btnAddition?.setOnClickListener{inputEventProc("+")}
        binding?.btnSubtraction?.setOnClickListener{inputEventProc("-")}
        binding?.btnMultiplication?.setOnClickListener{inputEventProc("*")}
        binding?.btnDivision?.setOnClickListener{inputEventProc("/")}
        binding?.btnEqual?.setOnClickListener{inputEventProc("=")}

        val sharedData = ViewModelProvider(parent).get(CalculatorViewModel::class.java)
        v1= sharedData.val1
        binding?.txtStep?.text = "$v1$symbol$v2"

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding=null
    }
    private fun inputEventProc(key: String) {
        var printResult = ""
        var printStep = ""


        when(key) {
            "C" -> {
                v1 = ""
                v2 = ""
                symbol = ""
            }
            "D" -> {
                if(symbol == "")
                {
                    if(v1 != "")
                    {
                        v1 = v1.substring(0, v1.length - 1)
                    }
                }else {
                    if(v2 != "")
                    {
                        v2 = v2.substring(0, v2.length - 1)
                    }
                }
                printStep = "$v1$symbol$v2"
            }
            "0" -> {
                if(v1 == "") {
                    v1 += key
                }else if(v1 != "0" && symbol == "")
                {
                    if (v1.length < 10) {
                        v1 += key
                    }else {
                        Toast.makeText(requireContext(), getString(R.string.txt_length_over_msg), Toast.LENGTH_SHORT).show()
                    }
                }else if(symbol != "")
                {
                    if(v2 == "")
                    {
                        v2 += key
                    }else if(v2 != "0")
                    {
                        if (v2.length < 10) {
                            v2 += key
                        }else {
                            Toast.makeText(requireContext(), getString(R.string.txt_length_over_msg), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                printStep = "$v1$symbol$v2"
            }
            "." -> {
                if(symbol == "")
                {
                    if (v1 == "") {
                        v1 = "0."
                    } else if(!v1.contains(".")) {
                        v1 += key
                    }
                }else {
                    if (v2 == "") {
                        v2 = "0."
                    } else if(!v2.contains(".")) {
                        v2 += key
                    }
                }
                printStep = "$v1$symbol$v2"
            }
            "=" -> {
                if(v1 != "" && v2 != "" && symbol != "")
                {
                    v1 = if (v1 == "0.") "0.0" else v1
                    v2 = if (v2 == "0.") "0.0" else v2

                    val result = cal(v1.toDouble(), v2.toDouble(), symbol)

                    printResult = if (result % 1 != 0.0) {
                        NumberFormat.getInstance(Locale.KOREA).format(result)
                    } else {
                        NumberFormat.getInstance(Locale.KOREA).format(result.toInt())
                    }

                    parent.RECORD_LIST.add(RecordAdapter.RecordData(v1,v2,symbol,printResult))

                    v1 = ""
                    v2 = ""
                    symbol = ""
                    printStep = "$v1$symbol$v2"
                }else {
                    printStep = "$v1$symbol$v2"
                }
            }
            "+", "-", "*", "/" -> {
                if(v2 == "" && v1 != "")
                {
                    v1 = if (v1 == "0.") "0.0" else v1
                    symbol = key

                    printStep = "$v1$symbol$v2"
                }else {
                    if (v1 != "") {
                        v1 = if (v1 == "0.") "0.0" else v1
                        v2 = if (v2 == "0.") "0.0" else v2

                        val result = cal(v1.toDouble(), v2.toDouble(), symbol)
                        val resultStr = if(result % 1 != 0.0) {
                            printResult = NumberFormat.getInstance(Locale.KOREA).format(result)
                            result.toString()
                        }else {
                            printResult = NumberFormat.getInstance(Locale.KOREA).format(result.toInt())
                            result.toInt().toString()
                        }

                        printStep = "$v1$symbol$v2"
                        parent.RECORD_LIST.add(RecordAdapter.RecordData(v1,v2,symbol,printResult))
                        v1 = resultStr
                        v2 = ""
                        symbol = key
                    }
                }
            }
            else -> {
                if(symbol == "")
                {
                    if (v1 == "0") {
                        v1 = key
                    } else if (v1.length < 10) {
                        v1 += key
                    }else {
                        Toast.makeText(requireContext(), getString(R.string.txt_length_over_msg), Toast.LENGTH_SHORT).show()
                    }
                }else {
                    if (v2 == "0") {
                        v2 = key
                    }else if (v2.length < 10) {
                        v2 += key
                    }else {
                        Toast.makeText(requireContext(), getString(R.string.txt_length_over_msg), Toast.LENGTH_SHORT).show()
                    }
                }
                printStep = "$v1$symbol$v2"
            }
        }
        binding?.txtStep?.text = printStep
        binding?.txtResult?.text = printResult
    }
}