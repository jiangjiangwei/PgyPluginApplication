package com.example.gradle.mylibrary.task

import com.android.build.gradle.api.ApplicationVariant
import com.example.gradle.mylibrary.*
import com.example.gradle.mylibrary.api.Api
import com.example.gradle.mylibrary.api.PgyBaseService
import com.example.gradle.mylibrary.api.PgyUploadService
import com.example.gradle.mylibrary.api.WxworkService
import com.example.gradle.mylibrary.api.bean.PgyAppPublishResult
import com.example.gradle.mylibrary.api.bean.PgyResult
import com.example.gradle.mylibrary.api.bean.PgyTokenResult
import com.example.gradle.mylibrary.utils.WxworkHelper
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.inject.Inject

/**
 * author:江伟
 * e-mail:jiangwei@sip.sh.cn
 * time  : 2022/09/09
 * desc  : this is UploadPgyTask
 * version:  1.0
 */
open class UploadPgyTask @Inject constructor(
    private val uploadExtension: UploadExtension,
    private val variant: ApplicationVariant,
    private val outputFile: File,
) : DefaultTask() {
    init {
        description = "upload apk to pgyer"
        group = TASK_UPLOAD_PGY
    }

    @TaskAction
    fun doAction() {
        log("############################上传蒲公英#############################")
        log("# applicationId: ${variant.applicationId}")
        log("# versionName  : ${variant.versionName}")
        log("# versionCode  : ${variant.versionCode}")
        log("# appName      : ${uploadExtension.appName}")
        log("# apkUrl       : ${outputFile.absolutePath}")
        log("#################################################################")

        val cosToken =
            Api.get(BASE_PGY_URL, PgyBaseService::class.java).getCOSToken(uploadExtension.apiKey)
                .execute()
        val response = cosToken.body()?.string()
        val pgyResult = response.toBean<PgyResult<PgyTokenResult>>()
        log("result-getCOSToken==${pgyResult}")

        //上传apk
        pgyResult?.data?.run {
            val multipartBody = MultipartBody.Builder()
                .addFormDataPart("key", key)
                .addFormDataPart("signature", params.signature)
                .addFormDataPart("x-cos-security-token", params.`x-cos-security-token`)
                .addFormDataPart("file", outputFile.name, outputFile.asRequestBody())
                .build()
            val uploadResponse =
                Api.get(endpoint, PgyUploadService::class.java).uploadApk(multipartBody).execute()
            log("result-uploadApp=$uploadResponse")
            if (UPLOAD_SUCCESS_CODE == uploadResponse.code()) {
                log("开始查询发版状态")
                //轮训查询上传结果、
                queryAppPublishStatusTimer(uploadExtension.apiKey, pgyResult.data.key)
            } else {
                log("apk上传失败..")
            }
        }
    }


    private fun queryAppPublishStatusTimer(
        _api_key: String,
        buildKey: String,
    ) {
        try {
            Thread.sleep(3 * 1000L)
            queryAppPublishStatus(_api_key, buildKey)
        } catch (e: InterruptedException) {
            e.printStackTrace()
            Thread.currentThread().interrupt()
        }
    }

    private fun queryAppPublishStatus(
        _api_key: String,
        buildKey: String,
    ) {
        val params = mapOf(
            Pair("_api_key", _api_key),
            Pair("buildKey", buildKey),
        )
        val result = Api.get(BASE_PGY_URL, PgyBaseService::class.java)
            .queryBuildInfo(params)
            .execute()
        val pgyResult = result.body()?.string()?.toBean<PgyResult<PgyAppPublishResult>>()
        log("result-query-buildInfo====$pgyResult")
        when (pgyResult?.code) {
            1246, 1247 -> {
                log("应用正在发布中..")
                queryAppPublishStatusTimer(_api_key, buildKey)
            }
            1216, 1249 -> {
                log("应用发布失败:${pgyResult.message}")
            }
            else -> {
                log("应用发布成功")
                //发送信息到企业微信
//                        (buildKey=a7c7d868621e41695dc2cac02e75be70, buildType=2, buildIsFirst=0, buildIsLastest=1,
                //                        buildFileSize=4139025, buildName=GradleTApplication, buildVersion=1.0,
                //                        buildVersionNo=1, buildBuildVersion=12, buildIdentifier=com.example.gradle.gradletapplication,
                //                        buildIcon=63d7930b1e0ecd7e390d68dd7f2cb8e7, buildDescription=, buildUpdateDescription=,
                //                        buildScreenShots=null, buildShortcutUrl=IfNC,
                //                        buildQRCodeURL=https://www.pgyer.com/app/qrcodeHistory/2c6548a9fc40f3d3ea6b3da125d4658e88f360dc0873c396de778b5aaa748a60,
                //                        buildCreated=2022-09-13 10:35:48, buildUpdated=2022-09-13 10:35:48)
                sendMsgToWxwork(pgyResult?.data)
            }
        }
    }


    /**
     * 发送消息到企业微信
     * 1一条markdown消息（markdown消息不支持图片）
     * 2一条图片消息
     */
    private fun sendMsgToWxwork(data: PgyAppPublishResult?) {
        data?.let {
            val markdownParams = WxworkHelper.getMarkdownMsg(it)
            val response = Api.get(BASE_WX_WORK_URL, WxworkService::class.java)
                .sendMessage(uploadExtension.webHookUrlKey, markdownParams.toString().toJsonBody())
                .execute()
            val sendResult = response.body()?.string()
            log("wxwork_send-markdown-response==$sendResult")


            val imgParam = WxworkHelper.getNetImgurlToBase64Msg(it.buildQRCodeURL)
            val response1 = Api.get(BASE_WX_WORK_URL, WxworkService::class.java)
                .sendMessage(uploadExtension.webHookUrlKey, imgParam.toString().toJsonBody())
                .execute()
            val sendResult1 = response1.body()?.string()
            log("wxwork_send-image-response==$sendResult1")
        }
    }
}