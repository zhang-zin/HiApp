package com.zj.hiapp

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.DialogFragment
import com.zj.common.ui.component.HiBaseActivity
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