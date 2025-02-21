package com.example.infoquizapp.data.theory

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TheoryDao {
    @Query("SELECT * FROM theory WHERE id = :id LIMIT 1")
    suspend fun getTheoryById(id: Int): TheoryEntity?

    @Query("SELECT * FROM theory")
    suspend fun getAllTheory(): List<TheoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTheory(theory: TheoryEntity)

    @Query("UPDATE theory SET isRead = :read WHERE id = :id")
    suspend fun updateReadStatus(id: Int, read: Boolean)
}