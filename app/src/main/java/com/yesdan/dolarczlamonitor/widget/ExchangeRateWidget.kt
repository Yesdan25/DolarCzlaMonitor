package com.yesdan.dolarczlamonitor.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.action.clickable
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.glance.LocalContext
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.yesdan.dolarczlamonitor.data.local.WidgetDataStore
import com.yesdan.dolarczlamonitor.worker.ExchangeRateWorker
import java.text.NumberFormat
import java.util.Locale

class ExchangeRateWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            WidgetContent()
        }
    }

    @Composable
    private fun WidgetContent() {
        val context = LocalContext.current
        val bcvPrice = WidgetDataStore.getBcvPrice(context)
        val binancePrice = WidgetDataStore.getBinancePrice(context)
        val spreadPercentage = WidgetDataStore.getSpreadPercentage(context)

        val formatter = NumberFormat.getNumberInstance(Locale.US)
        formatter.minimumFractionDigits = 2
        formatter.maximumFractionDigits = 2

        val spreadColor = when {
            kotlin.math.abs(spreadPercentage) > 3.0 -> Color(0xFFE17055)
            spreadPercentage < 0 -> Color(0xFF00B894)
            else -> Color(0xFF636E72)
        }

        val darkGray = Color(0xFF2D3436)

        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(
                    color = Color(android.graphics.Color.argb(217, 255, 255, 255))
                )
                .cornerRadius(24.dp)
                .padding(16.dp)
        ) {
            Column(
                modifier = GlanceModifier.fillMaxSize(),
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🇻🇪 DolarVzla",
                        style = TextStyle(
                            color = ColorProvider(darkGray),
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = GlanceModifier.width(8.dp))
                    Text(
                        text = "🔄",
                        style = TextStyle(
                            color = ColorProvider(darkGray),
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = GlanceModifier.clickable(actionRunCallback<RefreshCallback>())
                    )
                }

                Spacer(modifier = GlanceModifier.height(12.dp))

                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🏦 BCV",
                        style = TextStyle(
                            color = ColorProvider(darkGray),
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Text(
                        text = if (bcvPrice > 0) "${formatter.format(bcvPrice)} Bs" else "N/A",
                        style = TextStyle(
                            color = ColorProvider(darkGray),
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = GlanceModifier.height(8.dp))

                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🔶 P2P",
                        style = TextStyle(
                            color = ColorProvider(darkGray),
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Text(
                        text = if (binancePrice > 0) "${formatter.format(binancePrice)} Bs" else "N/A",
                        style = TextStyle(
                            color = ColorProvider(darkGray),
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = GlanceModifier.height(12.dp))

                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⚠️ Brecha",
                        style = TextStyle(
                            color = ColorProvider(spreadColor),
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Text(
                        text = "${String.format(Locale.US, "%.2f", kotlin.math.abs(spreadPercentage))}%",
                        style = TextStyle(
                            color = ColorProvider(spreadColor),
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

class RefreshCallback : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        androidx.work.WorkManager.getInstance(context).enqueue(
            androidx.work.OneTimeWorkRequestBuilder<ExchangeRateWorker>().build()
        )
    }
}
