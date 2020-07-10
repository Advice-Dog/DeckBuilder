package com.advice.cards.cards.status

abstract class StatusEffect(stacks: Int) {

    private var remainingTurns = stacks

    fun tick() {
        remainingTurns--
    }

    fun getStacks() = remainingTurns

    fun hasExpired() = remainingTurns == 0

}