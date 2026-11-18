package com.example.woofon.data.viewmodels.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.woofon.data.viewmodels.db.dao.SSHDao
import com.example.woofon.data.viewmodels.models.SSHModel

@Database(
    version = 1,
    entities = [(SSHModel::class)]
)
abstract class SSHDataBase : RoomDatabase() {
    abstract fun getSSHDao() : SSHDao

    companion object {
        @Volatile
        private var INSTANCE: SSHDataBase? = null

        fun getInstance(context: Context): SSHDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SSHDataBase::class.java,
                    "ssh"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}