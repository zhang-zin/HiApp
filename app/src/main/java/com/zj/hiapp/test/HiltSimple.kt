package com.zj.hiapp.test

import com.zj.hi_library.hiLog.HiLog
import javax.inject.Inject

class HiltSimple @Inject constructor() {

    fun doSomething(){
        HiLog.e("-----doSomething-----")
    }
}