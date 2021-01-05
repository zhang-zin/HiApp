package com.zj.hiapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.zj.hi_library.hiLog.HiLog
import com.zj.hi_library.hiLog.HiLogManager
import com.zj.hi_library.hiLog.printer.HiViewPrinter
import com.zj.hiapp.demo.DemoActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        HiLog.e("MainActivity")
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun toDemo(view: View) {
        startActivity(Intent(this, DemoActivity::class.java))
    }
}