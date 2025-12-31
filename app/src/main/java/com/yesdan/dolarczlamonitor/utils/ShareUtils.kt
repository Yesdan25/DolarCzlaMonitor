package com.yesdan.dolarczlamonitor.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ShareUtils {
    fun createCardBitmap(
        context: Context,
        title: String,
        price: Double,
        currency: String,
        lastUpdated: Long?,
        isDarkMode: Boolean
    ): Bitmap {
        val width = 800
        val height = 400
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val backgroundColor = if (isDarkMode) {
            android.graphics.Color.argb(230, 45, 45, 45)
        } else {
            android.graphics.Color.argb(140, 255, 255, 255)
        }
        val textColor = if (isDarkMode) {
            android.graphics.Color.argb(255, 255, 255, 255)
        } else {
            android.graphics.Color.argb(255, 45, 52, 54)
        }
        val secondaryTextColor = if (isDarkMode) {
            android.graphics.Color.argb(255, 176, 176, 176)
        } else {
            android.graphics.Color.argb(255, 99, 110, 114)
        }
        canvas.drawColor(backgroundColor)
        val paint = android.graphics.Paint().apply {
            isAntiAlias = true
            style = android.graphics.Paint.Style.FILL
            color = backgroundColor
        }
        val radius = 48f
        val rect = android.graphics.RectF(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawRoundRect(rect, radius, radius, paint)
        val titlePaint = android.graphics.Paint().apply {
            color = secondaryTextColor
            textSize = 42f
            isAntiAlias = true
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.NORMAL)
        }
        canvas.drawText(title, 60f, 80f, titlePaint)
        val formatter = NumberFormat.getNumberInstance(Locale.US)
        formatter.minimumFractionDigits = 2
        formatter.maximumFractionDigits = 2
        val priceText = if (price > 0) {
            "${formatter.format(price)} Bs"
        } else {
            "No disponible"
        }
        val pricePaint = android.graphics.Paint().apply {
            color = textColor
            textSize = 126f
            isAntiAlias = true
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
        }
        canvas.drawText(priceText, 60f, 200f, pricePaint)
        if (price > 0 && lastUpdated != null) {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val dateString = "Actualizado: ${dateFormat.format(Date(lastUpdated))}"
            val datePaint = android.graphics.Paint().apply {
                color = secondaryTextColor
                textSize = 36f
                isAntiAlias = true
            }
            canvas.drawText(dateString, 60f, 280f, datePaint)
        }
        val brandingPaint = android.graphics.Paint().apply {
            color = secondaryTextColor
            textSize = 30f
            isAntiAlias = true
            alpha = 180
        }
        canvas.drawText("DolarVzla Monitor", 60f, height - 40f, brandingPaint)
        return bitmap
    }

    suspend fun saveBitmapToCache(
        context: Context,
        bitmap: Bitmap,
        filename: String = "shared_card_${System.currentTimeMillis()}.png"
    ): Uri? = withContext(Dispatchers.IO) {
        try {
            val cacheDir = File(context.cacheDir, "shared_images")
            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
            }
            val imageFile = File(cacheDir, filename)
            FileOutputStream(imageFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                imageFile
            )
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    suspend fun shareCardImage(
        context: Context,
        title: String,
        price: Double,
        currency: String,
        lastUpdated: Long?,
        isDarkMode: Boolean
    ) {
        val bitmap = createCardBitmap(context, title, price, currency, lastUpdated, isDarkMode)
        val uri = saveBitmapToCache(context, bitmap)
        uri?.let {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, it)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                putExtra(Intent.EXTRA_TEXT, "💱 $title: ${NumberFormat.getNumberInstance(Locale.US).format(price)} Bs\n\n📱 DolarVzla Monitor")
            }
            context.startActivity(Intent.createChooser(shareIntent, "Compartir tarjeta"))
        }
    }
}
