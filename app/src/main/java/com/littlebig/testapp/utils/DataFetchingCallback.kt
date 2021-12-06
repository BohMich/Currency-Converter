package com.littlebig.testapp.utils

interface DataFetchingCallback {
    fun fetchingDone(payload: Any)
    fun fetchingError()
}