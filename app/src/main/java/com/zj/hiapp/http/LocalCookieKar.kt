package com.zj.hiapp.http

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zj.hi_library.hiLog.HiLog
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

internal class LocalCookieKar : CookieJar {

    /**
     * 本地cookies持久化存储
     */
    private val cookies = mutableListOf<Cookie>()

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        // 过期cookies
        val invalidCookies = mutableListOf<Cookie>()
        // 有效cookies
        val validCookies = mutableListOf<Cookie>()
        for (cookie in cookies) {
            if (cookie.expiresAt() < System.currentTimeMillis()) {
                // 判断是否有效
                invalidCookies.add(cookie)
            } else if (cookie.matches(url)) {
                // 匹配对应的url
                validCookies.add(cookie)
            }
        }
        //移除无效的cookie
        cookies.removeAll(invalidCookies)
        return validCookies
    }

    /**
     * 保存Cookies
     */
    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        this.cookies.addAll(cookies)
        /*response.errorData = gson.fromJson<MutableMap<String, String>>(
            data.toString(),
            object : TypeToken<MutableMap<String, String>>() {}.type
        )*/
        val gson = Gson()
        val toJson = gson.toJson(cookies)
        HiLog.e(toJson)
    }
}