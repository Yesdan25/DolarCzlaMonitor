package com.yesdan.dolarczlamonitor.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.yesdan.dolarczlamonitor.R
import com.yesdan.dolarczlamonitor.data.local.UserPreferencesRepository
import com.yesdan.dolarczlamonitor.data.remote.RemoteConfigRepository
import kotlinx.coroutines.flow.first

@Composable
fun SplashScreen(
    remoteConfigRepository: RemoteConfigRepository,
    preferencesRepository: UserPreferencesRepository,
    onNavigateToBlockScreen: () -> Unit,
    onNavigateToCitySelection: () -> Unit,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.splash_logo),
                contentDescription = "DolarVzla Monitor Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(24.dp))

            CircularProgressIndicator(
                color = Color(0xFF6C5CE7)
            )
        }
    }

    LaunchedEffect(Unit) {
        val isAppEnabled = remoteConfigRepository.isAppEnabled()

        if (!isAppEnabled) {
            onNavigateToBlockScreen()
            return@LaunchedEffect
        }

        val userCity = preferencesRepository.userCity.first()

        if (userCity.isNullOrEmpty()) {
            onNavigateToCitySelection()
        } else {
            onNavigateToHome()
        }
    }
}
