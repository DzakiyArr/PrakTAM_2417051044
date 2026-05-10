package com.example.praktam2_2417051044.data.repository
import com.example.praktam2_2417051044.data.api.RetrofitClient
import com.example.praktam2_2417051044.data.model.StudySession

class StudyRepository {
    suspend fun getSessions(): List<StudySession>{
        return try {
            RetrofitClient.instance.getSessions()
        } catch (e: Exception){
            emptyList()
        }
    }
}