package com.example.marrowgames.data

import com.example.marrowgames.data.model.Question
import retrofit2.http.GET

interface QuestionApiService {
    @GET("53846277a8fcb034e482906ccc0d12b2/raw")
    suspend fun getQuestions(): List<Question>
}