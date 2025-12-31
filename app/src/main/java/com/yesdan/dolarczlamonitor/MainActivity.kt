package com.yesdan.dolarczlamonitor

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.yesdan.dolarczlamonitor.data.local.UserPreferencesRepository
import com.yesdan.dolarczlamonitor.data.remote.RemoteConfigRepository
import com.yesdan.dolarczlamonitor.ui.screen.BlockScreen
import com.yesdan.dolarczlamonitor.ui.screen.CitySelectionScreen
import com.yesdan.dolarczlamonitor.ui.screen.MainScreen
import com.yesdan.dolarczlamonitor.ui.screen.SplashScreen
import com.yesdan.dolarczlamonitor.ui.theme.DolarCzlaMonitorTheme
import com.yesdan.dolarczlamonitor.ui.viewmodel.HomeViewModel
import com.yesdan.dolarczlamonitor.utils.AnalyticsHelper
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

class MainActivity : ComponentActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val remoteConfigRepository = RemoteConfigRepository()
        val preferencesRepository = UserPreferencesRepository(this)
        val deviceModel = "${Build.MANUFACTURER} ${Build.MODEL}"

        setContent {
            val isDarkMode by preferencesRepository.isDarkMode.collectAsState(initial = false)
            val scope = rememberCoroutineScope()
            var currentScreen by remember { mutableStateOf<Screen>(Screen.Splash) }
            var userCity by remember { mutableStateOf<String?>(null) }

            DolarCzlaMonitorTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (currentScreen) {
                        Screen.Splash -> {
                            SplashScreen(
                                remoteConfigRepository = remoteConfigRepository,
                                preferencesRepository = preferencesRepository,
                                onNavigateToBlockScreen = {
                                    currentScreen = Screen.Block
                                },
                                onNavigateToCitySelection = {
                                    currentScreen = Screen.CitySelection
                                },
                                onNavigateToHome = {
                                    scope.launch {
                                        userCity = preferencesRepository.userCity.first()
                                        currentScreen = Screen.Home
                                        userCity?.let { city ->
                                            AnalyticsHelper.logAppOpen(this@MainActivity, city, deviceModel)
                                        } ?: run {
                                            AnalyticsHelper.logAppOpen(this@MainActivity, null, deviceModel)
                                        }
                                    }
                                }
                            )
                        }
                        Screen.Block -> {
                            BlockScreen()
                        }
                        Screen.CitySelection -> {
                            CitySelectionScreen(
                                preferencesRepository = preferencesRepository,
                                scope = scope,
                                onCitySelected = {
                                    scope.launch {
                                        userCity = preferencesRepository.userCity.first()
                                        currentScreen = Screen.Home
                                        userCity?.let { city ->
                                            AnalyticsHelper.logAppOpen(this@MainActivity, city, deviceModel)
                                        }
                                    }
                                }
                            )
                        }
                        Screen.Home -> {
                            val homeViewModel: HomeViewModel = viewModel(
                                factory = object : ViewModelProvider.Factory {
                                    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                                        @Suppress("UNCHECKED_CAST")
                                        return HomeViewModel(preferencesRepository = preferencesRepository) as T
                                    }
                                }
                            )
                            homeViewModel.setContext(this@MainActivity)

                            MainScreen(
                                homeViewModel = homeViewModel,
                                preferencesRepository = preferencesRepository,
                                userCity = userCity,
                                remoteConfigRepository = remoteConfigRepository
                            )
                        }
                    }
                }
            }
        }
    }

    private sealed class Screen {
        object Splash : Screen()
        object Block : Screen()
        object CitySelection : Screen()
        object Home : Screen()
    }
}
