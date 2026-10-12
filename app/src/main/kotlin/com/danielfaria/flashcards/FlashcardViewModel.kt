package com.danielfaria.flashcards

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.danielfaria.flashcards.data.AppDatabase
import com.danielfaria.flashcards.data.Deck
import com.danielfaria.flashcards.data.DeckWithCount
import com.danielfaria.flashcards.data.Flashcard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FlashcardViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).flashcardDao()

    val decks: Flow<List<DeckWithCount>> = dao.getDecksWithCount()

    var studyCards by mutableStateOf<List<Flashcard>>(emptyList())
        private set
    var currentIndex by mutableIntStateOf(0)
        private set
    var showAnswer by mutableStateOf(false)
        private set
    var rightAnswers by mutableIntStateOf(0)
        private set
    var studyReady by mutableStateOf(false)
        private set

    val isStudyFinished: Boolean
        get() = currentIndex >= studyCards.size


    fun addDeck(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            dao.insertDeck(Deck(name = name.trim()))
        }
    }

    fun deleteDeck(deckId: Long) {
        viewModelScope.launch {
            dao.deleteDeck(deckId)
        }
    }

    fun getDeck(deckId: Long): Flow<Deck?> = dao.getDeck(deckId)

    fun getCards(deckId: Long): Flow<List<Flashcard>> = dao.getCards(deckId)

    fun addCard(deckId: Long, front: String, back: String) {
        if (front.isBlank() || back.isBlank()) return
        viewModelScope.launch {
            dao.insertCard(Flashcard(deckId = deckId, front = front.trim(), back = back.trim()))
        }
    }

    fun deleteCard(card: Flashcard) {
        viewModelScope.launch {
            dao.deleteCard(card)
        }
    }

    fun startStudy(deckId: Long) {
        studyReady = false
        viewModelScope.launch {
            studyCards = dao.getCardsOnce(deckId).shuffled()
            currentIndex = 0
            rightAnswers = 0
            showAnswer = false
            studyReady = true
        }
    }

    fun flipCard() {
        showAnswer = !showAnswer
    }

    fun answer(knewIt: Boolean) {
        if (knewIt) rightAnswers++
        currentIndex++
        showAnswer = false
    }
}
