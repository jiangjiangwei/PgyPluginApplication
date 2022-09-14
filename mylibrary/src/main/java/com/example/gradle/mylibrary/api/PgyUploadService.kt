package com.example.gradle.mylibrary.api

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


/**
 * author:江伟
 * e-mail:jiangwei@sip.sh.cn
 * time  : 2022/09/09
 * desc  : this is PgyUploadService
 * version:  1.0
 */
interface PgyUploadService {
    //动态baseurl
    @POST("/")
    fun uploadApk(@Body body: MultipartBody):Call<ResponseBody>
}