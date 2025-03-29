package com.example.infoquizapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.infoquizapp.data.practice.PracticeEntity
import com.example.infoquizapp.data.theory.TheoryEntity

@Database(
    entities = [
        TheoryEntity::class,
        PracticeEntity::class
               ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun theoryDao(): TheoryDao
    abstract fun  practiceDao(): PracticeDao
}