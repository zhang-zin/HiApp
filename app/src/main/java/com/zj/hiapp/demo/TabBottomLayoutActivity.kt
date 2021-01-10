package com.zj.hiapp.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zj.hiapp.R
import com.zj.hiapp.logic.MainActivityLogic

class TabBottomLayoutActivity : AppCompatActivity(), MainActivityLogic.ActivityProvider {

    lateinit var mainActivityLogic: MainActivityLogic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_bottom_layout)
        mainActivityLogic = MainActivityLogic(this, savedInstanceState)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mainActivityLogic.onSaveInstanceState(outState)
    }

}