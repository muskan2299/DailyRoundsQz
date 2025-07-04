package com.example.marrowgames.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.marrowgames.data.QuestionApiService
import com.example.marrowgames.data.QuizRepository
import com.example.marrowgames.ui.screens.QuestionsScreen
import com.example.marrowgames.ui.screens.ResultsScreen
import com.example.marrowgames.ui.screens.SplashScreen
import com.example.marrowgames.viewmodel.QuizViewModel
import com.example.marrowgames.viewmodel.QuizViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.marrowgames.ui.theme.MarrowGamesTheme
import com.example.marrowgames.viewmodel.ThemeViewModel
import com.example.marrowgames.viewmodel.ThemeViewModelFactory

@Composable
fun QuizApp() {
    // Initialize Retrofit for API calls
    val retrofit = Retrofit.Builder()
        .baseUrl("https://gist.githubusercontent.com/dr-samrat/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(QuestionApiService::class.java)
    val repository = QuizRepository(apiService)
    val viewModel: QuizViewModel = viewModel(factory = QuizViewModelFactory(repository))

    val context = LocalContext.current
    val themeViewModel: ThemeViewModel = viewModel(
        factory = ThemeViewModelFactory(context)
    )
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

    MarrowGamesTheme(darkTheme = isDarkTheme) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(top = 32.dp, start = 24.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ThemeSwitchWithIcon(themeViewModel)
            }
            // Main navigation host
            val navController = rememberNavController()
            NavHost(navController, startDestination = "splash") {
                composable("splash") { SplashScreen(navController, viewModel) }
                composable("quiz") { QuestionsScreen(navController, viewModel) }
                composable("results") { ResultsScreen(navController, viewModel) }
            }
        }
    }
}

@Composable
fun ThemeSwitchWithIcon(viewModel: ThemeViewModel) {
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()
    val context = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Switch(
            checked = isDarkTheme,
            onCheckedChange = { checked -> viewModel.setTheme(context, checked) },
            thumbContent = {
                val icon: ImageVector = if (isDarkTheme) {
                    Icons.Filled.DarkMode // Moon icon for dark mode
                } else {
                    Icons.Filled.LightMode // Sun icon for light mode
                }
                Icon(
                    imageVector = icon,
                    contentDescription = if (isDarkTheme) "Dark Mode" else "Light Mode",
                    modifier = Modifier.padding(2.dp)
                )
            }
        )
    }
}


