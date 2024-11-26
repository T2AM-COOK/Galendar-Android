//package com.example.galendar
//
//import android.os.Bundle
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import android.Manifest
//import android.content.pm.PackageManager
//import android.os.Build
//import android.widget.Toast
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.databinding.DataBindingUtil
//import com.example.Galendar.databinding.ActivityMainBinding // 패키지명은 프로젝트에 맞게 수정
//
//
//class FCMActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityMainBinding
//    private val PERMISSION_REQUEST_CODE = 5000
//
//    private fun permissionCheck() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            val permissionCheck = ContextCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.POST_NOTIFICATIONS
//            )
//            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
//                    PERMISSION_REQUEST_CODE
//                )
//            }
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_fcm)
//
//        permissionCheck()
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            PERMISSION_REQUEST_CODE -> {
//                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(applicationContext, "Permission is denied", Toast.LENGTH_SHORT)
//                        .show()
//                } else {
//                    Toast.makeText(applicationContext, "Permission is granted", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            }
//        }
//    }
//}