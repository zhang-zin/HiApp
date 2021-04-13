package com.zj.hiapp.biz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.zj.common.util.Toast
import com.zj.hi_library.restful.HiCallback
import com.zj.hi_library.restful.HiResponse
import com.zj.hiapp.R
import com.zj.hiapp.databinding.ActivityLoginBinding
import com.zj.hiapp.http.ApiFactory
import com.zj.hiapp.http.api.AccountApi
import com.zj.hiapp.http.model.UserModel

@Route(path = "/account/login")
class LoginActivity : AppCompatActivity() {
    private val REQUEST_CODE_REGISTRATION = 1000
    private lateinit var loginBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        initView()

    }

    private fun initView() {
        loginBinding.actionLogin.setOnClickListener {
            login()
        }

        loginBinding.toolbarActionRight.setOnClickListener {
            ARouter.getInstance().build("/account/register")
                .navigation(this, REQUEST_CODE_REGISTRATION)
        }

        loginBinding.toolbarActionBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun login() {
        val userName = loginBinding.inputUserName.getEditText().text.toString()
        val userPwd = loginBinding.inputUserPwd.getEditText().text.toString()

        if (userName.isEmpty() || userPwd.isEmpty()) {
            getString(R.string.user_name_or_user_pwd_is_empty_tips).Toast()
            return
        }

        ApiFactory.create(AccountApi::class.java).login(userName, userPwd)
            .enqueue(object : HiCallback<UserModel> {
                override fun onSuccess(response: HiResponse<UserModel>) {
                    if (response.code == HiResponse.SUCCESS) {
                        "登录成功".Toast()
                    } else {
                        "登录失败".Toast()
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    "登录失败".Toast()
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            loginBinding.inputUserName.getEditText()
                .setText(data.getStringExtra("register_success_user_name") ?: "")
        }
    }
}