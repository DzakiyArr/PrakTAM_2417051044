package com.example.praktam2_2417051044

import model.StudySessionSource
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import com.example.praktam2_2417051044.ui.theme.PrakTAM2_2417051044Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PrakTAM2_2417051044Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting() {

    val session = StudySessionSource.dummySessions[0]

    Column(modifier = Modifier.fillMaxSize().padding(all = 24.dp)){
        Text(text = "Nama: ${session.nama}")
        Text(text = "Deskripsi: ${session.deskripsi}")
        Text(text = "Hari Berjalan: ${session.currentStreak} hari")
        Text(text = "Durasi hari ini: ${session.durasiHariIni} menit")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PrakTAM2_2417051044Theme {
        Greeting()
    }
}