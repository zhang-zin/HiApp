package com.zj.hiapp.biz.account

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.zj.common.ui.component.HiBaseActivity
import com.zj.common.util.Toast
import com.zj.hiapp.R
import com.zj.hiapp.databinding.ActivityLoginBinding
import com.zj.hiapp.fragment.account.AccountManager

@Route(path = "/account/login")
class LoginActivity : HiBaseActivity() {
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

        AccountManager.observeRegister(this, { userName ->
            loginBinding.inputUserName.getEditText().setText(userName)
        })
    }

    private fun login() {
        val userName = loginBinding.inputUserName.getEditText().text.toString()
        val userPwd = loginBinding.inputUserPwd.getEditText().text.toString()

        if (userName.isEmpty() || userPwd.isEmpty()) {
            getString(R.string.user_name_or_user_pwd_is_empty_tips).Toast()
            return
        }

        AccountManager.login(this, userName, userPwd)
    }
}