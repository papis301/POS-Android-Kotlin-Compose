package com.pisco.stockmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.pisco.stockmanager.data.preferences.AppSettings
import com.pisco.stockmanager.data.preferences.AppThemeMode
import com.pisco.stockmanager.data.preferences.SettingsRepository
import com.pisco.stockmanager.navigation.AppNavGraph
import com.pisco.stockmanager.ui.theme.StockManagerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val settings by settingsRepository.settings.collectAsState(
                initial = AppSettings()
            )

            val systemDarkTheme = isSystemInDarkTheme()

            val isDarkTheme = when (settings.themeMode) {
                AppThemeMode.LIGHT -> false
                AppThemeMode.DARK -> true
                AppThemeMode.SYSTEM -> systemDarkTheme
            }

            StockManagerTheme(
                darkTheme = isDarkTheme
            ) {

                val navController =
                    rememberNavController()

                AppNavGraph(
                    navController
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StockManagerTheme {
        Greeting("Android")
    }
}