package com.zj.hiapp.biz.account

import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.zj.common.ui.component.HiBaseActivity
import com.zj.common.util.Toast
import com.zj.hiapp.R
import com.zj.hiapp.databinding.ActivityLoginBinding
import com.zj.hiapp.fragment.account.AccountManager

@Route(path = "/account/login")
class LoginActivity : HiBaseActivity<ActivityLoginBinding>() {

    override fun getLayoutId() = R.layout.activity_login

    override fun init() {
        binding.actionLogin.setOnClickListener {
            login()
        }

        binding.titleBar.setBackClick {
            onBackPressed()
        }

        binding.titleBar.rightClick {
            ARouter.getInstance()
                .build("/account/register")
                .navigation(this, 0)
        }

        AccountManager.observeRegister(this, { userName ->
            binding.inputUserName.getEditText().setText(userName)
        })
    }

    private fun login() {
        val userName = binding.inputUserName.getEditText().text.toString()
        val userPwd = binding.inputUserPwd.getEditText().text.toString()

        if (userName.isEmpty() || userPwd.isEmpty()) {
            getString(R.string.user_name_or_user_pwd_is_empty_tips).Toast()
            return
        }

        AccountManager.login(this, userName, userPwd)
    }
}