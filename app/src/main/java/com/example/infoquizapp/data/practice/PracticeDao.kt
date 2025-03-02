package com.example.infoquizapp.data.practice

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.infoquizapp.data.theory.TheoryEntity

@Dao
interface PracticeDao {

    @Query("SELECT * FROM practice WHERE id = :id LIMIT 1")
    suspend fun getPracticeById(id: Int): PracticeEntity

    @Query("SELECT * FROM practice")
    suspend fun getAllPractice(): List<PracticeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPractice(practice: PracticeEntity)

    @Query("UPDATE practice SET isDone = :done WHERE id = :id")
    suspend fun updatePracticeStatus(id: Int, done: Boolean)
}