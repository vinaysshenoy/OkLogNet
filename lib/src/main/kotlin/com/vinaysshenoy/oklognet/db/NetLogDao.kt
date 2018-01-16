package com.vinaysshenoy.oklognet.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

/**
 * Created by vinaysshenoy on 16/01/18.
 */
@Dao
internal interface NetLogDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun put(netLog: NetLogEntity): Long

    @Query("SELECT * FROM `NetLogs`")
    fun getAll(): List<NetLogEntity>
}