package com.zj.hiapp

import android.os.Bundle
import android.view.KeyEvent
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.zj.common.ui.component.HiBaseActivity
import com.zj.hiapp.http.api.AccountApi
import com.zj.hiapp.logic.MainActivityLogic
import com.zj.hiapp.test.HiltSimple
import com.zj.hiapp.test.module.AnalyticsService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : HiBaseActivity(), MainActivityLogic.ActivityProvider {

    lateinit var mainActivityLogic: MainActivityLogic

    override fun getLayoutId() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityLogic = MainActivityLogic(this, savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mainActivityLogic.onSaveInstanceState(outState)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN && BuildConfig.DEBUG) {
            // 点击音量下键
            kotlin.runCatching {
                val debugToolDialogClass = Class.forName("com.zj.hi_debugtools.DebugToolDialog")
                val debugToolDialog: DialogFragment =
                    debugToolDialogClass.getConstructor().newInstance() as DialogFragment
                debugToolDialog.show(supportFragmentManager, "debug_tool")
            }
        }
        return super.onKeyDown(keyCode, event)
    }

}