package com.zj.hiapp.biz.detail

import android.os.Handler
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.zj.common.ui.component.HiBaseActivity
import com.zj.hi_ui.ui.tab.top.HiTabTopInfo
import com.zj.hiapp.R
import com.zj.hiapp.databinding.ActivityCategoryDetailBinding
import com.zj.hiapp.http.model.ChildCategory
import com.zj.hiapp.router.HiRoute

@Route(path = "/detail/category")
class CategoryDetailActivity : HiBaseActivity<ActivityCategoryDetailBinding>() {

    @Autowired(name = "childrenList")
    lateinit var childrenList: List<ChildCategory>

    @Autowired
    lateinit var categoryTitle: String

    @JvmField
    @Autowired
    var selectPosition: Int = 0

    override fun getLayoutId() = R.layout.activity_category_detail
    override fun init() {
        HiRoute.inject(this)
    }

    override fun initData() {
        val infoList: MutableList<HiTabTopInfo<*>> = ArrayList()
        val defaultColor = resources.getColor(R.color.tabBottomDefaultColor)
        val tintColor = resources.getColor(R.color.tabBottomTintColor)
        for (childCategory in childrenList) {
            val info = HiTabTopInfo(childCategory.name, defaultColor, tintColor)
            infoList.add(info)
        }
        binding.tabTopLayout.inflateInfo(infoList)
        val select = if (selectPosition > infoList.size) infoList.size - 1 else selectPosition
        binding.tabTopLayout.defaultSelected(infoList[select])
        binding.titleBar.toolbarTitle(categoryTitle)
    }

    override fun initEvent() {
        binding.titleBar.setBackClick {
            onBackPressed()
        }
    }
}