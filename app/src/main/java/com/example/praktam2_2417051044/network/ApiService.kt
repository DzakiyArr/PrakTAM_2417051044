package com.example.praktam2_2417051044.network

import com.example.praktam2_2417051044.model.StudySession
import retrofit2.http.GET

interface ApiService {
    @GET("sesi_belajar.json")
    suspend fun getSessions(): List<StudySession>
}