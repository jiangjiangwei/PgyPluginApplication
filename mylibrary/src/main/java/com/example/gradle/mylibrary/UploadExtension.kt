package com.example.gradle.mylibrary

import com.android.builder.model.SigningConfig


/**
 * author:江伟
 * e-mail:jiangwei@sip.sh.cn
 * time  : 2022/09/07
 * desc  : this is UploadExtension
 * version:  1.0
 */
open class UploadExtension (
    //-------------- 加固相关的扩展 --------------
    var reinforceUserName: String = "",// 360加固的用户名
    var reinforcePassword: String = "",// 360加固的密码
    var reinforceFilePath:String = "",// 360加固jar包的路径
    var outputDirectory :String= "",// 加固后输出的apk目录
    var channelConfigFilePath :String= "", // 渠道路径
    var isOpenReinforce :Boolean= true,// 是否需要加固，默认为true
    var signingConfig: SigningConfig? = null,//签名信息

    // ------------ 上传蒲公英相关的扩展 ------------
    var apiKey :String= "",// API Key
    var appName :String= "",// 应用名称

    // ----------- 发送企业微信消息相关的扩展 -----------
    var webHookUrlKey :String= "78b3d70c-7faf-4b26-9459-5688646fe8ed", // 钉钉机器人WebHook地址key

){
    override fun toString(): String {
        return "UploadExtension(reinforceUserName='$reinforceUserName', reinforcePassword='$reinforcePassword', reinforceFilePath='$reinforceFilePath', outputDirectory='$outputDirectory', channelConfigFilePath='$channelConfigFilePath', isOpenReinforce=$isOpenReinforce, signingConfig=$signingConfig, apiKey='$apiKey', appName='$appName', webHookUrlKey='$webHookUrlKey')"
    }
}