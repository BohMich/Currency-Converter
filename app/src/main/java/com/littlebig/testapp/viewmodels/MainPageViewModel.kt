package com.littlebig.testapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.littlebig.testapp.constants.LogTags
import com.littlebig.testapp.constants.NetworkConstants
import com.littlebig.testapp.models.SingleExchange
import com.littlebig.testapp.network.ApiClient
import com.littlebig.testapp.utils.DataFetchingCallback
import com.littlebig.testapp.utils.FilteringTools
import com.littlebig.testapp.utils.UserSelectionCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MainPageViewModel @Inject constructor(private val apiClient: ApiClient, private val filteringTools: FilteringTools)
    : ViewModel() {

    private var cachedAllCurrencies: SingleExchange? = null
    private var cachedSelectedCurrency: MutableList<String> = ArrayList()
    private var cachedCurrencyBufferSize: Int = 2

    fun getCurrencies(callback: DataFetchingCallback, conversionAmount: String?) {

        if (cachedAllCurrencies != null) {
            var results = cachedAllCurrencies!!
            results = filteringTools.extractExchangeInstance(results,conversionAmount)
            callback.fetchingDone(results)
        } else {
            apiClient.getAllCurrenciesAlternative(NetworkConstants.GET_ALL_LATEST_RATES)?.enqueue(object: Callback<SingleExchange?> {

                override fun onResponse(
                    call: Call<SingleExchange?>,
                    response: Response<SingleExchange?>
                ) {
                    response.let {
                        if (it.isSuccessful && it.body() != null) {
                            cachedAllCurrencies = it.body()

                            var results = it.body()!!
                            if(results.success){
                                results = filteringTools.extractExchangeInstance(results,conversionAmount)
                                callback.fetchingDone(results)
                            }else{
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

                override fun onFailure(call: Call<SingleExchange?>, t: Throwable) {
                    callback.fetchingError()
                    t.let {
                        Log.e(LogTags.NETWORK_ERROR, it.message.toString())
                    }
                }
            })
        }
    }

    fun selectedCurrency(callback: UserSelectionCallback, currency: String){
        if(!cachedSelectedCurrency.contains(currency)){
            cachedSelectedCurrency.add(currency)
        }

        if(cachedSelectedCurrency.size == cachedCurrencyBufferSize ){
            val callbackList = cachedSelectedCurrency

            callback.currencySetSelected(callbackList)
            cachedSelectedCurrency.clear()
        }
        if(cachedSelectedCurrency.size > cachedCurrencyBufferSize){
            cachedSelectedCurrency.clear()
        }
    }
}