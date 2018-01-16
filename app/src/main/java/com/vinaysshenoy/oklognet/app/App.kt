package com.vinaysshenoy.oklognet.app

import android.app.Application
import com.facebook.stetho.Stetho
import com.vinaysshenoy.oklognet.OkLogNet
import okhttp3.OkHttpClient

/**
 * Created by vinaysshenoy on 16/01/18.
 */
class App : Application() {

    lateinit var okLogNet: OkLogNet
        private set

    lateinit var okHttpClient: OkHttpClient

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        initOkHttpClient()
    }

    private fun initOkHttpClient() {

        okLogNet = OkLogNet(this)

        okHttpClient = OkHttpClient.Builder()
                .addInterceptor(okLogNet.interceptor)
                .build()
    }

}