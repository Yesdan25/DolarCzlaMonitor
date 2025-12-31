package com.yesdan.dolarczlamonitor.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yesdan.dolarczlamonitor.data.local.UserPreferencesRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    preferencesRepository: UserPreferencesRepository,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDarkMode by preferencesRepository.isDarkMode.collectAsState(initial = false)
    val showEuro by preferencesRepository.showEuro.collectAsState(initial = false)
    val scope = rememberCoroutineScope()

    val backgroundGradient = if (isDarkMode) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF121212),
                Color(0xFF1E1E1E)
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFFF0F2F5),
                Color(0xFFE0E5EC)
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "Ajustes",
                        color = if (isDarkMode) Color.White else Color(0xFF2D3436),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = if (isDarkMode) Color.White else Color(0xFF2D3436)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isDarkMode)
                        Color(0xFF1E1E1E).copy(alpha = 0.9f)
                    else
                        Color.White.copy(alpha = 0.55f)
                )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDarkMode)
                            Color(0xFF2D2D2D).copy(alpha = 0.9f)
                        else
                            Color.White.copy(alpha = 0.55f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Preferencias",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkMode) Color.White else Color(0xFF2D3436),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Modo Oscuro",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (isDarkMode) Color.White else Color(0xFF2D3436)
                                )
                                Text(
                                    text = "Cambia el tema de toda la app",
                                    fontSize = 12.sp,
                                    color = if (isDarkMode) Color(0xFFB0B0B0) else Color(0xFF636E72),
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                            Switch(
                                checked = isDarkMode,
                                onCheckedChange = { enabled ->
                                    scope.launch {
                                        preferencesRepository.setDarkMode(enabled)
                                    }
                                }
                            )
                        }

                        HorizontalDivider(
                            color = if (isDarkMode) Color(0xFF4A4A4A) else Color(0xFFB2BEC3).copy(alpha = 0.5f),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Mostrar Euro (€)",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (isDarkMode) Color.White else Color(0xFF2D3436)
                                )
                                Text(
                                    text = "Habilita la visualización del Euro en el Inicio",
                                    fontSize = 12.sp,
                                    color = if (isDarkMode) Color(0xFFB0B0B0) else Color(0xFF636E72),
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                            Switch(
                                checked = showEuro,
                                onCheckedChange = { enabled ->
                                    scope.launch {
                                        preferencesRepository.setShowEuro(enabled)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
