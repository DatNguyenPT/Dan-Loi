package com.danny.vision

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.danny.vision.models.Audio
import com.danny.vision.pages.AudioPage
import com.danny.vision.viewmodels.AudioViewModel
import com.danny.vision.viewmodels.LoginViewModel
import com.danny.vision.pages.PlaylistScreen
import com.google.gson.Gson

@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    val viewModel: AudioViewModel = viewModel()
    val loginViewModel: LoginViewModel = viewModel()

    NavHost(navController = navController, startDestination = "playlist") {
        composable("playlist") {
            PlaylistScreen(viewModel, navController)
        }
        composable(
            "audioPage/{audio}",
            arguments = listOf(navArgument("audio") { type = NavType.StringType })
        ) { backStackEntry ->
            val json = backStackEntry.arguments?.getString("audio")
            val audio = Gson().fromJson(json, Audio::class.java)
            AudioPage(
                audio = audio,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}


