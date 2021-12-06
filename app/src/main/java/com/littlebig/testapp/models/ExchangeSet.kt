package com.littlebig.testapp.models

import com.google.gson.annotations.SerializedName
import java.util.*

open class ExchangeSet (
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("base")
    val base: String,

    @SerializedName("date")
    val date: Date,

    @SerializedName("rates")
    val ratesSetMap: Map<Date, Map<String, Double>>
)