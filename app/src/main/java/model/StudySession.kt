package model
import androidx.annotation.DrawableRes

data class StudySession(
    val nama: String,
    val deskripsi: String,
    val currentStreak: Int,
    val durasiHariIni: Int,
    @DrawableRes val imageRes: Int
)
