package com.yesdan.dolarczlamonitor.ui.viewmodel

import com.yesdan.dolarczlamonitor.data.model.ExchangeRate

sealed class ExchangeRateUiState {
    object Loading : ExchangeRateUiState()
    data class Success(
        val dollarRates: List<ExchangeRate>,
        val euroRates: List<ExchangeRate>,
        val dollarSpreadPercentage: Double?,
        val euroSpreadPercentage: Double?
    ) : ExchangeRateUiState()
    data class Error(val message: String) : ExchangeRateUiState()
}
