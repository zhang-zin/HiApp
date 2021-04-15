package com.zj.hi_debugtools

import android.app.Activity
import android.content.Intent
import android.os.Process
import com.zj.common.SP_KEY_DEGRADE_HTTP
import com.zj.common.util.SpUtil
import com.zj.hi_library.hiLog.HiLogManager
import com.zj.hi_library.hiLog.printer.HiViewPrinter
import com.zj.hi_library.hiLog.printer.HiViewPrinterProvider
import com.zj.hi_library.util.AppGlobals

class DebugTools {

    fun buildEnvironment(): String {
        return "构建环境：" + if (BuildConfig.DEBUG) "测试环境" else "正式环境"
    }

    fun buildVersion(): String {
        return "构建版本：" + BuildConfig.VERSION_NAME + "-" + BuildConfig.VERSION_CODE
    }

    fun buildTime(): String {
        return "构建时间：" + BuildConfig.BUILD_TIME
    }

    fun currentHttp(): String {
        return "请求网络：" + if (SpUtil.getBoolean(SP_KEY_DEGRADE_HTTP)) "http" else {
            "https"
        }
    }

    @HiDebug(name = "降级成HTTP", desc = "将继承Http,可以使用抓包工具明文抓包")
    fun degrade2ttp() {
        SpUtil.putBoolean(SP_KEY_DEGRADE_HTTP, true)

        AppGlobals.get()?.applicationContext?.run {
            //得到当前 启动页的Intent
            val launchIntent = packageManager.getLaunchIntentForPackage(packageName) ?: return
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            startActivity(launchIntent)
            //杀掉当前进程,并主动启动新的 启动页，以完成重启的动作
            Process.killProcess(Process.myPid())
        }
    }

    @HiDebug(name = "显示Log", parameterType = 1)
    fun showLog(activity: Activity) {
        AppGlobals.get()?.run {
            var bool = false
            HiLogManager.getInstance().printerList.forEach {
                if (it is HiViewPrinter) {
                    bool = true
                    return@forEach
                }
            }
            if (!bool) {
                val hiViewPrinter = HiViewPrinter(this)
                HiViewPrinterProvider.getInstance().addView(activity)
                HiLogManager.getInstance().addPrinter(hiViewPrinter)
            }
        }
    }

}