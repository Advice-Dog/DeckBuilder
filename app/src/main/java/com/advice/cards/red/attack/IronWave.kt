package com.advice.cards.red.attack

import com.advice.cards.*

class IronWave : Card(CardType.ATTACK, TargetType.ENEMY) {

    private val block = 5
    private val damage = 5

    init {
        effects.add(BlockEffect(block))
        effects.add(DamageEffect(damage))
    }
}