package com.vinaysshenoy.oklognet

import android.arch.persistence.room.Room
import android.content.Context
import com.vinaysshenoy.oklognet.db.OkLogNetDb
import okhttp3.Interceptor

/**
 * Created by vinaysshenoy on 16/01/18.
 */
class OkLogNet(context: Context) {

    private val netLogDb = Room.databaseBuilder(context.applicationContext, OkLogNetDb::class.java,
            "oknetlog.db")
            .build()

    private val okNetLogDao = netLogDb.netLogDao

    val interceptor by lazy { OkLogNetInterceptor(okNetLogDao) as Interceptor }

}