package com.yesdan.dolarczlamonitor.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class MarketHistoryItem(
    val time: String,
    val message: String,
    val isUpward: Boolean
)

val mockMarketHistory = listOf(
    MarketHistoryItem("09:00 AM", "BCV actualizó a 45.20", false),
    MarketHistoryItem("09:30 AM", "Binance subió 1%", true),
    MarketHistoryItem("10:15 AM", "BCV actualizó a 45.25", true),
    MarketHistoryItem("10:45 AM", "Binance bajó 0.5%", false),
    MarketHistoryItem("11:30 AM", "BCV actualizó a 45.30", true),
    MarketHistoryItem("12:00 PM", "Binance subió 2%", true),
    MarketHistoryItem("01:15 PM", "BCV actualizó a 45.35", true),
    MarketHistoryItem("02:00 PM", "Binance bajó 1.2%", false),
    MarketHistoryItem("02:45 PM", "BCV actualizó a 45.40", true),
    MarketHistoryItem("03:30 PM", "Binance subió 0.8%", true),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    isDarkMode: Boolean,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                        text = "Historial de Mercado",
                        color = if (isDarkMode) Color.White else Color(0xFF2D3436),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
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

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(mockMarketHistory) { item ->
                    MarketHistoryCard(
                        item = item,
                        isDarkMode = isDarkMode
                    )
                }
            }
        }
    }
}

@Composable
private fun MarketHistoryCard(
    item: MarketHistoryItem,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkMode)
                Color(0xFF2D2D2D).copy(alpha = 0.9f)
            else
                Color.White.copy(alpha = 0.55f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = if (item.isUpward) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                    contentDescription = if (item.isUpward) "Subió" else "Bajó",
                    tint = if (item.isUpward) Color(0xFF00B894) else Color(0xFFE17055),
                    modifier = Modifier.padding(4.dp)
                )
                Column {
                    Text(
                        text = item.message,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isDarkMode) Color.White else Color(0xFF2D3436)
                    )
                    Text(
                        text = item.time,
                        fontSize = 12.sp,
                        color = if (isDarkMode) Color(0xFFB0B0B0) else Color(0xFF636E72),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}
