package com.yesdan.dolarczlamonitor.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yesdan.dolarczlamonitor.data.local.UserPreferencesRepository
import com.yesdan.dolarczlamonitor.data.remote.RemoteConfigRepository
import com.yesdan.dolarczlamonitor.ui.viewmodel.HomeViewModel

sealed class Screen {
    object Home : Screen()
    object Notifications : Screen()
    object Settings : Screen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    homeViewModel: HomeViewModel,
    preferencesRepository: UserPreferencesRepository,
    userCity: String? = null,
    remoteConfigRepository: RemoteConfigRepository,
    modifier: Modifier = Modifier
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    val isDarkMode by preferencesRepository.isDarkMode.collectAsState(initial = false)
    val showEuro by preferencesRepository.showEuro.collectAsState(initial = false)

    val bottomBarColor = if (isDarkMode)
        Color(0xFF1E1E1E).copy(alpha = 0.9f)
    else
        Color.White.copy(alpha = 0.55f)

    val iconColor = if (isDarkMode) Color.White else Color(0xFF2D3436)

    androidx.compose.material3.Scaffold(
        modifier = modifier,
        bottomBar = {
            BottomAppBar(
                containerColor = bottomBarColor
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(
                        onClick = { currentScreen = Screen.Home },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Inicio",
                            tint = if (currentScreen is Screen.Home) Color(0xFF6C5CE7) else iconColor
                        )
                    }
                    IconButton(
                        onClick = { currentScreen = Screen.Notifications },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notificaciones",
                            tint = if (currentScreen is Screen.Notifications) Color(0xFF6C5CE7) else iconColor
                        )
                    }
                    IconButton(
                        onClick = { currentScreen = Screen.Settings },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Ajustes",
                            tint = if (currentScreen is Screen.Settings) Color(0xFF6C5CE7) else iconColor
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (currentScreen) {
                is Screen.Home -> {
                    HomeScreen(
                        viewModel = homeViewModel,
                        isDarkMode = isDarkMode,
                        userCity = userCity,
                        remoteConfigRepository = remoteConfigRepository,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                is Screen.Notifications -> {
                    NotificationScreen(
                        isDarkMode = isDarkMode,
                        onBackClick = { currentScreen = Screen.Home },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                is Screen.Settings -> {
                    SettingsScreen(
                        preferencesRepository = preferencesRepository,
                        onBackClick = { currentScreen = Screen.Home },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
