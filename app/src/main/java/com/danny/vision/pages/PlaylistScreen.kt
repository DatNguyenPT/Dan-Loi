package com.danny.vision.pages

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.danny.vision.components.ElevatedCard
import com.danny.vision.viewmodels.AudioViewModel
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen(viewModel: AudioViewModel, navController: androidx.navigation.NavHostController) {
    val playlist = viewModel.playlist.value
    val currentAudio = viewModel.currentAudio.value
    val isPlaying = viewModel.isPlaying.value

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Playlist") })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(playlist, key = { it.id }) { audio ->
                ElevatedCard(
                    audio = audio,
                    isPlaying = isPlaying && currentAudio?.id == audio.id,
                    onPlay = {
                        viewModel.playAudio(audio)
                        val json = Uri.encode(Gson().toJson(audio))
                        navController.navigate("audioPage/$json")
                    },
                    onPause = { viewModel.pauseAudio() }
                )
            }
        }
    }
}