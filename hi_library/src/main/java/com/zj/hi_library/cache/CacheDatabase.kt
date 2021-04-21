package com.zj.hi_library.cache

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zj.hi_library.util.AppGlobals

@Database(entities = [Cache::class], version = 1)
abstract class CacheDatabase : RoomDatabase() {

    abstract val cacheDao: CacheDao

    companion object {
        private var database: CacheDatabase

        fun getDatabase(): CacheDatabase {
            return database
        }

        init {
            val context = AppGlobals.get()!!.applicationContext
            database = Room.databaseBuilder(context, CacheDatabase::class.java, "howow_cache")
                .build()
        }

    }
}