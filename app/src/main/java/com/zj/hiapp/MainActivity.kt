package com.zj.hiapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.zj.common.ui.common.HiBaseActivity
import com.zj.hiapp.demo.*
import com.zj.hiapp.logic.MainActivityLogic
import com.zj.hiapp.test.CoroutinesTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.Continuation

class MainActivity : HiBaseActivity(), MainActivityLogic.ActivityProvider {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GlobalScope.launch {
            val readFile = Test.readFile(assets, "")
        }
    }


    fun click(view: View) {
        when (view.id) {
            R.id.tv_log -> {
                // ARouter.getInstance().build("/b/xx").navigation()
                //   startActivity(Intent(this, LogActivity::class.java))
                request()
            }
            R.id.tv_tab_bottom -> {
                startActivity(Intent(this, TabBottomLayoutActivity::class.java))
            }
            R.id.tv_tab_top -> {
                startActivity(Intent(this, TabTopLayoutActivity::class.java))
            }
            R.id.tv_refresh -> startActivity(Intent(this, HiRefreshDemoActivity::class.java))
            R.id.tv_banner -> startActivity(Intent(this, HiBannerDemoActivity::class.java))

        }
    }

    private fun request() {
        val callBack = Continuation<String>(Dispatchers.Main) {
            Log.e("zhang 结果", it.getOrNull())
        }
        CoroutinesTest.request1(callBack)
    }

}