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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.praktam2_2417051044.data.model.StudySession
import com.example.praktam2_2417051044.data.repository.StudyRepository
import com.example.praktam2_2417051044.ui.theme.PrakTAM2_2417051044Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PrakTAM2_2417051044Theme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var selectedMenu by remember { mutableIntStateOf(0) }

    var sessions by remember { mutableStateOf<List<StudySession>>(emptyList()) }

    val repository = remember { StudyRepository() }

    LaunchedEffect(Unit) {
        sessions = repository.getSessions()
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedMenu == 0,
                    onClick = { selectedMenu = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = selectedMenu == 1,
                    onClick = { selectedMenu = 1 },
                    icon = { Icon(Icons.Default.Add, contentDescription = "Tambah") },
                    label = { Text("Tambah") }
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (selectedMenu) {

                0 -> DaftarBelajarScreen(
                    sessions = sessions,
                    onDelete = { session ->
                        sessions = sessions.filter { it.id != session.id }
                    },
                    onEdit = { updatedSession ->
                        sessions = sessions.map {
                            if (it.id == updatedSession.id) updatedSession else it
                        }
                    }
                )

                1 -> FormTambahScreen(
                    onTambah = { newSession ->
                        sessions = sessions + newSession

                        selectedMenu = 0
                    }
                )
            }
        }
    }
}

@Composable
fun DaftarBelajarScreen(
    sessions: List<StudySession>,
    onDelete: (StudySession) -> Unit,
    onEdit: (StudySession) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val totalJam = getTotalJam(sessions)

    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                SummaryCard(totalJam)
            }

            item {
                Text("Rekomendasi Belajar", fontWeight = FontWeight.Bold)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(sessions) {
                        StudyRowItem(it)
                    }
                }
            }

            item {
                Text("Semua Aktivitas", fontWeight = FontWeight.Bold)
            }

            items(sessions) {
                DetailBelajarScreen(
                    session = it,
                    onDelete = onDelete,
                    onEdit = onEdit
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun SummaryCard(totalJam: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF6C63FF)
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Total Belajar", color = Color.White.copy(alpha = 0.7f))

            Text(
                totalJam,
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun StudyRowItem(session: StudySession) {
    val jam = session.durasi / 60
    val menit = session.durasi % 60
    Card(
        modifier = Modifier.width(160.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column {
            AsyncImage(
                model = session.imageUrl,
                contentDescription = session.nama,
                modifier = Modifier
                    .height(110.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    session.nama,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    "${jam}j ${menit}m",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun DetailBelajarScreen(session: StudySession,
                        onDelete: (StudySession) -> Unit,
                        onEdit: (StudySession) -> Unit) {
    var isEditing by remember { mutableStateOf(false) }
    var nama by remember { mutableStateOf(session.nama) }
    var deskripsi by remember { mutableStateOf(session.deskripsi) }
    var durasi by remember { mutableStateOf(session.durasi.toString()) }

    Card(shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column {
            AsyncImage(
                model = session.imageUrl,
                contentDescription = session.nama,
                modifier = Modifier.height(180.dp).fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {

                if (isEditing) {
                    OutlinedTextField(nama, { nama = it }, label = { Text("Nama") })
                    OutlinedTextField(deskripsi, { deskripsi = it }, label = { Text("Deskripsi") })
                    OutlinedTextField(durasi, { durasi = it }, label = { Text("Durasi") })

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = {
                            val updated = session.copy(
                                nama = nama,
                                deskripsi = deskripsi,
                                durasi = durasi.toInt()
                            )
                            onEdit(updated)
                            isEditing = false
                        }) {
                            Text("Save")
                        }

                        OutlinedButton(onClick = { isEditing = false }) {
                            Text("Cancel")
                        }
                    }

                } else {

                    Text(session.nama, fontWeight = FontWeight.Bold)
                    Text(session.deskripsi)

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                        Button(
                            onClick = { isEditing = true }
                        ) {
                            Text("Edit")
                        }

                        Button(
                            onClick = { onDelete(session) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("Delete")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FormTambahScreen(
    onTambah: (StudySession) -> Unit
) {
    var nama by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var durasi by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Tambah Aktivitas", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = nama,
            onValueChange = { nama = it },
            label = { Text("Nama") }
        )

        OutlinedTextField(
            value = deskripsi,
            onValueChange = { deskripsi = it },
            label = { Text("Deskripsi") }
        )

        OutlinedTextField(
            value = durasi,
            onValueChange = { durasi = it },
            label = { Text("Durasi Menit") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (nama.isNotBlank() && durasi.isNotBlank()) {

                    val newSession = StudySession(
                        id = System.currentTimeMillis().toInt(),
                        nama = nama,
                        deskripsi = deskripsi,
                        durasi = durasi.toInt(),
                        imageUrl = "https://picsum.photos/200"
                    )

                    onTambah(newSession)

                    nama = ""
                    deskripsi = ""
                    durasi = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simpan")
        }
    }
}

fun getTotalJam(sessions: List<StudySession>): String {
    val totalMenit = sessions.sumOf { it.durasi }

    val jam = totalMenit / 60
    val menit = totalMenit % 60

    return "${jam}j ${menit}m"
}