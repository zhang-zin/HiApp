package com.zj.hiapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zj.hi_library.hiLog.HiLog
import com.zj.hi_library.hiLog.HiLogManager
import com.zj.hi_library.hiLog.printer.HiViewPrinter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        HiLogManager.getInstance().addPrinter(HiViewPrinter(this))
        HiLog.e("MainActivity")
    }
}