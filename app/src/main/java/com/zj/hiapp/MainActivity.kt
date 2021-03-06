package com.zj.hiapp

import android.os.Bundle
import android.view.KeyEvent
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.zj.common.ui.component.HiBaseActivity
import com.zj.common.util.Toast
import com.zj.hiapp.logic.MainActivityLogic

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity  : HiBaseActivity<ViewDataBinding>(), MainActivityLogic.ActivityProvider {

    lateinit var mainActivityLogic: MainActivityLogic

    override fun getLayoutId() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityLogic = MainActivityLogic(this, savedInstanceState)

        "hi-tinker".Toast()
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