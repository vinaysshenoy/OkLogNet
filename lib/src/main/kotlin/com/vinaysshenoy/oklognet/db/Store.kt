package com.vinaysshenoy.oklognet.db

import com.vinaysshenoy.oklognet.NetLog

/**
 * Created by vinaysshenoy on 16/01/18.
 */
interface Store {

    fun put(netLog: NetLog)

    fun getAll(): List<NetLog>
}