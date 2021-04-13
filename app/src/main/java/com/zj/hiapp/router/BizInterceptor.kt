package com.zj.hiapp.router

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import com.zj.common.util.Toast
import com.zj.hiapp.R
import java.lang.RuntimeException

@Interceptor(name = "biz_interceptor", priority = 9)
class BizInterceptor : IInterceptor {

    private var context: Context? = null

    override fun init(context: Context?) {
        this.context = context
    }

    override fun process(postcard: Postcard?, callback: InterceptorCallback?) {
        val flag = postcard!!.extra

        when {
            flag and (RouterFlag.FLAG_LOGIN) != 0 -> {
                callback?.onInterrupt(RuntimeException("need login"))
                loginIntercept()
            }
           /* flag and (RouterFlag.FLAG_AUTHENTICATION) != 0 -> {
                callback?.onInterrupt(RuntimeException("need authentication"))
                showToast("请先实名认证")
            }
            flag and (RouterFlag.FLAG_VIP) != 0 -> {
                callback?.onInterrupt(RuntimeException("need become vip"))
                showToast("请先加入会员")
            }*/
            else -> {
                callback?.onContinue(postcard)
            }
        }
    }

    private fun loginIntercept() {
        Handler(Looper.getMainLooper()).post {
            context.getString(R.string.need_login_tips)?.Toast()
            HiRoute.startActivity(null, destination = HiRoute.Destination.ACCOUNT_LOGIN)
        }
    }
}