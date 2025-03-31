package com.example.infoquizapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.infoquizapp.data.theory.TheoryEntity

@Dao
interface TheoryDao {
    @Query("SELECT * FROM theory WHERE id = :id LIMIT 1")
    suspend fun getTheoryById(id: Int): TheoryEntity?

    @Query("SELECT * FROM theory")
    suspend fun getAllTheory(): List<TheoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTheory(theory: TheoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTheory(theory: List<TheoryEntity>)

    @Query("UPDATE theory SET isRead = :read WHERE id = :id")
    suspend fun updateReadStatus(id: Int, read: Boolean)

    @Query("SELECT COUNT(*) FROM theory")
    suspend fun getTheoryCount(): Int

    // Удалить все записи
    @Query("DELETE FROM theory")
    suspend fun clearTable()

    // Удалить конкретную запись по id
    @Query("DELETE FROM theory WHERE id = :id")
    suspend fun deleteTheoryById(id: Int)
}