package com.advice.cards.red.attack

import com.advice.cards.*

class Anger : Card(CardType.ATTACK, TargetType.ENEMY, energy = 0) {

    private val damage = 6

    init {
        effects.add(DamageEffect(damage))
        effects.add(CopyToDiscardEffect(this))
    }
}