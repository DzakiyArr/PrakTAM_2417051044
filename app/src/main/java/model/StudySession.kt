package model
import androidx.annotation.DrawableRes

data class StudySession(
    val nama: String,
    val deskripsi: String,
    val Durasi: Int,
    @DrawableRes val imageRes: Int
)
