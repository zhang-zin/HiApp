package com.zj.hiapp

import android.os.Bundle
import com.zj.common.ui.common.HiBaseActivity
import com.zj.hiapp.logic.MainActivityLogic

class MainActivity : HiBaseActivity(), MainActivityLogic.ActivityProvider {

    lateinit var mainActivityLogic: MainActivityLogic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainActivityLogic = MainActivityLogic(this, savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mainActivityLogic.onSaveInstanceState(outState)
    }

}