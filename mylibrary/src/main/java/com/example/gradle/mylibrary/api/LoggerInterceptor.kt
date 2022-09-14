package com.example.gradle.mylibrary.api

import com.example.gradle.mylibrary.log
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.nio.charset.StandardCharsets

/**
 * author:江伟
 * e-mail:jiangwei@sip.sh.cn
 * time  : 2022/09/13
 * desc  : this is LogInterceptor
 * version:  1.0
 */
class LoggerInterceptor:Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val buffer = Buffer()
        val request: Request = chain.request()
        val headers = request.headers
        log(
            "╔════════════════════════════════════════════════════════════════════════════════════════"
        )
        if (headers != null) {
            log(String.format("║ url %s", request.url))
            log(String.format("║ id %d", request.hashCode()))
            log(String.format("║ method %s", request.method))
            log(
                "╟────────────────────────────────────────────────────────────────────────────────────────"
            )
            request.body?.let {
                val contentType = it.contentType()
                log(String.format("║ contentType %s",contentType))
            }
            request.headers.forEach {
                log(String.format("║ Key:%s   Value:%s", it.first,it.second))
            }
        }
        val requestBody = request.body
        if (requestBody != null && requestBody !is MultipartBody) {
            log(
                "╟────────────────────────────────────────────────────────────────────────────────────────"
            )
            requestBody.writeTo(buffer)
            log(String.format("║ param %s", buffer.readString(StandardCharsets.UTF_8)))
            log(
                "╟────────────────────────────────────────────────────────────────────────────────────────"
            )
        }
        log("║ waiting response")
        log(
            "╚════════════════════════════════════════════════════════════════════════════════════════"
        )
        val response = chain.proceed(request)
        log("---------------------------------${requestBody}-------${requestBody is MultipartBody}")
        try {
            val responseBody = response.body
            val source = responseBody!!.source()
            log(
                "╔════════════════════════════════════════════════════════════════════════════════════════"
            )
            log(String.format("║ url %s", request.url))
            log(String.format("║ id %d", request.hashCode()))
            log("║ response")
            if (source == null) {
                log(String.format("║ code %s", response.code))
            } else {
                source.request(Long.MAX_VALUE) // Buffer the entire body.
                val bufferS = source.buffer()
                val contentType = responseBody.contentType()
                if (contentType != null) {
                    val json = bufferS.clone().readString(StandardCharsets.UTF_8)
                    val con = formatJson(json).split("\n").toTypedArray()
                    for (line in con) {
                        log("║$line")
                    }
                }
            }
            log(
                "╚════════════════════════════════════════════════════════════════════════════════════════"
            )
        } catch (e: IOException) {
            log(
                "╔════════════════════════════════════════════════════════════════════════════════════════"
            )
            log("║ IOException")
            log(String.format("║ url %s", request.url))
            log(String.format("║ id %d", request.hashCode()))
            log("║ response")
            log("║" + e.message + ", " + e.localizedMessage)
            log(
                "╚════════════════════════════════════════════════════════════════════════════════════════"
            )
            e.printStackTrace()
            throw e
        }
        return response
    }

    companion object {

        /**
         * 格式化
         *
         * @param jsonStr
         * @return
         */
        fun formatJson(jsonStr: String?): String {
            if (null == jsonStr || "" == jsonStr) return ""
            val sb = StringBuilder()
            var last = '\u0000'
            var current = '\u0000'
            var indent = 0
            var isInQuotationMarks = false
            for (element in jsonStr) {
                last = current
                current = element
                when (current) {
                    '"' -> {
                        if (last != '\\') {
                            isInQuotationMarks = !isInQuotationMarks
                        }
                        sb.append(current)
                    }
                    '{', '[' -> {
                        sb.append(current)
                        if (!isInQuotationMarks) {
                            sb.append('\n')
                            indent++
                            addIndentBlank(sb, indent)
                        }
                    }
                    '}', ']' -> {
                        if (!isInQuotationMarks) {
                            sb.append('\n')
                            indent--
                            addIndentBlank(sb, indent)
                        }
                        sb.append(current)
                    }
                    ',' -> {
                        sb.append(current)
                        if (last != '\\' && !isInQuotationMarks) {
                            sb.append('\n')
                            addIndentBlank(sb, indent)
                        }
                    }
                    else -> sb.append(current)
                }
            }
            return sb.toString()
        }

        /**
         * 添加space
         *
         * @param sb
         * @param indent
         */
        private fun addIndentBlank(sb: StringBuilder, indent: Int) {
            for (i in 0 until indent) {
                sb.append('\t')
            }
        }

        /**
         * 判断字符串是否为 null 或全为空格
         *
         * @param s 待校验字符串
         * @return `true`: null 或全空格<br></br> `false`: 不为 null 且不全空格
         */
        fun isTrimEmpty(s: String?): Boolean {
            return s == null || s.trim { it <= ' ' }.isEmpty()
        }
    }
}