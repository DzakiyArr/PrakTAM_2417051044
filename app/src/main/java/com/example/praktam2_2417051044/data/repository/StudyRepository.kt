package com.example.praktam2_2417051044.data.repository
import com.example.praktam2_2417051044.data.api.RetrofitClient
import com.example.praktam2_2417051044.data.model.StudySession

class StudyRepository {
    suspend fun getSessions(): List<StudySession>{
        return try {
            RetrofitClient.instance.getSessions().mapIndexed { index, item ->
                item.copy(id = index + 1)
            }
        } catch (e: Exception){
            emptyList()
        }
    }
}