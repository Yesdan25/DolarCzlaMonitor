package com.yesdan.dolarczlamonitor.data.model

data class ExchangeRate(
    val price: Double,
    val source: String,
    val currency: String = "USD",
    val lastUpdated: Long = System.currentTimeMillis()
)
