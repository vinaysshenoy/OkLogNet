package com.vinaysshenoy.oklognet.db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by vinaysshenoy on 16/01/18.
 */
@Entity(tableName = "OkNetLog_NetLogs")
internal class NetLogEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id = 0L

    @ColumnInfo(name = "url")
    var url = ""

    @ColumnInfo(name = "request_time")
    var requestTime = 0L

    @ColumnInfo(name = "method")
    var method = ""

    @ColumnInfo(name = "request_headers")
    var requestHeaders = ""

    @ColumnInfo(name = "request_body")
    var requestBody = ""

    @ColumnInfo(name = "response_code")
    var responseCode = 0

    @ColumnInfo(name = "response_headers")
    var responseHeaders = ""

    @ColumnInfo(name = "response_body")
    var responseBody = ""

    @ColumnInfo(name = "duration")
    var duration = 0L
}