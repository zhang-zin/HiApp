package com.zj.hiapp.fragment.account

import android.app.Activity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.zj.common.util.Toast
import com.zj.hi_library.restful.HiCallback
import com.zj.hi_library.restful.HiResponse
import com.zj.hiapp.R
import com.zj.hiapp.http.ApiFactory
import com.zj.hiapp.http.api.AccountApi
import com.zj.hiapp.http.model.UserModel

object AccountManager {

    private val userModel = MutableLiveData<UserModel>()
    private val registerLiveData = MutableLiveData<String>()

    fun login(activity: Activity, userName: String, userPwd: String) {
        ApiFactory.create(AccountApi::class.java).login(userName, userPwd)
            .enqueue(object : HiCallback<UserModel> {
                override fun onSuccess(response: HiResponse<UserModel>) {
                    if (response.code == HiResponse.SUCCESS) {
                        userModel.value = response.data
                        activity.finish()
                    } else {
                        activity.getString(R.string.login_fail).Toast()
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    activity.getString(R.string.login_fail).Toast()
                }
            })
    }

    fun register(
        activity: Activity,
        userName: String,
        userPwd: String,
        userEmail: String,
        userPwdAgain: String
    ) {
        if (userName.isEmpty() || userEmail.isEmpty() || userPwd.isEmpty() || userPwdAgain.isEmpty()) {
            activity.getString(R.string.check_input_error).Toast()
            return
        }

        if (userPwdAgain != userPwd) {
            activity.getString(R.string.again_input_pwd_errro).Toast()
            return
        }
        ApiFactory.create(AccountApi::class.java)
            .register(userName, userPwd, userPwdAgain)
            .enqueue(object : HiCallback<UserModel> {
                override fun onSuccess(response: HiResponse<UserModel>) {
                    registerLiveData.value = userName
                    activity.finish()
                }

                override fun onFailed(throwable: Throwable) {
                    activity.getString(R.string.action_register_fail).Toast()
                }

            })
    }

    fun observeLogin(owner: LifecycleOwner, observer: Observer<UserModel>) {
        userModel.observe(owner, observer)
    }

    fun observeRegister(owner: LifecycleOwner, observer: Observer<String>) {
        registerLiveData.observe(owner, observer)
    }

    fun getUserModel(): UserModel? {
        return userModel.value
    }

}