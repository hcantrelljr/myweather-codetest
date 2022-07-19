package com.codetest.myweather

interface LoadDataCallback<T> {
    fun onDataLoaded(response: T)
    fun onDataNotAvailable(errorCode: Int, reasonMsg: String)
}
