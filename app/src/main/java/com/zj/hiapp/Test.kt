package com.zj.hiapp

import android.content.res.AssetManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder

object Test {

    suspend fun request1(): String {
        val request2 = request2()
        println("after delay")
        return "result from request1$request2"
    }

    suspend fun request2(): String {
        delay(2 * 1000)//suspend fun
        println("after delay")
        return "result from request2"
    }

    /**
     * 让普通方法成为一个挂起函数
     * 异步方式读取assets目录下的文件，并且适配协程的写法，让他真正的挂起函数
     * 方便调用方 直接以同步的方式拿到返回值
     */
    suspend fun readFile(assetManager: AssetManager, fileName: String): String {
        return suspendCancellableCoroutine { continuation ->
            Thread {
                //io 操作
                val open = assetManager.open(fileName)
                val bufferedReader = BufferedReader(InputStreamReader(open))
                val stringBuilder = StringBuilder()
                var line: String?
                do {
                    line = bufferedReader.readLine()
                    if (line != null) {
                        stringBuilder.append(line)
                    } else {
                        break
                    }
                } while (true)
                open.close()
                bufferedReader.close()

                Thread.sleep(2000) //模拟耗时操作

                continuation.resumeWith(Result.success(stringBuilder.toString())) //返回真正的结果
            }.start()
        }
    }
}