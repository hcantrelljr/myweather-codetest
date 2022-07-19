package com.codetest.myweather

enum class ResponseStatus {
    ERROR, LOADING, SUCCESS
}

class ResponseStatusCallbacks<T>(
    val status: ResponseStatus,
    val data: T?
)
