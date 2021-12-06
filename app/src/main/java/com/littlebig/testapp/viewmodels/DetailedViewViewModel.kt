package com.littlebig.testapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.littlebig.testapp.constants.LogTags
import com.littlebig.testapp.constants.NetworkConstants
import com.littlebig.testapp.models.ExchangeSet
import com.littlebig.testapp.network.ApiClient
import com.littlebig.testapp.utils.DataFetchingCallback
import com.littlebig.testapp.utils.FilteringTools
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class DetailedViewViewModel @Inject constructor(private val apiClient: ApiClient, private val filteringTools: FilteringTools)
    : ViewModel() {

    fun getCurrencyRates(callback: DataFetchingCallback, currency1: String?, currency2: String?, amount: String?) {

        apiClient.getHistoricalRates(
                    NetworkConstants.GET_TIME_SERIES
                        + NetworkConstants.GET_ACCESS_KEY
                        + NetworkConstants.GET_START_DATE_FORMAT + filteringTools.getDate(4)
                        + NetworkConstants.GET_END_DATE_FORMAT + filteringTools.getDate(0)
                        + NetworkConstants.GET_SYMBOLS
                        + currency1 + "," + currency2
            )?.enqueue(object : Callback<ExchangeSet?> {
                override fun onResponse(
                    call: Call<ExchangeSet?>,
                    response: Response<ExchangeSet?>
                ) {
                    response.let {
                        if (it.isSuccessful && it.body() != null) {
                            var results = it.body()!!
                            if (results.success) {
                                var dataSet = filteringTools.extractExchangeInstance(results,amount)
                                callback.fetchingDone(dataSet)
                            } else {
                                callback.fetchingError()
                            }
                        } else {
                            callback.fetchingError()
                            it.errorBody()?.let {
                                Log.e(LogTags.NETWORK_ERROR, it.string())
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<ExchangeSet?>, t: Throwable) {
                    callback.fetchingError()
                    t.let {
                        Log.e(LogTags.NETWORK_ERROR, it.message.toString())
                    }
                }
            })
    }
}