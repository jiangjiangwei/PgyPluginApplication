package com.example.gradle.mylibrary

import com.android.builder.model.SigningConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.math.RoundingMode
import java.nio.charset.Charset
import java.text.DecimalFormat

/**
 * author:江伟
 * e-mail:jiangwei@sip.sh.cn
 * time  : 2022/09/08
 * desc  : this is Ext
 * version:  1.0
 */
/**
 * 在控制台执行命令
 */

fun SigningConfig?.sign(): String {
    this ?: return ""
    return "$storeFile $storePassword $keyAlias $keyPassword"
}

@Throws(IOException::class)
fun String.execute() = Runtime.getRuntime().exec(this)

/**
 * 获取控制台文字信息
 */
fun Process.text(charset: Charset = Charset.forName("gbk")): String {
    return this.inputStream.reader(charset).readText()
}

fun Process.error(charset: Charset = Charset.forName("gbk")) =
    this.errorStream.reader(charset).readText()

/**
 * 输出加固日志
 */
fun log(msg: String?) {
    msg?.let { println("JiaGu >>> $it") }
}

/**
 * 获取编码字符，如果指定的编码有误，则返回null
 */
fun forNameCharsetOrNull(charset: String): Charset? {
    return try {
        Charset.forName(charset)
    } catch (e: Exception) {
        null
    }
}


/**
 * 忽略异常
 */
inline fun <R> tryCaching(block: () -> R): R? {
    return try {
        block.invoke()
    } catch (e: Exception) {
        null
    }
}


inline fun <reified T> String?.toBean(): T? {
    val type = object : TypeToken<T>() {}.type
    return Gson().fromJson(this, type)
}

fun Number.formatMB(): String {
    val format = DecimalFormat("0.00")
    format.roundingMode = RoundingMode.HALF_UP
    return format.format(this)
}

fun String.toJsonBody(): RequestBody {
    return this.toRequestBody("application/json;charset=utf-8".toMediaTypeOrNull())
}
