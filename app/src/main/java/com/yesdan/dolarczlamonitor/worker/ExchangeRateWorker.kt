package com.yesdan.dolarczlamonitor.worker

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.yesdan.dolarczlamonitor.data.local.WidgetDataStore
import com.yesdan.dolarczlamonitor.data.repository.ExchangeRepository
import com.yesdan.dolarczlamonitor.widget.ExchangeRateWidget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExchangeRateWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val repository = ExchangeRepository()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        return@withContext try {
            val rates = repository.getRates()
            val bcvRate = rates.find { it.source == "BCV" }
            val binanceRate = rates.find { it.source == "Binance" }

            if (bcvRate != null && bcvRate.price > 0) {
                WidgetDataStore.saveBcvPrice(applicationContext, bcvRate.price)
            }

            if (binanceRate != null && binanceRate.price > 0) {
                WidgetDataStore.saveBinancePrice(applicationContext, binanceRate.price)
            }

            if (bcvRate != null && binanceRate != null &&
                bcvRate.price > 0 && binanceRate.price > 0) {
                val spread = ((binanceRate.price - bcvRate.price) / bcvRate.price) * 100
                WidgetDataStore.saveSpreadPercentage(applicationContext, spread)
            }

            WidgetDataStore.saveLastUpdate(applicationContext, System.currentTimeMillis())
            ExchangeRateWidget().updateAll(applicationContext)

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
