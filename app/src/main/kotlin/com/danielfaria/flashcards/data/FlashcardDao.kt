package com.danielfaria.flashcards.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {


    @Query(
        """
        SELECT d.id, d.name, COUNT(c.id) AS cardCount
        FROM decks d
        LEFT JOIN cards c ON c.deckId = d.id
        GROUP BY d.id
        ORDER BY d.name
        """
    )
    fun getDecksWithCount(): Flow<List<DeckWithCount>>

    @Query("SELECT * FROM decks WHERE id = :deckId")
    fun getDeck(deckId: Long): Flow<Deck?>

    @Insert
    suspend fun insertDeck(deck: Deck): Long

    @Query("DELETE FROM decks WHERE id = :deckId")
    suspend fun deleteDeck(deckId: Long)

    @Query("SELECT * FROM cards WHERE deckId = :deckId ORDER BY id")
    fun getCards(deckId: Long): Flow<List<Flashcard>>

    @Query("SELECT * FROM cards WHERE deckId = :deckId")
    suspend fun getCardsOnce(deckId: Long): List<Flashcard>

    @Insert
    suspend fun insertCard(card: Flashcard)

    @Delete
    suspend fun deleteCard(card: Flashcard)
}
