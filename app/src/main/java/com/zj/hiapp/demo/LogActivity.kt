package com.zj.hiapp.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zj.hi_library.hiLog.HiLog
import com.zj.hiapp.R

class LogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)

        HiLog.e("LogActivity")
    }
}