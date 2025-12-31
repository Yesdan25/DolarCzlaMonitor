package com.yesdan.dolarczlamonitor.data.scraper

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.helper.HttpConnection
import java.io.IOException
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.security.cert.X509Certificate

object BCVScraper {
    private const val BCV_URL = "https://www.bcv.org.ve/"

    private fun createInsecureTrustManager(): TrustManager {
        return object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        }
    }

    private fun createInsecureSocketFactory(): SSLSocketFactory {
        val trustAllCerts = arrayOf<TrustManager>(createInsecureTrustManager())
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        return sslContext.socketFactory
    }

    suspend fun getDollarPrice(): Double? = withContext(Dispatchers.IO) {
        try {
            val connection = Jsoup.connect(BCV_URL) as HttpConnection
            connection
                .timeout(15000)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .ignoreHttpErrors(true)
                .sslSocketFactory(createInsecureSocketFactory())

            val document = connection.get()
            val dolarDiv = document.selectFirst("div#dolar")

            if (dolarDiv != null) {
                val priceElement = dolarDiv.selectFirst("strong")

                if (priceElement != null) {
                    val priceText = priceElement.text()
                        .replace(",", ".")
                        .replace(Regex("[^0-9.]"), "")

                    return@withContext priceText.toDoubleOrNull()
                }
            }

            val priceText = dolarDiv?.text()
                ?.replace(",", ".")
                ?.replace(Regex("[^0-9.]"), "")

            return@withContext priceText?.toDoubleOrNull()
        } catch (e: IOException) {
            e.printStackTrace()
            return@withContext null
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }

    suspend fun getEuroPrice(): Double? = withContext(Dispatchers.IO) {
        try {
            val connection = Jsoup.connect(BCV_URL) as HttpConnection
            connection
                .timeout(15000)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .ignoreHttpErrors(true)
                .sslSocketFactory(createInsecureSocketFactory())

            val document = connection.get()
            val euroDiv = document.selectFirst("div#euro")

            if (euroDiv != null) {
                val priceElement = euroDiv.selectFirst("strong")

                if (priceElement != null) {
                    val priceText = priceElement.text()
                        .replace(",", ".")
                        .replace(Regex("[^0-9.]"), "")

                    return@withContext priceText.toDoubleOrNull()
                }
            }

            val priceText = euroDiv?.text()
                ?.replace(",", ".")
                ?.replace(Regex("[^0-9.]"), "")

            return@withContext priceText?.toDoubleOrNull()
        } catch (e: IOException) {
            e.printStackTrace()
            return@withContext null
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }
}
