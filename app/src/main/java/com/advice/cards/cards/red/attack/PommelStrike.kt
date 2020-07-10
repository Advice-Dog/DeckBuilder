package com.advice.cards.cards.red.attack

import com.advice.cards.cards.*

class PommelStrike : Card(CardType.ATTACK, TargetType.ENEMY) {

    private val damage = 9
    private val draw = 1

    init {
        effects.add(DamageEffect(damage))
        effects.add(DrawCardEffect(draw))
    }
}