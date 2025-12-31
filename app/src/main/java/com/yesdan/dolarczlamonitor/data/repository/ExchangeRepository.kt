package com.yesdan.dolarczlamonitor.data.repository

import android.util.Log
import com.yesdan.dolarczlamonitor.data.api.BinanceApiClient
import com.yesdan.dolarczlamonitor.data.api.BinanceRequest
import com.yesdan.dolarczlamonitor.data.model.ExchangeRate
import com.yesdan.dolarczlamonitor.data.scraper.BCVScraper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ExchangeRepository {
    private val binanceApi = BinanceApiClient.api

    suspend fun getRates(): List<ExchangeRate> = withContext(Dispatchers.IO) {
        val rates = mutableListOf<ExchangeRate>()

        val binanceRate = try {
            getBinanceRate()
        } catch (e: Exception) {
            Log.e("ExchangeError", "Fallo en Binance: ${e.message}", e)
            ExchangeRate(
                price = 0.0,
                source = "Binance",
                currency = "USD",
                lastUpdated = System.currentTimeMillis()
            )
        }
        rates.add(binanceRate)

        val bcvRate = try {
            getBCVRate()
        } catch (e: Exception) {
            Log.e("ExchangeError", "Fallo en BCV: ${e.message}", e)
            ExchangeRate(
                price = 0.0,
                source = "BCV",
                currency = "USD",
                lastUpdated = System.currentTimeMillis()
            )
        }
        rates.add(bcvRate)

        return@withContext rates
    }

    suspend fun getEuroRates(binanceUsdtPrice: Double, bcvDollarPrice: Double): List<ExchangeRate> = withContext(Dispatchers.IO) {
        val rates = mutableListOf<ExchangeRate>()

        val bcvEuroRate = try {
            getBCVEuroRate()
        } catch (e: Exception) {
            Log.e("ExchangeError", "Fallo en BCV Euro: ${e.message}", e)
            ExchangeRate(
                price = 0.0,
                source = "BCV",
                currency = "EUR",
                lastUpdated = System.currentTimeMillis()
            )
        }
        rates.add(bcvEuroRate)

        if (bcvEuroRate.price > 0 && binanceUsdtPrice > 0 && bcvDollarPrice > 0) {
            val euroParaleloPrice = (binanceUsdtPrice / bcvDollarPrice) * bcvEuroRate.price
            rates.add(
                ExchangeRate(
                    price = euroParaleloPrice,
                    source = "Paralelo",
                    currency = "EUR",
                    lastUpdated = System.currentTimeMillis()
                )
            )
        }

        return@withContext rates
    }

    private suspend fun getBCVEuroRate(): ExchangeRate {
        return try {
            val price = BCVScraper.getEuroPrice()

            if (price != null && price > 0) {
                ExchangeRate(
                    price = price,
                    source = "BCV",
                    currency = "EUR",
                    lastUpdated = System.currentTimeMillis()
                )
            } else {
                Log.e("ExchangeError", "Fallo en BCV Euro: Precio inválido o nulo")
                throw Exception("No se pudo obtener precio del Euro BCV (precio nulo o inválido)")
            }
        } catch (e: IOException) {
            Log.e("ExchangeError", "Fallo en BCV Euro: Error de IO/red - ${e.message}", e)
            throw Exception("Error de conexión al BCV Euro: ${e.message}")
        } catch (e: SocketTimeoutException) {
            Log.e("ExchangeError", "Fallo en BCV Euro: Timeout de conexión", e)
            throw Exception("Timeout de conexión al BCV Euro")
        } catch (e: UnknownHostException) {
            Log.e("ExchangeError", "Fallo en BCV Euro: Error de conexión/DNS", e)
            throw Exception("No se pudo conectar al BCV Euro (error de red/DNS)")
        } catch (e: Exception) {
            Log.e("ExchangeError", "Fallo en BCV Euro: ${e.message}", e)
            throw e
        }
    }

    private suspend fun getBinanceRate(): ExchangeRate {
        return try {
            val request = BinanceRequest(
                asset = "USDT",
                fiat = "VES",
                tradeType = "BUY",
                rows = 1,
                page = 1,
                publisherType = null
            )

            val response = binanceApi.searchP2P(request)

            if (response.isSuccessful && response.body()?.success == true) {
                val dataList = response.body()?.data
                val adv = dataList?.firstOrNull()?.adv
                val priceString = adv?.price

                if (priceString != null) {
                    val price = priceString.toDoubleOrNull()
                    if (price != null && price > 0) {
                        ExchangeRate(
                            price = price,
                            source = "Binance",
                            currency = "USD",
                            lastUpdated = System.currentTimeMillis()
                        )
                    } else {
                        Log.e("ExchangeError", "Fallo en Binance: Precio inválido o cero - $priceString")
                        throw Exception("Precio inválido de Binance: $priceString")
                    }
                } else {
                    Log.e("ExchangeError", "Fallo en Binance: Respuesta sin datos de precio (data: $dataList)")
                    throw Exception("Respuesta de Binance sin datos de precio")
                }
            } else {
                val errorMsg = "Respuesta no exitosa: ${response.code()} - ${response.message()}"
                Log.e("ExchangeError", "Fallo en Binance: $errorMsg")
                throw Exception(errorMsg)
            }
        } catch (e: HttpException) {
            Log.e("ExchangeError", "Fallo en Binance: Error HTTP ${e.code()} - ${e.message()}", e)
            throw Exception("Error HTTP ${e.code()}: ${e.message()}")
        } catch (e: SocketTimeoutException) {
            Log.e("ExchangeError", "Fallo en Binance: Timeout de conexión", e)
            throw Exception("Timeout de conexión a Binance")
        } catch (e: UnknownHostException) {
            Log.e("ExchangeError", "Fallo en Binance: Error de conexión/DNS", e)
            throw Exception("No se pudo conectar a Binance (error de red)")
        } catch (e: Exception) {
            Log.e("ExchangeError", "Fallo en Binance: ${e.message}", e)
            throw e
        }
    }

    private suspend fun getBCVRate(): ExchangeRate {
        return try {
            val price = BCVScraper.getDollarPrice()

            if (price != null && price > 0) {
                ExchangeRate(
                    price = price,
                    source = "BCV",
                    currency = "USD",
                    lastUpdated = System.currentTimeMillis()
                )
            } else {
                Log.e("ExchangeError", "Fallo en BCV: Precio inválido o nulo")
                throw Exception("No se pudo obtener precio del BCV (precio nulo o inválido)")
            }
        } catch (e: IOException) {
            Log.e("ExchangeError", "Fallo en BCV: Error de IO/red - ${e.message}", e)
            throw Exception("Error de conexión al BCV: ${e.message}")
        } catch (e: SocketTimeoutException) {
            Log.e("ExchangeError", "Fallo en BCV: Timeout de conexión", e)
            throw Exception("Timeout de conexión al BCV")
        } catch (e: UnknownHostException) {
            Log.e("ExchangeError", "Fallo en BCV: Error de conexión/DNS", e)
            throw Exception("No se pudo conectar al BCV (error de red/DNS)")
        } catch (e: Exception) {
            Log.e("ExchangeError", "Fallo en BCV: ${e.message}", e)
            throw e
        }
    }
}
