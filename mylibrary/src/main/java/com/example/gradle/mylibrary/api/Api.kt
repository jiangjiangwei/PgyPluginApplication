package com.example.gradle.mylibrary.api

import com.example.gradle.mylibrary.log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * author:江伟
 * e-mail:jiangwei@sip.sh.cn
 * time  : 2022/09/09
 * desc  : this is Api
 * version:  1.0
 */
object Api {
    private val retrofits = mutableMapOf<Any, Retrofit>()

    fun <T> get(url: String, clazz: Class<T>): T {
        return getRetrofit(url, clazz).create(clazz)
    }

    private fun <T> getRetrofit(url: String, clazz: Class<T>): Retrofit {
        var retrofit = retrofits[clazz]
        if (retrofit == null) {
            synchronized(Api::class.java) {
                if (retrofit == null) {
                    retrofit = Retrofit.Builder()
                        .baseUrl(url)
                        .client(createClient())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }
            }
        }
        return retrofit!!
    }


    private fun <T> getBaseUrl(clazz: Class<T>): String {
        return clazz.getAnnotation(Service::class.java).baseUrl
    }

     fun createClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .callTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(LoggerInterceptor())
//            .addInterceptor(getHttpLogInterceptor())  //上传图片日志太多
        return builder.build()
    }


    private fun getHttpLogInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor {
            log(it)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return httpLoggingInterceptor
    }

}