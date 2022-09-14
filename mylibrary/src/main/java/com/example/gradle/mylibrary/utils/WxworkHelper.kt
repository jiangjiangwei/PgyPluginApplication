package com.example.gradle.mylibrary.utils

import com.example.gradle.mylibrary.api.Api
import com.example.gradle.mylibrary.api.bean.PgyAppPublishResult
import com.example.gradle.mylibrary.formatMB
import okhttp3.Request
import org.apache.commons.codec.digest.DigestUtils
import org.json.JSONArray
import org.json.JSONObject
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*

/**
 * author:江伟
 * e-mail:jiangwei@sip.sh.cn
 * time  : 2022/09/13
 * desc  : this is WxworkHelper
 * version:  1.0
 */
object WxworkHelper {

    /**
     * 获取text消息
     */
    fun getTextMsg(data: PgyAppPublishResult): JSONObject {
        val content = """
          应用名称：${data.buildName}
          版本信息：${data.buildVersion} (Build:${data.buildBuildVersion})
          应用大小：${(data.buildFileSize.toInt() * 1f / 1024 / 1024).formatMB()}M
          更新时间：${data.buildUpdated}
          更新内容：${"更新内容"}
          扫码下载：${data.buildQRCodeURL}
        """.trimIndent()
        //企业微信通知人
        val textParams = JSONObject().apply {
            put("msgtype", "text")
            put("text", JSONObject().apply {
                put("content", content)
//                put("mentioned_mobile_list", "15000853651")//多个用逗号分隔
            })
        }
        return textParams
    }

    /**
     * 获取news消息
     */
    fun getNewsMsg(data: PgyAppPublishResult): JSONObject {
        val content = """
          应用名称：${data.buildName}
          版本信息：${data.buildVersion} (Build:${data.buildBuildVersion})
          应用大小：${(data.buildFileSize.toInt() * 1f / 1024 / 1024).formatMB()}M
          更新时间：${data.buildUpdated}
          更新内容：${"更新内容"}
          扫码下载：${data.buildQRCodeURL}
        """.trimIndent()
            val params = JSONObject().apply {
                put("msgtype", "news")
                put("news", JSONObject().apply {
                    put("articles", JSONArray().apply {
                        put(JSONObject().apply {
                            put("title", "软件更新提醒")
                            put("description", content)
                            put("picurl", data.buildQRCodeURL)
                            put("url", data.buildQRCodeURL)
                        })
                    })
                })
            }

        return params
    }

    /**
     * 获取markdown消息
     */
    fun getMarkdownMsg(data: PgyAppPublishResult): JSONObject {
        val markdownContent = """
                **版本更新提示**
                  应用名称：${data.buildName}
                  版本信息：${data.buildVersion} (Build:${data.buildBuildVersion})
                  应用大小：${(data.buildFileSize.toInt() * 1f / 1024 / 1024).formatMB()}M
                  更新时间：${data.buildUpdated}
                  更新内容：${"更新内容"}
                  扫码下载：[二维码](${data.buildQRCodeURL})
            """.trimIndent()
        val params = JSONObject().apply {
            put("msgtype", "markdown")
            put("markdown", JSONObject().apply {
                put("content", markdownContent)
            })
        }
        return params
    }

    /**
     * 将网络图片地址，直接转为图片信息的jsonObject
     */
    fun getNetImgurlToBase64Msg(url: String): JSONObject {
        val okHttpClient = Api.createClient()
        val request = Request.Builder()
            .url(url)
            .method("GET", null)
            .build()
        //base64
        val response = okHttpClient.newCall(request).execute()
        val bytes = response.body?.bytes()
        val base64 = Base64.getEncoder().encodeToString(bytes)
        // 获取md5值
        val md5 = DigestUtils.md5Hex(bytes)
        val image = JSONObject()
        image.put("base64", base64)
        image.put("md5", md5)
        val reqBody = JSONObject()
        reqBody.put("msgtype", "image")
        reqBody.put("image", image)
        return reqBody
    }

    /**
     * 发送图片消息，需要对图片进行base64编码并计算图片的md5值
     *
     * @param path 需要发送的图片路径
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun getImgMsg(path: String): JSONObject {
        var base64 = ""
        var md5 = ""

        // 获取Base64编码
        try {
            val inputStream = FileInputStream(path)
            val bs = ByteArray(inputStream.available())
            inputStream.read(bs)
            base64 = Base64.getEncoder().encodeToString(bs)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // 获取md5值
        try {
            val inputStream = FileInputStream(path)
            val buf = ByteArray(inputStream.available())
            inputStream.read(buf)
            md5 = DigestUtils.md5Hex(buf)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val image = JSONObject()
        image.put("base64", base64)
        image.put("md5", md5)
        val reqBody = JSONObject()
        reqBody.put("msgtype", "image")
        reqBody.put("image", image)
        return reqBody
    }

}