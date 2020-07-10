package com.advice.cards.cards.red.attack

import com.advice.cards.cards.*

class Anger : Card(CardType.ATTACK, TargetType.ENEMY, energy = 0) {

    private val damage = 6

    init {
        effects.add(DamageEffect(damage))
        effects.add(CopyToDiscardEffect(this))
    }
}