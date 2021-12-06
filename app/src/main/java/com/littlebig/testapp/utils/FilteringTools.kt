package com.littlebig.testapp.utils

import com.littlebig.testapp.models.ExchangeValue
import com.littlebig.testapp.models.ExchangeSet
import com.littlebig.testapp.models.SingleExchange
import java.math.RoundingMode
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FilteringTools {

    fun extractExchangeInstance(currencySet: SingleExchange, conversionValue: String?
    ): SingleExchange {

        val amount: Double? = conversionValue?.toDoubleOrNull()
        val currencies: MutableList<ExchangeValue> = ArrayList()

        currencySet.ratesSetMap.forEach { (name, rate) ->
            currencies.add(ExchangeValue(name,rate)) }

        if(amount != null && amount > 0){
            currencies.forEach {currency: ExchangeValue ->
                currency.rate = currency.rate * amount}
        }
        currencies.forEach {currency: ExchangeValue ->
            currency.rate = currency.rate.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()}

        currencySet.currencyArray = currencies

        return currencySet
    }

    fun extractExchangeInstance(timeSeriesSet: ExchangeSet, conversionValue: String?
    ): List<SingleExchange> {
        val exchangeObjectCache: MutableList<SingleExchange> = ArrayList()
        val processedData: MutableList<SingleExchange> = ArrayList()
        var amount: Double = 0.0

        if(conversionValue?.toDoubleOrNull() != null){
            amount = conversionValue.toDouble()
        }

        timeSeriesSet.ratesSetMap.forEach { (date, set) ->
            exchangeObjectCache.add(SingleExchange(timeSeriesSet.success,
                                             timeSeriesSet.base,
                                             date,
                                             set,
                                             amount,
                                             ArrayList()))
            }

        exchangeObjectCache.forEach { entry: SingleExchange ->
            processedData.add(extractExchangeInstance(entry, conversionValue))
        }

        processedData.sortByDescending { it.date }

        return processedData
    }

    fun getDate(daysFromNow: Int) :String{
        val dateFormatter: DateFormat = SimpleDateFormat("yyyy-MM-dd")

        val calendar =  Calendar.getInstance()

        calendar.add(Calendar.DAY_OF_YEAR, -daysFromNow)
        return dateFormatter.format(calendar.time)
    }
}

