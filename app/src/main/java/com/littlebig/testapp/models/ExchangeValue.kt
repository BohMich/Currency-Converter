package com.littlebig.testapp.models

import com.google.gson.annotations.SerializedName

open class ExchangeValue (
    @SerializedName("currencyName")
    val currencyName: String,

    @SerializedName("rate")
    var rate: Double,
)