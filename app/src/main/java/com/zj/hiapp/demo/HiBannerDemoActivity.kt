package com.zj.hiapp.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.zj.hi_ui.ui.banner.HiBanner
import com.zj.hi_ui.ui.banner.core.HiBannerMo
import com.zj.hi_ui.ui.banner.indicator.HiCircleIndicator
import com.zj.hi_ui.ui.banner.indicator.HiNumIndicator
import com.zj.hiapp.R
import java.util.ArrayList

class HiBannerDemoActivity : AppCompatActivity() {

    private var urls = arrayOf(
        "https://www.devio.org/img/beauty_camera/beauty_camera1.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera3.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera4.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera5.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera2.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera6.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera7.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera8.jpeg"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hi_banner_demo)

        val imageView = findViewById<ImageView>(R.id.iv_image)
        Glide.with(this@HiBannerDemoActivity).load(urls[0]).into(imageView)

        initView()
    }

    private fun initView() {
        val mHiBanner = findViewById<HiBanner>(R.id.hi_banner)
        val hiNumIndicator = HiNumIndicator(this)
        val moList: MutableList<HiBannerMo> = ArrayList()
        for (i in 0..2) {
            val bannerMo = BannerMo()
            bannerMo.url = urls[i]
            moList.add(bannerMo)
        }

        mHiBanner.setHiIndicator(hiNumIndicator)
        mHiBanner.setIntervalTime(3000)
        mHiBanner.setScrollDuration(2000)
        mHiBanner.setAutoPlay(false)
        mHiBanner.setLoop(true)
        mHiBanner.setBannerData(moList)
        mHiBanner.setBindAdapter { viewHolder, mo, position ->
            val imageView: ImageView = viewHolder.findViewById(R.id.iv_banner)
            Glide.with(this@HiBannerDemoActivity).load(mo.url).into(imageView)
        }

    }
}

class BannerMo : HiBannerMo() {}