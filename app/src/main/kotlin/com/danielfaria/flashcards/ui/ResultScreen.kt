package com.danielfaria.flashcards.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.danielfaria.flashcards.FlashcardViewModel
import com.danielfaria.flashcards.ui.theme.RightGreen
import com.danielfaria.flashcards.ui.theme.WrongRed

// Shown at the end of a study session with the final score.
@Composable
fun ResultScreen(
    viewModel: FlashcardViewModel,
    onStudyAgain: () -> Unit,
    onBackToDecks: () -> Unit
) {
    val total = viewModel.studyCards.size
    val right = viewModel.rightAnswers
    val percent = if (total > 0) right * 100 / total else 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // ring that fills up with the score
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = { percent / 100f },
                modifier = Modifier.size(180.dp),
                color = scoreColor(percent),
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeWidth = 14.dp,
                strokeCap = StrokeCap.Round
            )
            Text(
                "$percent%",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(32.dp))
        Text("Session finished!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(
            "You knew $right of $total cards",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(8.dp))
        Text(resultMessage(percent), style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
        Spacer(Modifier.height(40.dp))

        Button(
            onClick = onStudyAgain,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Study again", style = MaterialTheme.typography.titleMedium)
        }
        Spacer(Modifier.height(12.dp))
        OutlinedButton(
            onClick = onBackToDecks,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Back to decks", style = MaterialTheme.typography.titleMedium)
        }
    }
}

// Green for a good score, orange for medium, red for low.
@Composable
private fun scoreColor(percent: Int): Color {
    return when {
        percent >= 70 -> RightGreen
        percent >= 40 -> MaterialTheme.colorScheme.tertiary
        else -> WrongRed
    }
}

// Picks a short message based on the score.
private fun resultMessage(percent: Int): String {
    return when {
        percent == 100 -> "Perfect! You knew every card."
        percent >= 70 -> "Good job! Just a few more to review."
        percent >= 40 -> "Not bad. Try this deck again soon."
        else -> "Keep practicing, you'll get there."
    }
}
