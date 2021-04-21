package com.zj.hi_library.restful

import com.zj.hi_library.restful.annotation.*
import java.lang.Exception
import java.lang.reflect.*

/**
 * 解析请求方法上的注解，得到需要的请求参数和返回值
 */
class MethodParser(private val baseUrl: String, method: Method) {

    private var cacheStrategy: Int = CacheStrategy.NET_ONLY
    private var domainUrl: String? = null
    private var formPost: Boolean = false
    private var httpMethod: Int = 0
    private lateinit var relativeUrl: String
    private var headers: MutableMap<String, String> = mutableMapOf()
    private var parameters: MutableMap<String, String> = mutableMapOf()

    private var returnType: Type? = null // 真实的数据返回类型

    init {
        // 解析方法上的注解 比如：get、headers
        parserMethodAnnotations(method)

        // 解析方法的返回值类型
        parserMethodReturnType(method)
    }

    /**
     * interface ApiService {
     *   @Headers("auth-token:token", "accountId:123456")
     *   @BaseUrl("https://api.devio.org/as/")
     *   @POST("/cities/{province}")
     *   @GET("/cities")
     *   fun listCities(
     *       @Path("province") province: Int,
     *       @Filed("page") page: Int
     *   ): HiCall<JSONObject>
     * }
     */
    private fun parserMethodReturnType(method: Method) {
        if (method.returnType != HiCall::class.java) {
            throw IllegalStateException(
                String.format(
                    "method %s is must be type of HiCall.class",
                    method.name
                )
            )
        }
        val genericReturnType = method.genericReturnType
        if (genericReturnType is ParameterizedType) {
            val actualTypeArguments = genericReturnType.actualTypeArguments
            require(actualTypeArguments.size == 1) {
                String.format("method %s can only has one generic return type", method.name)
            }
            val type = actualTypeArguments[0]
            require(validateGenericType(type)) {
                String.format("method %s generic return type must not be an unknown type. " + method.name)
            }
            returnType = type
        } else {
            throw IllegalStateException(
                String.format(
                    "method %s is must has one generic return type",
                    method.name
                )
            )
        }
    }

    private fun parserMethodParameters(method: Method, args: Array<Any>) {
        parameters.clear() //每次调用api接口都需要清楚上一次的请求，因为存在复用

        //@Path("province") province: Int, @Filed("page") page: Int
        val parameterAnnotations = method.parameterAnnotations
        val equals = parameterAnnotations.size == args.size
        require(equals) {
            String.format(
                "arguments annotations count %s don't match expect count %s",
                parameterAnnotations.size,
                args.size
            )
        }

        for (index in args.indices) {
            val annotations = parameterAnnotations[index]
            require(annotations.size <= 1) { "filed can only has one annotation :index = $index" }

            val value = args[index]
            require(isPrimitive(value)) { "8 basic types are supported for now, index = $index" }

            val annotation = annotations[0]
            if (annotation is Filed) {
                val key = annotation.value
                parameters[key] = value.toString()
            } else if (annotation is Path) {
                //@POST("/cities/{province}")
                //@Path("province") province: Int
                val replaceName = annotation.value
                val replacement = value.toString()
                if (replaceName != null && replacement != null) {
                    relativeUrl = relativeUrl.replace("{$replaceName}", replacement)
                }
            } else if (annotation is CacheStrategy) {
                cacheStrategy = value as Int
            } else {
                throw IllegalStateException(
                    String.format(
                        "cannot handle parameter annotation:",
                        annotation.javaClass.toString()
                    )
                )
            }
        }
    }

    private fun isPrimitive(value: Any): Boolean {
        // String
        if (value.javaClass == String::class.java) {
            return true
        }
        try {
            //int byte short long boolean char double float
            val field = value.javaClass.getField("TYPE")
            val clazz = field[null] as Class<*>
            if (clazz.isPrimitive) {
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun validateGenericType(type: Type): Boolean {
        /**
         *wrong
         *  fun test():HiCall<Any>
         *  fun test():HiCall<List<*>>
         *  fun test():HiCall<ApiInterface>
         *expect
         *  fun test():HiCall<User>
         */
        //如果指定的泛型是集合类型的，那还检查集合的泛型参数
        if (type is GenericArrayType) {
            return validateGenericType(type.genericComponentType)
        }

        //如果指定的泛型是一个接口 也不行
        if (type is TypeVariable<*>) {
            return false
        }
        //如果指定的泛型是一个通配符 ？extends Number 也不行
        if (type is WildcardType) {
            return false
        }
        return true
    }

    private fun parserMethodAnnotations(method: Method) {
        val annotations = method.annotations
        for (annotation in annotations) {
            when (annotation) {
                is GET -> {
                    relativeUrl = annotation.value
                    httpMethod = HiRequest.METHOD.GET
                }
                is POST -> {
                    relativeUrl = annotation.value
                    httpMethod = HiRequest.METHOD.POST
                    formPost = annotation.formPost
                }
                is Headers -> {
                    // @Headers("auth-token:token", "accountId:123456")
                    val headersArray = annotation.value
                    for (header in headersArray) {
                        val colon = header.indexOf(":")
                        check(!(colon == 0 || colon == 1)) {
                            String.format(
                                "@headers value must be in the form [name:value],but found [%s]",
                                header
                            )
                        }
                        val name = header.substring(0, colon)
                        val value = header.substring(colon + 1).trim()
                        headers[name] = value
                    }
                }
                is BaseUrl -> {
                    domainUrl = annotation.value
                }

                is CacheStrategy -> {
                    cacheStrategy = annotation.value
                }
                else -> {
                    throw IllegalStateException(
                        String.format(
                            "cannot handle method annotation:",
                            annotation.javaClass.toString()
                        )
                    )
                }
            }
        }

        require(!(httpMethod != HiRequest.METHOD.GET && httpMethod != HiRequest.METHOD.POST)) {
            String.format("method %s must has one of GET,POST", method.name)
        }

        if (domainUrl == null) {
            domainUrl = baseUrl
        }
    }

    fun newRequest(method: Method, args: Array<out Any>?): HiRequest {
        val arguments: Array<Any> = args as Array<Any>? ?: arrayOf()
        // 解析方法入参的注解，比如：path、filed
        parserMethodParameters(method, arguments)

        val hiRequest = HiRequest()
        hiRequest.domainUrl = domainUrl
        hiRequest.headers = headers
        hiRequest.httpMethod = httpMethod
        hiRequest.parameters = parameters
        hiRequest.relativeUrl = relativeUrl
        hiRequest.returnType = returnType
        hiRequest.formPost = formPost
        hiRequest.cacheStrategy = cacheStrategy
        return hiRequest
    }

    companion object {
        fun parse(baseUrl: String, method: Method): MethodParser {
            return MethodParser(baseUrl, method)
        }
    }

}