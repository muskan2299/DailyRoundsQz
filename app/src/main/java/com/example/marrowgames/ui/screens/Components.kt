package com.example.marrowgames.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QuizProgressBar(current: Int, total: Int) {
    Column {
        LinearProgressIndicator(
            progress = current / total.toFloat(),
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Question $current of $total",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
fun OptionCard(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    showAnswer: Boolean,
    onClick: () -> Unit
) {
    val bgColor = when {
        showAnswer && isCorrect -> MaterialTheme.colorScheme.primaryContainer
        showAnswer && isSelected && !isCorrect -> MaterialTheme.colorScheme.errorContainer
        isSelected -> MaterialTheme.colorScheme.secondaryContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(enabled = !showAnswer) { onClick() },
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(20.dp)
        )
    }
}

@Composable
fun StreakBadge(streak: Int) {
    if (streak >= 3) {
        // Animate scale or glow for effect
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(56.dp)
                .padding(8.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text("🔥 $streak", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}
