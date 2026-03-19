package com.example.praktam2_2417051044

import model.StudySession
import model.StudySessionSource
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.praktam2_2417051044.ui.theme.PrakTAM2_2417051044Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PrakTAM2_2417051044Theme {
                DaftarBelajarScreen()
            }
        }
    }
}

@Composable
fun DaftarBelajarScreen() {

    val sessions = StudySessionSource.dummySessions

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Text(
            text = "KonsistenBelajar",
            style = MaterialTheme.typography.headlineMedium
        )

        sessions.forEach { session ->
            DetailBelajarScreen(session)
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}

@Composable
fun DetailBelajarScreen(session: StudySession) {

    var isDone by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Image(
            painter = painterResource(id = session.imageRes),
            contentDescription = session.nama,
            modifier = Modifier.size(120.dp)
        )

        Text(
            text = session.nama,
            fontWeight = FontWeight.Bold
        )

        Text(text = session.deskripsi)
        Text(text = "Durasi: ${session.Durasi} jam")

        Button(
            onClick = { isDone = !isDone },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isDone) Color.Green else Color.Red
            )
        ) {
            Text(
                text = if (isDone) "Sudah Selesai" else "Done"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DaftarBelajarPreview() {
    PrakTAM2_2417051044Theme {
        DaftarBelajarScreen()
    }
}