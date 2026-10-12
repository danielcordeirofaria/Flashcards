# Flashcards App

# Overview

I work mostly with Java on the backend, and I had never built a mobile app before. I made this project to learn how Android apps work: how to build screens, how to move between them, and how to save data on the phone.

Flashcards is an Android app, written in Kotlin, that helps you study with digital cards. You create decks (for example "English verbs" or "SQL commands"), add cards with a question on the front and the answer on the back, and then study them. In study mode the cards come in a random order. You tap a card to flip it and then say if you knew the answer or not. At the end the app shows your score.

The app has four screens:
- **My Decks**: list of all decks, with how many cards each one has. You can create and delete decks here.
- **Deck details**: the cards in a deck. You can add and delete cards and start studying.
- **Study**: one card at a time. Tap to flip, then choose "I knew it" or "I didn't know it".
- **Result**: your final score and options to study again or go back.

All decks and cards are saved in a local SQLite database with Room, so nothing is lost when the app closes or the phone turns off.

To run the app:
1. Open the `FlashcardsApp` folder in Android Studio.
2. Wait for the Gradle sync to finish.
3. Start an emulator (or connect an Android phone with USB debugging on) and click **Run**.

[Software Demo Video](https://www.youtube.com/watch?v=ZJljnfcU9Fk)

# Development Environment

- Android Studio
- Kotlin
- Jetpack Compose (Android toolkit to build the UI with Kotlin code)
- Navigation Compose (to move between screens)
- Room (library that stores data in SQLite on the phone)
- Kotlin coroutines and Flow (to run database work without freezing the screen)
- Android Emulator
- Git and GitHub

# Useful Websites

- [Android Developers - Jetpack Compose tutorial](https://developer.android.com/develop/ui/compose/tutorial)
- [Android Developers - Save data in a local database using Room](https://developer.android.com/training/data-storage/room)
- [Android Developers - Navigation with Compose](https://developer.android.com/develop/ui/compose/navigation)
- [Android Basics with Compose (Codelabs)](https://developer.android.com/courses/android-basics-compose/course)
- [Kotlin documentation](https://kotlinlang.org/docs/home.html)

# Future Work

- Edit a card without having to delete it and create it again
- Save the score of each study session and show progress over time
- Show the cards you got wrong more often (spaced repetition)
- Add a flip animation to the cards
