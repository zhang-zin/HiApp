package com.zj.hiapp.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.zj.hi_ui.ui.refresh.HiLottieOverView
import com.zj.hi_ui.ui.refresh.HiRefresh
import com.zj.hi_ui.ui.refresh.HiRefreshLayout
import com.zj.hi_ui.ui.refresh.HiTextOverView
import com.zj.hiapp.R

class HiRefreshDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hi_refresh_demo)

        val refresh = findViewById<HiRefreshLayout>(R.id.hi_refresh)

        refresh.setRefreshOverview(HiLottieOverView(this))
        refresh.setRefreshListener(object : HiRefresh.HiRefreshListener {
            override fun onRefresh() {
                Handler().postDelayed({
                    refresh.refreshFinished()
                }, 1000)
            }

            override fun enableRefresh() = true
        })
    }
}