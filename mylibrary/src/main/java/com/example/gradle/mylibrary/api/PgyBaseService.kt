package com.example.gradle.mylibrary.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


/**
 * author:江伟
 * e-mail:jiangwei@sip.sh.cn
 * time  : 2022/09/09
 * desc  : this is PgyUploadService
 * version:  1.0
 */
interface PgyBaseService {
    /**
     * 获取上传相关信息
     * 例如token，url
     */
    @FormUrlEncoded
    @POST("app/getCOSToken/")
    fun getCOSToken(
        @Field("_api_key") key: String,
        @Field("buildType") buildType: String = "android",
    ): Call<ResponseBody>



    /**
     *  _api_key 	(必填) API Key
     *  "buildKey"  //getCOSToken返回
     */
    @GET("app/buildInfo")
    fun queryBuildInfo(
        @QueryMap params:Map<String,String>): Call<ResponseBody>
}