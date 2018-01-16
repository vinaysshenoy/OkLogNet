package com.vinaysshenoy.oklognet

import java.util.Date

/**
 * Created by vinaysshenoy on 16/01/18.
 */
data class NetLog(
        val requestTime: Date,
        val url: String,
        val method: String,
        val requestHeaders: Map<String, String>,
        val requestBody: String,
        val responseCode: Int,
        val responseHeaders: Map<String, String>,
        val responseBody: String,
        val duration: Long)