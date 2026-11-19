package com.woofon.data.viewmodels.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.woofon.data.viewmodels.models.SSHModel
import kotlinx.coroutines.flow.Flow

@Dao
interface SSHDao {
    @Query("SELECT * FROM ssh")
    fun getSSH(): Flow<List<SSHModel>>

    @Insert
    suspend fun addSSH(ssh: SSHModel)

    @Query("UPDATE ssh SET password = :newPassword WHERE id = :sshId")
    suspend fun updateSSHPassword(sshId: Int, newPassword: String)

    @Update
    suspend fun updateSSH(ssh: SSHModel)

    @Delete
    suspend fun deleteSSH(device: List<SSHModel>)
}