package com.littlebig.testapp.network

import com.littlebig.testapp.models.ExchangeSet
import com.littlebig.testapp.models.SingleExchange
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

// External gate for communication with API endpoints (operated by Retrofit)
interface ApiClient {

    @GET
    fun getAllCurrenciesAlternative(@Url url: String?): Call<SingleExchange?>?

    @GET
    fun getHistoricalRates(@Url url: String?): Call<ExchangeSet?>?

}