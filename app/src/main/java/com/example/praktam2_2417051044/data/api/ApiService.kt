package com.example.praktam2_2417051044.data.api

import com.example.praktam2_2417051044.data.model.StudySession
import retrofit2.http.GET

interface ApiService {
    @GET("studysession.json")
    suspend fun getSessions(): List<StudySession>
}