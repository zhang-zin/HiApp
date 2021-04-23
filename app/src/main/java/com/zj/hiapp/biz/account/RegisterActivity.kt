package com.zj.hiapp.biz.account

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.zj.common.ui.component.HiBaseActivity
import com.zj.hiapp.R
import com.zj.hiapp.databinding.ActivityRegisterBinding
import com.zj.hiapp.fragment.account.AccountManager

@Route(path = "/account/register")
class RegisterActivity : HiBaseActivity() {

    private lateinit var registerBinding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        initView()
    }

    private fun initView() {
        registerBinding.actionRegister.setOnClickListener {
            val userName = registerBinding.inputUserName.getEditText().text.toString()
            val userEmail = registerBinding.inputUserEmail.getEditText().text.toString()
            val userPwd = registerBinding.inputUserPwd.getEditText().text.toString()
            val userPwdAgain = registerBinding.inputUserPwdAgain.getEditText().text.toString()
            AccountManager.register(this, userName, userPwd, userEmail, userPwdAgain)
        }
    }

}