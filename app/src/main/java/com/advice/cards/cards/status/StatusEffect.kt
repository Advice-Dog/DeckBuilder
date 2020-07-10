package com.advice.cards.cards.status

abstract class StatusEffect(stacks: Int) : Cloneable {

    private var remainingTurns = stacks

    fun tick() {
        remainingTurns--
    }

    fun getStacks() = remainingTurns

    fun hasExpired() = remainingTurns == 0

    public override fun clone(): StatusEffect {
        return super.clone() as StatusEffect
    }
}