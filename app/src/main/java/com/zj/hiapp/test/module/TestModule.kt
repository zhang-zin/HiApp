package com.zj.hiapp.test.module

import com.zj.hi_library.hiLog.HiLog
import com.zj.hiapp.http.ApiFactory
import com.zj.hiapp.http.api.AccountApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import javax.inject.Inject
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object TestModule {

    @Provides
    @Singleton
    fun getAccountService(): AccountApi {
        return ApiFactory.create(AccountApi::class.java)
    }
}

@Module
@InstallIn(ActivityComponent::class)
abstract class AnalyticsModule {

    //注入接口实例
    @Binds
    abstract fun bindAnalyticsService(analyticsServiceImpl: AnalyticsServiceImpl): AnalyticsService
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthInterceptorOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OtherInterceptorOkHttpClient

interface AnalyticsService {
    fun analyticsMethods()
}

class AnalyticsServiceImpl @Inject constructor() : AnalyticsService {

    override fun analyticsMethods() {
        HiLog.e("----analyticsMethods-----")
    }
}

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @AuthInterceptorOkHttpClient
    @Provides
    fun provideAuthInterceptorOkHttpClient(
        string: String
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @OtherInterceptorOkHttpClient
    @Provides
    fun provideOtherInterceptorOkHttpClient(
        int: Int
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }
}
