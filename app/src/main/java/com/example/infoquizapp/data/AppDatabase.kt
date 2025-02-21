package com.example.infoquizapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.infoquizapp.data.theory.TheoryDao
import com.example.infoquizapp.data.theory.TheoryEntity

@Database(
    entities = [TheoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun theoryDao(): TheoryDao
}