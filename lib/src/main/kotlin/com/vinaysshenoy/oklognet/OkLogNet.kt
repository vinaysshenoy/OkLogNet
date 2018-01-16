package com.vinaysshenoy.oklognet

import android.arch.persistence.room.Room
import android.content.Context
import com.vinaysshenoy.oklognet.db.OkLogNetDb
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response

/**
 * Created by vinaysshenoy on 16/01/18.
 */
class OkLogNet(context: Context) : Interceptor {

    private val netLogDb = Room.databaseBuilder(context.applicationContext, OkLogNetDb::class.java,
            "oknetlog.db")
            .build()

    private val okNetLogDao = netLogDb.netLogDao

    override fun intercept(chain: Chain): Response {
        return chain.proceed(chain.request())
    }

}