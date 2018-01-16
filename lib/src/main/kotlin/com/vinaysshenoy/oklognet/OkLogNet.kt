package com.vinaysshenoy.oklognet

import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response

/**
 * Created by vinaysshenoy on 16/01/18.
 */
class OkLogNet: Interceptor {

    override fun intercept(chain: Chain): Response {
        return chain.proceed(chain.request())
    }

}