package com.zj.hi_library.cache

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cache")
class Cache {
    @NonNull
    @PrimaryKey(autoGenerate = false)
    var key: String = ""

    //缓存二进制数据
    var data: ByteArray? = null
}