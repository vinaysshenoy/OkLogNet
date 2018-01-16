package com.vinaysshenoy.oklognet.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

/**
 * Created by vinaysshenoy on 16/01/18.
 */
@Database(entities = [NetLogEntity::class], version = 1)
internal abstract class OkLogNetDb : RoomDatabase() {

    abstract val netLogDao: NetLogDao
}