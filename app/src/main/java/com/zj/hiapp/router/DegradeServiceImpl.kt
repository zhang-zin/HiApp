package com.zj.hiapp.router

import android.content.Context
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.DegradeService
import com.alibaba.android.arouter.launcher.ARouter

/**
 * 全局降级服务，当路由目标页不存在的时候，此时重定向到统一错误页
 */
@Route(path = "/degrade/global/service")
class DegradeServiceImpl : DegradeService {
    override fun init(context: Context?) {
    }

    override fun onLost(context: Context?, postcard: Postcard?) {
        ARouter.getInstance()
            .build("/degrade/global/activity")
            .greenChannel()
            .navigation()
    }
}