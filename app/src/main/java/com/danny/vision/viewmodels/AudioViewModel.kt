package com.danny.vision.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.danny.vision.models.Audio

class AudioViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext
    private val player: ExoPlayer = ExoPlayer.Builder(context).build()

    val playlist = mutableStateOf(
        listOf(
            Audio("1", "Audio One", "Artist A", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"),
            Audio("2", "Audio Two", "Artist B", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3"),
            Audio("3", "Audio Three", "Artist C", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3")
        )
    )

    private val _currentAudio = mutableStateOf<Audio?>(null)
    val currentAudio: State<Audio?> = _currentAudio

    private val _isPlaying = mutableStateOf(false)
    val isPlaying: State<Boolean> = _isPlaying

    private val _currentPosition = mutableStateOf(0L)
    val currentPosition: State<Long> = _currentPosition

    private val _duration = mutableStateOf(0L)
    val duration: State<Long> = _duration

    private var progressJob: Job? = null

    fun playAudio(audio: Audio) {
        if (_currentAudio.value?.id != audio.id) {
            player.setMediaItem(MediaItem.fromUri(Uri.parse(audio.uri)))
            player.prepare()
            _currentAudio.value = audio
        }
        player.play()
        _isPlaying.value = true
        startProgressUpdater()
    }

    fun pauseAudio() {
        player.pause()
        _isPlaying.value = false
        stopProgressUpdater()
    }

    fun seekTo(positionMs: Long) {
        player.seekTo(positionMs)
        _currentPosition.value = positionMs
    }

    private fun startProgressUpdater() {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            while (player.isPlaying) {
                _currentPosition.value = player.currentPosition
                _duration.value = player.duration.coerceAtLeast(0L)
                delay(500L)
            }
        }
    }

    private fun stopProgressUpdater() {
        progressJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
        stopProgressUpdater()
    }
}
