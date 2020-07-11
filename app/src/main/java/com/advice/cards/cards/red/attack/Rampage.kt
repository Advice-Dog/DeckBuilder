package com.advice.cards.cards.red.attack

import com.advice.cards.cards.*

class Rampage : Card(CardType.ATTACK, TargetType.ENEMY, CardRarity.UNCOMMON) {

    private val damage = 8
    private val ramp = 5

    init {
        effects.add(DamageRampEffect(damage, ramp))
    }
}