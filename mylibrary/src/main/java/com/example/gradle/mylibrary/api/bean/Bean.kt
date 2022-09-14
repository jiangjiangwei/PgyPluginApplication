package com.example.gradle.mylibrary.api.bean

/**
 * author:江伟
 * e-mail:jiangwei@sip.sh.cn
 * time  : 2022/09/09
 * desc  : this is Bean
 * version:  1.0
 */

//{"code":0,"message":"","data":{"params":{"signature":"q-sign-algorithm=sha1&q-ak=AKID40eDr1qMsIN4s2gTI-ZR0M7aYiDzFDunP-rAe0JfrNRzhAjkS9xg8M96aPT80NPW&q-sign-time=1662707038;1662708898&q-key-time=1662707038;1662708898&q-header-list=&q-url-param-list=&q-signature=ec38d953a07cc114250ff74c3d3685c21d016e1d","x-cos-security-token":"0dcdLR1aCc69Y9hOPPMEqgXZfXEDedra8dd07b383267a90839105e8a1fcb2853LDrs_psJkWrpcQUzvrbwomw3oan49yLr-2_qWsMP_UuTAEuJOlfDnqLQ5v7Rs3y-2mQw69zZ582oX6LY2iLo-eAXPRtEiE6YwzETfUturN5ke2fNEPXnT9TMI49XN7d6UzB8Jx5evgEw3f62vwr3NZKB9ZeWMSf75H8glS1Ks-SHXOO8ekfMu6Buuw-Ug6sPktWCMMgHxRDTnLuvbRYim06WD1ob_FItVSDk49qUx0xaraR3SSPs_f6UvUiMkdl8dfLf_xUZIZ7v31wMUmB5tCwzaD2teZ-0WVIbiI_pxmlhhgUpXm1OTnZopWQ2NnVNIlPT7uzdKtW1vLbOsgfylKNh2wbVbZYz52i7CrHSeAE","key":"da98eb7f16482114ce91c68f571da2d8.apk"},"key":"da98eb7f16482114ce91c68f571da2d8.apk","endpoint":"https:\/\/pgy-apps-1251724549.cos.ap-guangzhou.myqcloud.com"}}
data class PgyResult<T>(
    val code: Int? = 0,
    val message: String? = "",
    val data: T?
) {
    fun isSuccess() = 0 == code
}

data class PgyTokenResult(
    val key: String, //key 上传文件存储标识唯一 key
    val endpoint: String,//上传文件的 URL
    val params: Params //上传文件需要的参数，包含signature、x-cos-security-token、key
) {
    data class Params(
        val signature: String,
        val `x-cos-security-token`: String,
        val key: String? = "",
    )
}



data class PgyAppPublishResult(
    val buildKey:String,
    val buildType:String,
    val buildIsFirst:String,
    val buildIsLastest:String,
    val buildFileSize:String,
    val buildName:String,
    val buildVersion:String,
    val buildVersionNo:String,
    val buildBuildVersion:String,
    val buildIdentifier:String,
    val buildIcon:String,
    val buildDescription:String,
    val buildUpdateDescription:String,
    val buildScreenShots:String,
    val buildShortcutUrl:String,
    val buildQRCodeURL:String,
    val buildCreated:String,
    val buildUpdated:String,

//    val code:String,//错误码，1216 应用发布失败|错误码，1247 应用正在发布中
//    val message:String,//信息提示
)