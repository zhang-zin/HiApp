package com.zj.hiapp.biz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.zj.common.util.Toast
import com.zj.hi_library.restful.HiCallback
import com.zj.hi_library.restful.HiResponse
import com.zj.hiapp.R
import com.zj.hiapp.databinding.ActivityRegisterBinding
import com.zj.hiapp.http.ApiFactory
import com.zj.hiapp.http.api.AccountApi
import com.zj.hiapp.http.model.UserModel

@Route(path = "/account/register")
class RegisterActivity : AppCompatActivity() {

    private lateinit var registerBinding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        initView()
    }

    private fun initView() {
        registerBinding.actionRegister.setOnClickListener {
            register()
        }
    }

    private fun register() {
        val userName = registerBinding.inputUserName.getEditText().text.toString()
        val userEmail = registerBinding.inputUserEmail.getEditText().text.toString()
        val userPwd = registerBinding.inputUserPwd.getEditText().text.toString()
        val userPwdAgain = registerBinding.inputUserPwdAgain.getEditText().text.toString()
        if (userName.isEmpty() || userEmail.isEmpty() || userPwd.isEmpty() || userPwdAgain.isEmpty()) {
            getString(R.string.check_input_error).Toast()
            return
        }

        if (userPwdAgain != userPwd) {
            getString(R.string.again_input_pwd_errro).Toast()
            return
        }

        ApiFactory.create(AccountApi::class.java)
            .register(userName, userPwd, userPwdAgain)
            .enqueue(object : HiCallback<UserModel> {
                override fun onSuccess(response: HiResponse<UserModel>) {
                    setResult(RESULT_OK, Intent().putExtra("register_success_user_name", userName))
                    finish()
                }

                override fun onFailed(throwable: Throwable) {
                    getString(R.string.action_register_fail).Toast()
                }

            })
    }
}