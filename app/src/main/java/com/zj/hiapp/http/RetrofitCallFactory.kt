package com.zj.hiapp.http

import com.zj.hi_library.restful.*
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.*
import java.lang.IllegalStateException

class RetrofitCallFactory(baseUrl: String) : HiCall.Factory {

    private var gsonConvert: GsonConvert
    private var apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .build()
        apiService = retrofit.create(ApiService::class.java)
        gsonConvert = GsonConvert()
    }

    override fun newCall(request: HiRequest): HiCall<Any> {
        return RetrofitCall(request)
    }

    internal inner class RetrofitCall<T>(private val request: HiRequest) : HiCall<T> {
        override fun execute(): HiResponse<T> {
            val realCall: Call<ResponseBody> = createRealCall(request)
            val response: Response<ResponseBody> = realCall.execute()
            return parseResponse(response)
        }

        override fun enqueue(callback: HiCallback<T>) {
            val readCall = createRealCall(request)
            readCall.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    callback.onSuccess(parseResponse(response))
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    callback.onFailed(t)
                }
            })
        }

        private fun createRealCall(request: HiRequest): Call<ResponseBody> {
            when (request.httpMethod) {
                HiRequest.METHOD.GET -> {
                    return apiService.get(
                        request.headers,
                        request.endPointUrl(),
                        request.parameters
                    )
                }
                HiRequest.METHOD.POST -> {
                    val parameters = request.parameters
                    val builder = FormBody.Builder() //表单提交
                    val requestBody: RequestBody
                    val jsonObject = JSONObject()
                    if (parameters != null) {
                        for ((key, value) in parameters) {
                            if (request.formPost) {
                                builder.add(key, value)
                            } else {
                                jsonObject.put(key, value)
                            }
                        }
                    }
                    requestBody = if (request.formPost) {
                        builder.build()
                    } else {
                        RequestBody.create(
                            MediaType.parse("application/json;utf-8"),
                            jsonObject.toString()
                        )
                    }
                    return apiService.post(request.headers, request.endPointUrl(), requestBody)
                }
                else -> {
                    throw IllegalStateException("hirestful only support GET POST for now ,url=" + request.endPointUrl())
                }
            }
        }

        private fun parseResponse(response: Response<ResponseBody>): HiResponse<T> {
            var rawData = ""
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    rawData = body.string()
                }
            } else {
                val body: ResponseBody? = response.errorBody()
                if (body != null) {
                    rawData = body.string()
                }
            }
            return gsonConvert.convert(rawData, request.returnType!!)
        }
    }

    interface ApiService {
        @GET
        fun get(
            @HeaderMap headers: MutableMap<String, String>?,
            @Url url: String,
            @QueryMap(encoded = true) params: MutableMap<String, String>?
        ): Call<ResponseBody>

        @POST
        fun post(
            @HeaderMap headers: MutableMap<String, String>?,
            @Url url: String,
            @Body body: RequestBody?
        ): Call<ResponseBody>
    }
}