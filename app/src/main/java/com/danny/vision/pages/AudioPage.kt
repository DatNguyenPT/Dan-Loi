package com.danny.vision.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.danny.vision.models.Audio
import com.danny.vision.viewmodels.AudioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioPage(
    audio: Audio?,
    viewModel: AudioViewModel,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(audio?.title ?: "Audio") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (audio != null) {
                val isPlaying = viewModel.isPlaying.value
                val currentAudio = viewModel.currentAudio.value
                val currentPosition = viewModel.currentPosition.value
                val duration = viewModel.duration.value

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = audio.title, style = MaterialTheme.typography.headlineMedium)
                    Text(text = audio.artist, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(24.dp))

                    Slider(
                        value = if (duration > 0) currentPosition.toFloat() / duration else 0f,
                        onValueChange = { fraction ->
                            val newPosition = (fraction * duration).toLong()
                            viewModel.seekTo(newPosition)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(formatTime(currentPosition))
                        Text(formatTime(duration))
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    if (isPlaying && currentAudio?.id == audio.id) {
                        Button(onClick = { viewModel.pauseAudio() }) {
                            Text("Pause")
                        }
                    } else {
                        Button(onClick = { viewModel.playAudio(audio) }) {
                            Text("Play")
                        }
                    }
                }
            } else {
                Text("Audio not found")
            }
        }
    }
}

fun formatTime(millis: Long): String {
    val totalSeconds = millis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}
