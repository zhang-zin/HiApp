package com.zj.hiapp.http

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zj.hi_library.restful.HiConvert
import com.zj.hi_library.restful.HiResponse
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type

class GsonConvert : HiConvert {

    private var gson = Gson()

    /* wanAndroid Api
    errorCode = 0 代表执行成功，不建议依赖任何非0的 errorCode.
    errorCode = -1001 代表登录失效，需要重新登录。
    {
        "data": ...,
        "errorCode": 0,
        "errorMsg": ""
    }
    */
    override fun <T> convert(rawData: String, dataType: Type): HiResponse<T> {
        val response: HiResponse<T> = HiResponse()
        try {
            val jsonObject = JSONObject(rawData)
            response.code = jsonObject.optInt("errorCode")
            response.msg = jsonObject.optString("errorMsg")
            val data = jsonObject.opt("data")
            if ((data is JSONObject) or (data is JSONArray)) {
                if (response.code == HiResponse.SUCCESS) {
                    if (dataType.typeName == "java.lang.String"){
                        response.data = data.toString() as T?
                    }else{
                        response.data = gson.fromJson(data.toString(), dataType)
                    }
                } else {
                    response.errorData = gson.fromJson<MutableMap<String, String>>(
                        data.toString(),
                        object : TypeToken<MutableMap<String, String>>() {}.type
                    )
                }
            } else {
                response.data = data as T?
            }
            if (data==null){
                response.data = gson.fromJson(rawData, dataType)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            response.code = -1
            response.msg = e.message
        }
        response.rawData = rawData
        return response
    }

}