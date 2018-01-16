package com.vinaysshenoy.oklognet

import android.arch.persistence.room.Room
import android.content.Context
import com.vinaysshenoy.oklognet.db.NetLogEntity
import com.vinaysshenoy.oklognet.db.OkLogNetDb
import okhttp3.Interceptor
import java.util.Date

/**
 * Created by vinaysshenoy on 16/01/18.
 */
class OkLogNet(context: Context) {

    private val okLogNetDb by lazy {
        Room.databaseBuilder(context.applicationContext, OkLogNetDb::class.java,
                "oknetlog.db")
                .build()
    }

    private val netLogDao by lazy { okLogNetDb.netLogDao }

    val interceptor by lazy { OkLogNetInterceptor(netLogDao) as Interceptor }

    fun getAllNetLogs() = netLogDao.getAll().map(this::mapNetLogEntitityToModel)

    private fun mapNetLogEntitityToModel(entity: NetLogEntity) =
            NetLog(
                    requestTime = Date(entity.requestTime),
                    url = entity.url,
                    method = entity.method,
                    requestHeaders = convertHeaderStringToHeaderMap(entity.requestHeaders),
                    requestBody = entity.requestBody,
                    responseCode = entity.responseCode,
                    responseHeaders = convertHeaderStringToHeaderMap(entity.responseHeaders),
                    responseBody = entity.responseBody,
                    duration = entity.duration)

    //TODO: Test this chain
    private fun convertHeaderStringToHeaderMap(headerString: String)
            = headerString
            .split("|")
            .filter { it.contains("=") }
            .map { it.split("=") }
            .filter { it.size == 2 }
            .associate { Pair(it[0], it[1]) }

}