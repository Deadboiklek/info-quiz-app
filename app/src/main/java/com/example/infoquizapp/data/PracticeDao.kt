package com.example.infoquizapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.infoquizapp.data.practice.PracticeEntity

@Dao
interface PracticeDao {

    @Query("SELECT * FROM practice WHERE id = :id LIMIT 1")
    suspend fun getPracticeById(id: Int): PracticeEntity

    @Query("SELECT * FROM practice")
    suspend fun getAllPractice(): List<PracticeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPractice(practice: PracticeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPractice(practice: List<PracticeEntity>)

    @Query("UPDATE practice SET isDone = :done WHERE id = :id")
    suspend fun updatePracticeStatus(id: Int, done: Boolean)

    @Query("SELECT COUNT(*) FROM practice")
    suspend fun getPracticeCount(): Int

    @Query("DELETE FROM practice")
    suspend fun clearTable()

    @Query("DELETE FROM practice WHERE id = :id")
    suspend fun deletePracticeById(id: Int)
}