package com.zj.hiapp.demo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zj.hi_ui.ui.tab.top.HiTabTopInfo
import com.zj.hi_ui.ui.tab.top.HiTabTopLayout
import com.zj.hiapp.R
import java.util.*

class TabTopLayoutActivity : AppCompatActivity() {

    var tabsStr = arrayOf(
        "热门",
        "服装",
        "数码",
        "鞋子",
        "零食",
        "家电",
        "汽车",
        "百货",
        "家居",
        "装修",
        "运动"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_top_bottom_layout)
        initTabTop()
    }

    private fun initTabTop() {
        val hiTabTopLayout: HiTabTopLayout = findViewById(R.id.tab_top_layout)
        val infoList: MutableList<HiTabTopInfo<*>> = ArrayList<HiTabTopInfo<*>>()
        val defaultColor = resources.getColor(R.color.tabBottomDefaultColor)
        val tintColor = resources.getColor(R.color.tabBottomTintColor)
        for (s in tabsStr) {
            val info: HiTabTopInfo<*> = HiTabTopInfo<Int>(s, defaultColor, tintColor)
            infoList.add(info)
        }
        hiTabTopLayout.inflateInfo(infoList)
        hiTabTopLayout.addTabSelectedChangeListener { _, _, nextInfo ->
            Toast.makeText(this, nextInfo.name, Toast.LENGTH_SHORT).show()
        }
        hiTabTopLayout.defaultSelected(infoList[0])
    }
}