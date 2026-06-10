package com.example.praktam2_2417051044.data.model

import com.google.gson.annotations.SerializedName

data class StudySession(
    val id: Int,
    @SerializedName("nama")
    val nama: String,
    @SerializedName("deskripsi")
    val deskripsi: String,
    @SerializedName("durasi")
    val durasi: Int,
    @SerializedName("image_url")
    val imageUrl: String
)