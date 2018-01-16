package com.vinaysshenoy.oklognet

import com.vinaysshenoy.oklognet.db.NetLogDao
import com.vinaysshenoy.oklognet.db.NetLogEntity
import okhttp3.FormBody
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import java.util.concurrent.Executors

/**
 * Created by vinaysshenoy on 16/01/18.
 */

internal class OkLogNetInterceptor(private val netLogDao: NetLogDao) : Interceptor {

    private val contentTypesToSave = setOf(
            "application/json",
            "application/x-www-form-urlencoded",
            "application/xml")

    private val writeExecutor = Executors.newSingleThreadExecutor {
        Thread("oklognet-write-thread")
    }

    override fun intercept(chain: Chain): Response {

        val requestTime = System.currentTimeMillis()

        val request = chain.request()

        val url = request.url().toString()
        val method = request.method()
        val requestHeaders = readHeaderString(request.headers())
        val requestBody = readRequestBody(request.body())

        var responseCode = -1
        var responseHeaders = ""
        var responseBody = ""
        var duration = 0L

        try {
            val response = chain.proceed(reconstructRequest(request, requestBody))
            responseCode = response.code()
            responseHeaders = readHeaderString(response.headers())
            responseBody = readResponseBody(response.body())

            return reconstructResponse(response, responseBody)
        } catch (e: Exception) {
            throw e
        } finally {
            duration = System.currentTimeMillis() - requestTime
            save(requestTime, url, method, requestHeaders, requestBody, responseCode,
                    responseHeaders, responseBody, duration)
        }
    }

    private fun save(requestTime: Long, url: String, method: String, requestHeaders: String,
            requestBody: String, responseCode: Int, responseHeaders: String, responseBody: String,
            duration: Long) {
        writeExecutor.submit {
            val netLogEntity = NetLogEntity().apply {
                this.requestTime = requestTime
                this.url = url
                this.method = method
                this.requestHeaders = requestHeaders
                this.requestBody = requestBody
                this.responseCode = responseCode
                this.responseHeaders = responseHeaders
                this.responseBody = responseBody
                this.duration = duration
            }
            netLogDao.put(netLogEntity)
        }
    }

    private fun reconstructResponse(response: Response, body: String) =
            when {
                body.isBlank() -> response
                else -> {
                    response.newBuilder()
                            .body(ResponseBody.create(response.body()?.contentType(), body))
                            .build()
                }
            }

    private fun reconstructRequest(request: Request, body: String) =
            when {
                body.isBlank() -> request
                else -> {
                    val builder = request.newBuilder()
                    val newBody = FormBody.create(request.body()?.contentType(), body)
                    when (request.method()) {
                        "POST" -> builder.post(newBody)
                        "PUT" -> builder.post(newBody)
                        "PATCH" -> builder.patch(newBody)
                        "DELETE" -> builder.delete(newBody)
                        else -> throw IllegalStateException("Unknown method: ${request.method()}")
                    }
                    builder.build()
                }
            }

    private fun readHeaderString(headers: Headers?): String {
        return headers?.let { actualHeaders ->
            actualHeaders.names()
                    .map { Pair(it, actualHeaders.get(it) ?: "") }
                    .joinToString("|")
        } ?: ""
    }

    private fun readResponseBody(body: ResponseBody?): String {
        return body?.let { actualBody ->
            val contentType = contentTypeFromMediaType(actualBody.contentType())

            when {
                contentType.isNotBlank() -> convertResponseBodyToString(actualBody, contentType)
                else -> "OkNetLog: No Content Type"
            }
        } ?: ""
    }

    private fun convertResponseBodyToString(actualBody: ResponseBody, contentType: String) =
            when (contentType) {
                in contentTypesToSave -> readBodyFromResponseBody(actualBody)
                else -> "OkNetLog: $contentType is skipped from saving"
            }

    private fun readBodyFromResponseBody(actualBody: ResponseBody) = actualBody.string()

    private fun readRequestBody(body: RequestBody?): String {
        return body?.let { actualBody ->

            val contentType = contentTypeFromMediaType(actualBody.contentType())

            when {
                contentType.isNotBlank() -> convertRequestBodyToString(actualBody, contentType)
                else -> "OkNetLog: No Content Type"
            }

        } ?: "OkNetLog: No Body Present"
    }

    private fun convertRequestBodyToString(requestBody: RequestBody, contentType: String) =
            when (contentType) {
                in contentTypesToSave -> readBodyFromRequestBody(requestBody)
                else -> "OkNetLog: $contentType is skipped from saving"
            }

    private fun readBodyFromRequestBody(requestBody: RequestBody) = Buffer()
            .also { requestBody.writeTo(it) }
            .readUtf8()

    private fun contentTypeFromMediaType(mediaType: MediaType?) =
            mediaType?.let {
                val type = mediaType.type() ?: ""
                val subType = mediaType.subtype() ?: ""

                if (type.isNotBlank() && subType.isNotBlank()) "$type/$subType" else ""
            } ?: ""
}