package com.e.kotlinapp.network

import java.io.IOException

class NoConnectivityException : IOException() {
    override val message: String
        get() = "Network Connection exceptionnnn"
}