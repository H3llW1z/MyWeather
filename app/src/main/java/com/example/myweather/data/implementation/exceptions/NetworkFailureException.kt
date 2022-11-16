package com.example.myweather.data.implementation.exceptions

class NetworkFailureException: Exception {
    constructor() : super()
    constructor(message: String) : super(message)
}