package com.example.marrowgames.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.marrowgames.R
import com.example.marrowgames.ui.theme.HighlightGreen
import com.example.marrowgames.ui.theme.HighlightRed
import com.example.marrowgames.viewmodel.QuizViewModel
import com.example.marrowgames.viewmodel.ThemeViewModel

@Composable
fun QuestionsScreen(
    navController: NavController,
    viewModel: QuizViewModel
) {
    val state by viewModel.uiState.collectAsState()
    val timeLeft by viewModel.timeLeft.collectAsState()
    val showStreakAnimation = remember(state.streak) {
        mutableStateOf(state.streak == 3)
    }

    LaunchedEffect(Unit) {
        viewModel.startGlobalTimer()
    }

    // Reset animation trigger if streak drops below 3
    LaunchedEffect(state.streak) {
        if (state.streak == 3) {
            showStreakAnimation.value = true
        } else {
            showStreakAnimation.value = false
        }
    }

    when {
        state.isLoading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        state.error != null -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: ${state.error}")
            }
        }
        state.showResult -> {
            LaunchedEffect(Unit) {
                navController.navigate("results") { popUpTo("quiz") { inclusive = true } }
            }
        }
        else -> {
            val question = state.questions.getOrNull(state.currentIndex) ?: return

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .pointerInput(state.currentIndex, state.showAnswer) {
                        detectHorizontalDragGestures { _, dragAmount ->
                            // Only allow navigation if answer is not being shown
                            if (!state.showAnswer) {
                                if (dragAmount < -50 && state.currentIndex < state.questions.size - 1) {
                                    // Swipe left: Next question
                                    viewModel.goToNextQuestion()
                                } else if (dragAmount > 50 && state.currentIndex > 0) {
                                    // Swipe right: Previous question
                                    viewModel.goToPreviousQuestion()
                                }
                            }
                        }
                    }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    QuizTimer(timeLeft = timeLeft)
                    // Progress Bar and Counter
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = (state.currentIndex) / state.questions.size.toFloat(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Question ${state.currentIndex + 1} of ${state.questions.size}",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.align(Alignment.End),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(24.dp))

                    // Question Text
                    Text(
                        question.question,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 24.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Options
                    question.options.forEachIndexed { idx, option ->
                        val isSelected = state.selectedOption == idx
                        val isCorrect = question.correctOptionIndex == idx
                        val showAnswer = state.showAnswer
                        val bgColor = when {
                            showAnswer && isCorrect -> HighlightGreen
                            showAnswer && isSelected && !isCorrect -> HighlightRed
                            isSelected -> MaterialTheme.colorScheme.secondaryContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clip(MaterialTheme.shapes.medium)
                                .clickable(enabled = !showAnswer) { viewModel.selectOption(idx) },
                            colors = CardDefaults.cardColors(containerColor = bgColor)
                        ) {
                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    // Skip Button
                    Button(
                        onClick = { viewModel.skipQuestion() },
                        enabled = !state.showAnswer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Skip")
                    }

                    Spacer(Modifier.height(24.dp))
                    StreakIndicator(streak = state.streak)
                }
                if (showStreakAnimation.value) {
                    StreakAnimation(
                        onAnimationFinished = { showStreakAnimation.value = false }
                    )
                }

                // Fixed navigation arrows at the bottom
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(bottom = 32.dp, start = 48.dp, end = 48.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircleIconButton(
                        onClick = { if (state.currentIndex > 0) viewModel.goToPreviousQuestion() },
                        enabled = state.currentIndex > 0 && !state.showAnswer,
                        icon = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Previous Question"
                    )
                    Button(
                        onClick = { viewModel.finishTest() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Finish Test")
                    }
                    CircleIconButton(
                        onClick = { if (state.currentIndex < state.questions.size - 1) viewModel.goToNextQuestion() },
                        enabled = (state.currentIndex < state.questions.size - 1) && !state.showAnswer,
                        icon = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next Question"
                    )
                }
            }
        }
    }
}

@Composable
fun StreakIndicator(streak: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Fire icons (lit or gray)
        for (i in 1..3) {
            val isLit = streak >= i
            Icon(
                painter = painterResource(id = R.drawable.fire),
                contentDescription = "Fire Icon",
                tint = if (isLit) Color.Unspecified else Color.Gray,
                modifier = Modifier.size(48.dp)
            )
        }
        // "+N" badge for streaks > 3
        if (streak > 3) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+${streak - 3}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun StreakAnimation(onAnimationFinished: () -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.streak))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
        speed = 1f,
        isPlaying = true,
        restartOnPlay = false
    )

    // When the animation finishes, call the callback to hide it
    if (progress == 1f) {
        LaunchedEffect(Unit) {
            onAnimationFinished()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(300.dp)
        )
    }
}

@Composable
fun CircleIconButton(
    onClick: () -> Unit,
    enabled: Boolean,
    icon: ImageVector,
    contentDescription: String
) {
    Surface(
        shape = CircleShape,
        color = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        shadowElevation = 4.dp,
        modifier = Modifier.size(32.dp)
    ) {
        IconButton(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = if (enabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun QuizTimer(timeLeft: Int) {
    val minutes = timeLeft / 60
    val seconds = timeLeft % 60
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Timer,
                contentDescription = "Timer",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = String.format("%02d:%02d", minutes, seconds),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
