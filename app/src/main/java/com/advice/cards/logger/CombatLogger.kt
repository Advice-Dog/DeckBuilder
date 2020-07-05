package com.advice.cards.logger

object CombatLogger : BaseLogger() {

    private val log = ArrayList<String>()

    override fun onMessage(message: String) {
        log.add(message)
    }

    override fun toString() = log.joinToString(separator = "\n")

    fun reset() {
        log.clear()
    }
}