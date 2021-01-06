package com.zj.hiapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.zj.hi_library.hiLog.HiLog
import com.zj.hi_library.hiLog.HiLogManager
import com.zj.hi_library.hiLog.printer.HiViewPrinter
import com.zj.hiapp.demo.DemoActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        HiLog.e("MainActivity")
        HiLog.e(1,11)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun toDemo(view: View) {
//        HiLog.v("v日志")
//        HiLog.d("d日志")
//        HiLog.i("i日志")
//        HiLog.w("w日志")
//        HiLog.e("e日志")
//        HiLog.a("a日志")
        startActivity(Intent(this, DemoActivity::class.java))
    }
}