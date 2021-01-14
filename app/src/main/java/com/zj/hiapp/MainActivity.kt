package com.zj.hiapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.zj.common.ui.common.HiBaseActivity
import com.zj.hiapp.demo.HiRefreshDemoActivity
import com.zj.hiapp.demo.LogActivity
import com.zj.hiapp.demo.TabBottomLayoutActivity
import com.zj.hiapp.demo.TabTopLayoutActivity
import com.zj.hiapp.logic.MainActivityLogic

class MainActivity : HiBaseActivity(), MainActivityLogic.ActivityProvider {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun click(view: View) {
        when (view.id) {
            R.id.tv_log -> {
                startActivity(Intent(this, LogActivity::class.java))
            }
            R.id.tv_tab_bottom -> {
                startActivity(Intent(this, TabBottomLayoutActivity::class.java))
            }
            R.id.tv_tab_top -> {
                startActivity(Intent(this, TabTopLayoutActivity::class.java))
            }
            R.id.tv_refresh -> startActivity(Intent(this, HiRefreshDemoActivity::class.java))

        }
    }

}