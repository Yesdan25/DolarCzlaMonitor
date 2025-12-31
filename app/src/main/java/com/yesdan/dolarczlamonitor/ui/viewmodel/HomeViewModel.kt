package com.yesdan.dolarczlamonitor.ui.viewmodel

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yesdan.dolarczlamonitor.data.local.WidgetDataStore
import com.yesdan.dolarczlamonitor.data.local.UserPreferencesRepository
import com.yesdan.dolarczlamonitor.data.model.ExchangeRate
import com.yesdan.dolarczlamonitor.data.repository.ExchangeRepository
import com.yesdan.dolarczlamonitor.widget.ExchangeRateWidget
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: ExchangeRepository = ExchangeRepository(),
    private val preferencesRepository: UserPreferencesRepository? = null
) : ViewModel() {
    private var appContext: Context? = null

    fun setContext(context: Context) {
        this.appContext = context.applicationContext
    }

    private val _uiState = MutableStateFlow<ExchangeRateUiState>(ExchangeRateUiState.Loading)
    val uiState: StateFlow<ExchangeRateUiState> = _uiState.asStateFlow()

    private val _showEuro = MutableStateFlow(false)
    val showEuro: StateFlow<Boolean> = _showEuro.asStateFlow()

    init {
        viewModelScope.launch {
            preferencesRepository?.showEuro?.first()?.let { show ->
                _showEuro.value = show
            }
        }
        viewModelScope.launch {
            preferencesRepository?.showEuro?.collect { show ->
                val previousValue = _showEuro.value
                _showEuro.value = show
                if (previousValue != show && show) {
                    loadRates()
                }
            }
        }
        loadRates()
    }

    fun loadRates() {
        viewModelScope.launch {
            _uiState.value = ExchangeRateUiState.Loading

            try {
                val dollarRates = repository.getRates()
                val bcvDollarRate = dollarRates.find { it.source == "BCV" && it.currency == "USD" }
                val binanceRate = dollarRates.find { it.source == "Binance" && it.currency == "USD" }

                val dollarSpreadPercentage = calculateSpread(
                    binanceRate,
                    bcvDollarRate
                )

                val euroRates = if (_showEuro.value && bcvDollarRate?.price != null && binanceRate?.price != null) {
                    repository.getEuroRates(
                        binanceUsdtPrice = binanceRate.price,
                        bcvDollarPrice = bcvDollarRate.price
                    )
                } else {
                    emptyList()
                }

                val bcvEuroRate = euroRates.find { it.source == "BCV" && it.currency == "EUR" }
                val euroParaleloRate = euroRates.find { it.source == "Paralelo" && it.currency == "EUR" }
                val euroSpreadPercentage = calculateSpread(
                    euroParaleloRate,
                    bcvEuroRate
                )

                val validDollarRates = dollarRates.filter { it.price > 0 }
                val validEuroRates = euroRates.filter { it.price > 0 }

                if (validDollarRates.isNotEmpty() || validEuroRates.isNotEmpty()) {
                    appContext?.let { ctx ->
                        saveDataToWidgetStore(ctx, dollarRates, dollarSpreadPercentage)
                        viewModelScope.launch {
                            ExchangeRateWidget().updateAll(ctx)
                        }
                    }

                    _uiState.value = ExchangeRateUiState.Success(
                        dollarRates = dollarRates,
                        euroRates = euroRates,
                        dollarSpreadPercentage = dollarSpreadPercentage,
                        euroSpreadPercentage = euroSpreadPercentage
                    )
                } else {
                    _uiState.value = ExchangeRateUiState.Error(
                        "No se pudieron obtener las tasas de cambio de ninguna fuente"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = ExchangeRateUiState.Error(
                    e.message ?: "Error desconocido al obtener las tasas"
                )
            }
        }
    }

    private fun calculateSpread(
        marketRate: ExchangeRate?,
        officialRate: ExchangeRate?
    ): Double? {
        if (marketRate != null && officialRate != null &&
            marketRate.price > 0 && officialRate.price > 0) {
            val spread = ((marketRate.price - officialRate.price) / officialRate.price) * 100
            return spread
        }
        return null
    }

    private fun saveDataToWidgetStore(
        context: Context,
        rates: List<ExchangeRate>,
        spreadPercentage: Double?
    ) {
        val bcvRate = rates.find { it.source == "BCV" && it.currency == "USD" }
        val binanceRate = rates.find { it.source == "Binance" && it.currency == "USD" }

        if (bcvRate != null && bcvRate.price > 0) {
            WidgetDataStore.saveBcvPrice(context, bcvRate.price)
        }

        if (binanceRate != null && binanceRate.price > 0) {
            WidgetDataStore.saveBinancePrice(context, binanceRate.price)
        }

        if (spreadPercentage != null) {
            WidgetDataStore.saveSpreadPercentage(context, spreadPercentage)
        }

        WidgetDataStore.saveLastUpdate(context, System.currentTimeMillis())
    }
}
