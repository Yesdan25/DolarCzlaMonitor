package com.yesdan.dolarczlamonitor.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface BinanceApi {
    @POST("bapi/c2c/v2/friendly/c2c/adv/search")
    suspend fun searchP2P(@Body request: BinanceRequest): Response<BinanceResponse>
}

data class BinanceRequest(
    val asset: String,
    val fiat: String,
    val tradeType: String,
    val rows: Int,
    val page: Int,
    val publisherType: String? = null
)

data class BinanceResponse(
    val code: String,
    val message: String?,
    val messageDetail: String?,
    val data: List<BinanceData>?,
    val total: Int,
    val success: Boolean
)

data class BinanceData(
    val adv: BinanceAdv?
)

data class BinanceAdv(
    val price: String,
    val minSingleTransAmount: String?,
    val maxSingleTransAmount: String?,
    val tradeType: String?
)
