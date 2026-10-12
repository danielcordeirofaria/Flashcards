package com.danielfaria.flashcards.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Deck::class, Flashcard::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun flashcardDao(): FlashcardDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null


        fun getDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "flashcards.db"
                )
                    .addCallback(SampleDataCallback)
                    .build()
                    .also { instance = it }
            }
        }
    }
}


private object SampleDataCallback : RoomDatabase.Callback() {

    // Inserts the sample decks and cards with plain SQL.
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        db.execSQL("INSERT INTO decks (id, name) VALUES (1, 'English Verbs')")
        db.execSQL("INSERT INTO decks (id, name) VALUES (2, 'Kotlin Basics')")
        db.execSQL("INSERT INTO decks (id, name) VALUES (3, 'SQL Commands')")

        val cards = listOf(
            Triple(1, "to go", "ir"),
            Triple(1, "to eat", "comer"),
            Triple(1, "to learn", "aprender"),
            Triple(1, "to build", "construir"),
            Triple(1, "to forget", "esquecer"),
            Triple(2, "val vs var", "val can't be changed after it's set, var can"),
            Triple(2, "What does ?. do?", "Safe call: runs only if the value is not null"),
            Triple(2, "data class", "A class made to hold data. It gets equals, toString and copy for free"),
            Triple(2, "when", "Kotlin's version of switch, but it can return a value"),
            Triple(3, "SELECT", "Reads rows from a table"),
            Triple(3, "INSERT", "Adds a new row to a table"),
            Triple(3, "LEFT JOIN", "Joins two tables and keeps rows from the left one even with no match"),
            Triple(3, "GROUP BY", "Groups rows so you can use COUNT, SUM, AVG on each group")
        )
        for ((deckId, front, back) in cards) {
            db.execSQL(
                "INSERT INTO cards (deckId, front, back) VALUES (?, ?, ?)",
                arrayOf<Any>(deckId, front, back)
            )
        }
    }
}
