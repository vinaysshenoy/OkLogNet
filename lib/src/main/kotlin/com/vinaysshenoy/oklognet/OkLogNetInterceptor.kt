package com.vinaysshenoy.oklognet

import com.vinaysshenoy.oklognet.db.NetLogDao
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response

/**
 * Created by vinaysshenoy on 16/01/18.
 */
internal class OkLogNetInterceptor(private val netLogDao: NetLogDao): Interceptor {

    override fun intercept(chain: Chain): Response {
        return chain.proceed(chain.request())
    }
}