package com.streamliners.commons

sealed class ResultOf<T>(val data: T? = null, val message: String? = null) {

    class Success<T>(data: T) : ResultOf<T>(data)

    class Error<T>(message: String) : ResultOf<T>(message = message)

    fun onSuccess(handler: (T) -> Unit): ResultOf<T> {
        if(this is Success)
            data?.let(handler)
        return this
    }

    fun onFailure(handler: (String) -> Unit): ResultOf<T> {
        if(this is Error)
            message?.let(handler)
        return this
    }

    fun finally(handler: () -> Unit): ResultOf<T> {
        handler()
        return this
    }

    fun await(): T {
        if(this is Success)
            data?.let {
                return it
            } ?: throw Exception("Something went wrong")
        else
            message?.let {
                throw Exception(it)
            } ?: throw Exception("Something went wrong")
    }

}