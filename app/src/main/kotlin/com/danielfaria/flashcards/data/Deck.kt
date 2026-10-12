package com.danielfaria.flashcards.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "decks")
data class Deck(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String
)

data class DeckWithCount(
    val id: Long,
    val name: String,
    val cardCount: Int
)
