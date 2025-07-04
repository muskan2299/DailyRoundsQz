package com.example.marrowgames.data

import com.example.marrowgames.data.model.Question

class QuizRepository(private val api: QuestionApiService) {
    suspend fun fetchQuestions(): List<Question> {
        return api.getQuestions()
    }
}