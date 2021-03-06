package ru.volgadev.common

sealed class DataResult<out T : Any>
class SuccessResult<out T : Any>(val data: T) : DataResult<T>()
class ErrorResult(val message: String? = null, val exception: Throwable? = null) : DataResult<Nothing>()