package com.zj.hiapp.biz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.zj.hiapp.R
import com.zj.hiapp.router.RouterFlag

@Route(path = "/profile/authentication", extras = RouterFlag.FLAG_AUTHENTICATION)
class AuthenticationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
    }
}