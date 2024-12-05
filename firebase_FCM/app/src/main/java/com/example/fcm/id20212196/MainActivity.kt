package com.example.fcm.id20212196

import android.app.AlertDialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)){v,insets->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left,systemBars.top,systemBars.right,systemBars.bottom)
            insets
        }

        handleIntentData(intent)

        createNotificationChannel()
        checkNotificationPermission()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntentData(intent)
    }

    private fun createNotificationChannel(){
        val channelId = getString(R.string.notification_channel_id)

        val channel = NotificationChannel(
            channelId,
            getString(R.string.notification_channel_title),
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = getString(R.string.notification_channel_description)
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private  val requestNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){isGranted->
        if (isGranted){
            Toast.makeText(this,"알람 수신 권한에 동의하였습니다.",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this,"알람 수신 권한에 동의해주시기 바랍니다.",Toast.LENGTH_SHORT).show()
        }
    }

    private fun showPermissionDeniedDialog(){
        AlertDialog.Builder(this)
            .setTitle("알림 권한 설정")
            .setMessage("앱 설정에서 알림 권한을 허용해야 서비스를 이용하실 수 있습니다.")
            .setPositiveButton("설정으로 이동"){ _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply{
                    data = Uri.fromParts("package",packageName,null)
                }
                startActivity(intent)
            }
            .setNegativeButton("취소"){dialog, _ -> dialog.dismiss()}
            .show()
    }

    private fun isAlreadyGranted(permission: String) =
        ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED

    private fun isRejected(permission: String) =
        ActivityCompat.shouldShowRequestPermissionRationale(this,permission)

    private fun checkNotificationPermission(){
        when{
            isAlreadyGranted(android.Manifest.permission.POST_NOTIFICATIONS) -> {}
            isRejected(android.Manifest.permission.POST_NOTIFICATIONS) -> showPermissionDeniedDialog()

            else ->{
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    requestNotificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    private fun handleIntentData(intent: Intent){
        val page = intent.getStringExtra("page")
        if (page != null)
        {
            var pIntent:Intent? = null
            when(page.toInt())
            {
                2 ->{
                    pIntent = Intent(this,Page2Activity::class.java)
                }
                3 ->{
                    pIntent = Intent(this,Page3Activity::class.java)
                }
            }
            if(pIntent != null){
                lifecycleScope.launch {
                    delay(1000)
                    startActivity(pIntent)
                }
            }

        }
    }

}