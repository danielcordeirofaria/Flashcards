package com.danielfaria.flashcards.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danielfaria.flashcards.FlashcardViewModel
import com.danielfaria.flashcards.ui.theme.RightGreen
import com.danielfaria.flashcards.ui.theme.WrongRed


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyScreen(
    viewModel: FlashcardViewModel,
    deckId: Long,
    onFinished: () -> Unit,
    onClose: () -> Unit
) {
    LaunchedEffect(deckId) {
        viewModel.startStudy(deckId)
    }

    if (!viewModel.studyReady) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val index = viewModel.currentIndex
    val card = viewModel.studyCards.getOrNull(index) ?: return
    val total = viewModel.studyCards.size

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Study", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = "Stop studying")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {
            Text(
                "Card ${index + 1} of $total",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { index.toFloat() / total },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(Modifier.height(32.dp))


            key(index) {
                FlipCard(
                    front = card.front,
                    back = card.back,
                    showAnswer = viewModel.showAnswer,
                    onClick = { viewModel.flipCard() }
                )
            }

            Spacer(Modifier.height(32.dp))

            if (!viewModel.showAnswer) {
                Text(
                    "Tap the card to see the answer",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AnswerButton(
                        text = "Didn't know",
                        color = WrongRed,
                        knewIt = false,
                        modifier = Modifier.weight(1f),
                        onClick = { answerAndCheck(viewModel, false, onFinished) }
                    )
                    AnswerButton(
                        text = "Knew it",
                        color = RightGreen,
                        knewIt = true,
                        modifier = Modifier.weight(1f),
                        onClick = { answerAndCheck(viewModel, true, onFinished) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FlipCard(front: String, back: String, showAnswer: Boolean, onClick: () -> Unit) {
    val rotation by animateFloatAsState(
        targetValue = if (showAnswer) 180f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "flip"
    )
    // after half of the turn we start showing the back side
    val showingBack = rotation > 90f
    val shape = RoundedCornerShape(24.dp)
    val colors = MaterialTheme.colorScheme

    Card(
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = if (showingBack) colors.secondaryContainer else colors.primaryContainer,
            contentColor = if (showingBack) colors.onSecondaryContainer else colors.onPrimaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .clip(shape)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                // turn the text again on the back side, or it would look mirrored
                .graphicsLayer { if (showingBack) rotationY = 180f },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                if (showingBack) "ANSWER" else "QUESTION",
                style = MaterialTheme.typography.labelLarge,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(16.dp))
            Text(
                if (showingBack) back else front,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun AnswerButton(
    text: String,
    color: Color,
    knewIt: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color, contentColor = Color.White),
        modifier = modifier.height(56.dp)
    ) {
        Icon(if (knewIt) Icons.Default.Check else Icons.Default.Close, contentDescription = null)
        Spacer(Modifier.width(6.dp))
        Text(text, style = MaterialTheme.typography.titleSmall)
    }
}

private fun answerAndCheck(viewModel: FlashcardViewModel, knewIt: Boolean, onFinished: () -> Unit) {
    viewModel.answer(knewIt)
    if (viewModel.isStudyFinished) {
        onFinished()
    }
}
