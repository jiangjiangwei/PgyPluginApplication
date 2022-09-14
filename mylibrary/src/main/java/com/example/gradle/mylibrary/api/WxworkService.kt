package com.example.gradle.mylibrary.api

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * author:江伟
 * e-mail:jiangwei@sip.sh.cn
 * time  : 2022/09/13
 * desc  : this is WxworkService
 * version:  1.0
 */
interface WxworkService {

    //参考api：https://developer.work.weixin.qq.com/tutorial/detail/54
    //参考api：https://developer.work.weixin.qq.com/document/path/91770
//    val webHookUrl =
//        "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=78b3d70c-7faf-4b26-9459-5688646fe8ed"


    //    @POST("cgi-bin/webhook/send")
    @POST("cgi-bin/webhook/send")
    fun sendMessage(@Query("key")key:String, @Body requestBody: RequestBody): Call<ResponseBody>
}