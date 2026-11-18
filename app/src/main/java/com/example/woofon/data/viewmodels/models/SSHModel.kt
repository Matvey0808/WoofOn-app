package com.example.woofon.data.viewmodels.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ssh")
data class SSHModel(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val username: String,
    @ColumnInfo(name = "device_name") val deviceName: String,
    val host: String,
    val port: String,
    @ColumnInfo(name = "private_key") val privateKey: String?,
    var password: String?
)