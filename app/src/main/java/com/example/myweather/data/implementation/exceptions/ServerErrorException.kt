package com.example.myweather.data.implementation.exceptions

class ServerErrorException: Exception {
    constructor() : super()
    constructor(message: String) : super(message)
}