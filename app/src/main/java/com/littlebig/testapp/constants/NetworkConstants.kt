package com.littlebig.testapp.constants

class NetworkConstants {
    companion object {
        //Access Key generated from a free membership of fixer.io. Replace value if needed.
        private const val ACCESS_KEY = "452c30d52baf49534f8f93aa15dbf532"
        private const val CURRENCIES = "USD,EUR,JPY,GBP,AUD,CAD,CHF,CNY,SEK,NZD"

        const val BASE_URL = "http://data.fixer.io/"
        const val GET_ALL_LATEST_RATES = "api/latest?access_key=$ACCESS_KEY&symbols=$CURRENCIES"
        const val GET_TIME_SERIES = "api/timeseries"
        const val GET_ACCESS_KEY = "?access_key=$ACCESS_KEY"
        const val GET_START_DATE_FORMAT = "&start_date="
        const val GET_END_DATE_FORMAT = "&end_date="
        const val GET_SYMBOLS = "&symbols="
    }
}