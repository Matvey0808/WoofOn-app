package com.example.woofon.data.viewmodels.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.woofon.data.viewmodels.db.dao.DeviceDao
import com.example.woofon.data.viewmodels.models.DeviceModel

@Database(
    version = 1,
    entities = [(DeviceModel::class)]
)
abstract class DeviceDatabase : RoomDatabase() {
    abstract fun getDeviceDao() : DeviceDao

    companion object {
        @Volatile
        private var INSTANCE: DeviceDatabase? = null
        fun getInstance(context: Context): DeviceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DeviceDatabase::class.java,
                    "devices"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}