package com.zj.hi_library.restful

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun <T : Any> HiCall<T>.serverData(): HiResponse<T> {
    return suspendCancellableCoroutine { continuation ->
        this.enqueue(object : HiCallback<T> {
            override fun onSuccess(response: HiResponse<T>) {
                continuation.resume(response)
            }

            override fun onFailed(throwable: Throwable) {
                continuation.resumeWithException(throwable)
            }
        })
    }

}

fun <T : Any> HiCall<T>.toLiveData() {
    val result = MutableLiveData<T>()
    this.enqueue(object : HiCallback<T> {
        override fun onSuccess(response: HiResponse<T>) {
            result.postValue(response.data)
        }

        override fun onFailed(throwable: Throwable) {
            result.postValue(null)
        }

    })
}