package com.example.calculator

import com.example.calculator.databinding.ActivityMainBinding
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import java.text.NumberFormat
import java.util.Locale
import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.calculator.record.RecordAdapter

class MainActivity : AppCompatActivity() {

    private val TAG : String? = this::class.simpleName

    private var backPressedTime: Long=0
    private val BACK_PRESSED_DURATION = 2000L

    private lateinit var binding:ActivityMainBinding
    val RECORD_LIST = mutableListOf<RecordAdapter.RecordData>()


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main){v,insets->
            var systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left,systemBars.top,systemBars.right,0)
            insets
        }

        onBackPressedDispatcher.addCallback(this,object :OnBackPressedCallback(true){
            override fun handleOnBackPressed(){
                if(System.currentTimeMillis()-backPressedTime >=BACK_PRESSED_DURATION){
                    backPressedTime = System.currentTimeMillis()
                    Toast.makeText(this@MainActivity,getString(R.string.backpress_event_toast_msg),Toast.LENGTH_SHORT).show()
                }
                else{
                    finishAffinity()
                }
            }
        })

        setSupportActionBar(binding.toolbar)

        val navController = supportFragmentManager.findFragmentById(binding.containerMain.id)?.findNavController()
        navController?.let{
            binding.bottomNavigationView.setupWithNavController(it)
            binding.bottomNavigationView.setOnItemSelectedListener { item->
                val current = it.currentDestination
                when(item.itemId){
                    R.id.nav_calculator ->{
                        if(current?.id!=item.itemId){
                            it.navigate(R.id.action_nav_record_to_nav_calculator)
                            true
                        }
                        else{
                            false
                        }
                    }
                    R.id.nav_record->{
                        if( current?.id!=item.itemId){
                            it.navigate(R.id.action_nav_calculator_to_nav_record)
                            true
                        }
                        else{
                            false
                        }
                    }
                    else->false
                }

            }
        }



        }

}



//    private var input = ""
//    private var firstNumber = ""
//    private var operator = ""
//    private var isOperatorClicked = false
//
//    private fun inputEventProc(key: String) {
//        when (key) {
//            "C" -> {  // Clear 입력
//                input = ""
//                firstNumber = ""
//                operator = ""
//                binding.txtStep.text = ""
//                binding.txtResult.text = ""
//            }
//            "D" -> {  // 한 글자씩 삭제
//                if (input.isNotEmpty()) {
//                    input = input.dropLast(1)
//                    binding.txtStep.text = input
//                }
//            }
//            "+", "-", "×", "÷" -> {   //람다식 쓰기전 연산자 인식가능하게 변환
//                if (input.isNotEmpty()) {
//                    firstNumber = input
//                    operator =  when(key){
//                        "×" -> "*"
//                        "÷" -> "/"
//                        else -> key
//                    }
//                    isOperatorClicked = true
//                    binding.txtStep.text = "$firstNumber $operator"
//                    input = ""
//                }
//            }
//            "=" -> {  // 계산 실행
//                if (firstNumber.isNotEmpty() && operator.isNotEmpty() && input.isNotEmpty()) {
//                    val result = cal(firstNumber.toDouble(), input.toDouble(), operator)
//                    binding.txtStep.text = "$firstNumber $operator $input ="
//                    binding.txtResult.text = result.toString()
//                    input = result.toString()  // 결과를 input에 저장하여 계속 연산 가능
//                    firstNumber = ""
//                    operator = ""
//                    isOperatorClicked = false
//                }
//            }
//            "."-> {
//                if(input.isEmpty()){
//                    input = "0."
//                }
//                else if(!input.contains(".")){
//                    input +=key
//                }
//                binding.txtStep.text=input
//            }
//
//            else -> {  // 숫자나 소수점 입력
//                input += key
//                binding.txtStep.text = input
//            }
//        }
//    }
//
//}




