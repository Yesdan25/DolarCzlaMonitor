package com.yesdan.dolarczlamonitor.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.yesdan.dolarczlamonitor.utils.AnalyticsHelper

@Composable
fun AdBanner(
    imageUrl: String,
    linkUrl: String?,
    modifier: Modifier = Modifier,
    cardColor: Color,
    isDarkMode: Boolean
) {
    val context = LocalContext.current
    var imageLoaded by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp), clip = false, ambientColor = Color.Gray.copy(alpha = 0.2f), spotColor = Color.Gray.copy(alpha = 0.2f))
            .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.8f)), RoundedCornerShape(16.dp))
            .clickable {
                linkUrl?.let { url ->
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                        AnalyticsHelper.logAdClicked(context, "local")
                    } catch (e: Exception) {
                    }
                }
            },
        color = cardColor,
        shape = RoundedCornerShape(16.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Anuncio",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop,
                onSuccess = { imageLoaded = true },
                onError = {
                }
            )
        }
    }
}
