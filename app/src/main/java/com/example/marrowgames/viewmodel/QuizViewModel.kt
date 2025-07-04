package com.example.marrowgames.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.marrowgames.data.model.Question
import com.example.marrowgames.data.QuizRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

data class QuizUiState(
    val questions: List<Question> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val currentIndex: Int = 0,
    val selectedOption: Int? = null,
    val showAnswer: Boolean = false,
    val correctCount: Int = 0,
    val skippedCount: Int = 0,
    val streak: Int = 0,
    val longestStreak: Int = 0,
    val showResult: Boolean = false
)

class QuizViewModel(private val repository: QuizRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()
    private var timerJob: Job? = null
    private val _timeLeft = MutableStateFlow(15 * 60) // 15 minutes in seconds
    val timeLeft: StateFlow<Int> = _timeLeft

    fun startGlobalTimer() {
        timerJob?.cancel()
        _timeLeft.value = 15 * 60
        timerJob = viewModelScope.launch {
            while (_timeLeft.value > 0) {
                delay(1000)
                _timeLeft.value -= 1
            }
            // Time's up: finish the test
            finishTest()
        }
    }

    fun stopGlobalTimer() {
        timerJob?.cancel()
    }

    fun finishTest() {
        stopGlobalTimer()
        _uiState.value = _uiState.value.copy(showResult = true)
    }

    init {
        fetchQuestions()
    }

    private fun fetchQuestions() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                Log.d("QuizViewModel", "Fetching questions from repository : ${repository}")
                val questions = repository.fetchQuestions()
                Log.d("QuizViewModel", "Fetched questions: ${questions.size} questions")
                _uiState.value = _uiState.value.copy(
                    questions = questions,
                    isLoading = false,
                    error = null,
                    currentIndex = 0,
                    selectedOption = null,
                    showAnswer = false,
                    correctCount = 0,
                    skippedCount = 0,
                    streak = 0,
                    longestStreak = 0,
                    showResult = false
                )
            } catch (e: Exception) {
                Log.e("QuizViewModel", "Error fetching questions $e")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to load questions"
                )
            }
        }
    }

    fun selectOption(index: Int) {
        val state = _uiState.value
        val question = state.questions.getOrNull(state.currentIndex) ?: return
        val isCorrect = question.correctOptionIndex == index
        val newStreak = if (isCorrect) state.streak + 1 else 0
        val newLongest = maxOf(state.longestStreak, newStreak)
        _uiState.value = state.copy(
            selectedOption = index,
            showAnswer = true,
            correctCount = if (isCorrect) state.correctCount + 1 else state.correctCount,
            streak = newStreak,
            longestStreak = newLongest
        )
        viewModelScope.launch {
            delay(2000)
            nextQuestion()
        }
    }

    fun skipQuestion() {
        val state = _uiState.value
        _uiState.value = state.copy(
            skippedCount = state.skippedCount + 1,
            selectedOption = null,
            showAnswer = false,
            streak = 0 // Reset streak on skip
        )
        nextQuestion()
    }

    private fun nextQuestion() {
        val state = _uiState.value
        val nextIndex = state.currentIndex + 1
        if (nextIndex >= state.questions.size) {
            _uiState.value = state.copy(showResult = true)
        } else {
            _uiState.value = state.copy(
                currentIndex = nextIndex,
                selectedOption = null,
                showAnswer = false
            )
        }
    }

    fun goToPreviousQuestion() {
        val state = _uiState.value
        if (state.currentIndex > 0) {
            _uiState.value = state.copy(
                currentIndex = state.currentIndex - 1,
                selectedOption = null,
                showAnswer = false
            )
        }
    }

    fun goToNextQuestion() {
        val state = _uiState.value
        if (state.currentIndex < state.questions.size - 1) {
            _uiState.value = state.copy(
                currentIndex = state.currentIndex + 1,
                selectedOption = null,
                showAnswer = false
            )
        }
    }

    fun restartQuiz() {
        fetchQuestions()
    }
}
