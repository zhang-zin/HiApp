package com.zj.hiapp.biz.account

import com.alibaba.android.arouter.facade.annotation.Route
import com.zj.common.ui.component.HiBaseActivity
import com.zj.hiapp.R
import com.zj.hiapp.databinding.ActivityRegisterBinding
import com.zj.hiapp.fragment.account.AccountManager

@Route(path = "/account/register")
class RegisterActivity : HiBaseActivity<ActivityRegisterBinding>() {

    override fun getLayoutId() = R.layout.activity_login

     override fun initEvent() {
        binding.actionRegister.setOnClickListener {
            val userName = binding.inputUserName.getEditText().text.toString()
            val userEmail = binding.inputUserEmail.getEditText().text.toString()
            val userPwd = binding.inputUserPwd.getEditText().text.toString()
            val userPwdAgain = binding.inputUserPwdAgain.getEditText().text.toString()
            AccountManager.register(this, userName, userPwd, userEmail, userPwdAgain)
        }

         binding.titleBar.setBackClick {
             onBackPressed()
         }
    }

}