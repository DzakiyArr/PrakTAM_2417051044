package com.example.praktam2_2417051044

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.praktam2_2417051044.model.StudySession
import com.example.praktam2_2417051044.network.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.praktam2_2417051044.ui.theme.PrakTAM2_2417051044Theme
import com.example.praktam2_2417051044.ui.theme.DoneGreen
import com.example.praktam2_2417051044.ui.theme.NotDoneRed

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PrakTAM2_2417051044Theme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    DaftarBelajarScreen()
                }
            }
        }
    }
}

@Composable
fun DaftarBelajarScreen() {
    var sessions by remember { mutableStateOf<List<StudySession>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        try {
            sessions = RetrofitClient.instance.getSessions()
            isLoading = false
            isError = false
        } catch (e: Exception) {
            isLoading = false
            isError = true
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (isError || sessions.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Gagal Memuat Data",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Pastikan koneksi internet Anda menyala",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().statusBarsPadding(),
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    Text(
                        text = "Rekomendasi Belajar",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(sessions) { session ->
                            StudyRowItem(session = session)
                        }
                    }
                    Spacer(modifier = Modifier.height(45.dp))
                    Text(
                        text = "Daftar Aktivitas Lengkap",
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                items(sessions) { session ->
                    DetailBelajarScreen(session = session, snackbarHostState = snackbarHostState)
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun StudyRowItem(session: StudySession) {
    Card(
        modifier = Modifier.width(160.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = session.imageUrl,
                contentDescription = session.nama,
                placeholder = painterResource(id = R.drawable.ic_launcher_background),
                error = painterResource(id = R.drawable.ic_launcher_foreground),
                modifier = Modifier.fillMaxWidth().height(100.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = session.nama,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "${session.durasi} Jam",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun DetailBelajarScreen(session: StudySession, snackbarHostState: SnackbarHostState) {
    var isDone by remember { mutableStateOf(false) }
    var isProcessLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = session.imageUrl,
                contentDescription = session.nama,
                placeholder = painterResource(id = R.drawable.ic_launcher_background),
                error = painterResource(id = R.drawable.ic_launcher_foreground),
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = session.nama,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = session.deskripsi,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (!isDone) {
                            coroutineScope.launch {
                                isProcessLoading = true
                                delay(2000)
                                isProcessLoading = false
                                isDone = true
                                snackbarHostState.showSnackbar("Aktivitas ${session.nama} selesai!")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isProcessLoading && !isDone,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDone) DoneGreen else NotDoneRed
                    )
                ) {
                    if (isProcessLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Memproses...", color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text(text = if (isDone) "Sudah Selesai" else "Tandai Selesai", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        }
    }
}