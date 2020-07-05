package com.advice.cards.logger

open class BaseLogger {

    open fun onMessage(message: String) {
        println(message)
    }
}