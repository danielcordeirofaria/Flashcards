package com.danielfaria.flashcards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.danielfaria.flashcards.ui.DeckDetailScreen
import com.danielfaria.flashcards.ui.DeckListScreen
import com.danielfaria.flashcards.ui.ResultScreen
import com.danielfaria.flashcards.ui.StudyScreen
import com.danielfaria.flashcards.ui.theme.FlashcardsTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlashcardsTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Box(modifier = Modifier.safeDrawingPadding()) {
                        FlashcardsApp()
                    }
                }
            }
        }
    }
}


@Composable
fun FlashcardsApp() {
    val navController = rememberNavController()
    val viewModel: FlashcardViewModel = viewModel()
    val deckIdArg = listOf(navArgument("deckId") { type = NavType.LongType })

    NavHost(navController = navController, startDestination = "decks") {

        composable("decks") {
            DeckListScreen(
                viewModel = viewModel,
                onOpenDeck = { deckId -> navController.navigate("deck/$deckId") }
            )
        }

        composable("deck/{deckId}", arguments = deckIdArg) { entry ->
            val deckId = entry.arguments?.getLong("deckId") ?: 0L
            DeckDetailScreen(
                viewModel = viewModel,
                deckId = deckId,
                onStudy = { navController.navigate("study/$deckId") },
                onBack = { navController.popBackStack() }
            )
        }

        composable("study/{deckId}", arguments = deckIdArg) { entry ->
            val deckId = entry.arguments?.getLong("deckId") ?: 0L
            StudyScreen(
                viewModel = viewModel,
                deckId = deckId,
                onClose = { navController.popBackStack() },
                onFinished = {
                    navController.navigate("result/$deckId") {
                        popUpTo("study/{deckId}") { inclusive = true }
                    }
                }
            )
        }

        composable("result/{deckId}", arguments = deckIdArg) { entry ->
            val deckId = entry.arguments?.getLong("deckId") ?: 0L
            ResultScreen(
                viewModel = viewModel,
                onStudyAgain = {
                    navController.navigate("study/$deckId") {
                        popUpTo("result/{deckId}") { inclusive = true }
                    }
                },
                onBackToDecks = { navController.popBackStack("decks", inclusive = false) }
            )
        }
    }
}
