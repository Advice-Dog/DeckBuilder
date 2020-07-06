package com.advice.cards.red.attack

import com.advice.cards.*

class Bludgeon : Card(CardType.ATTACK, TargetType.ENEMY, CardRarity.RARE, energy = 3) {

    private val damage = 32

    init {
        effects.add(DamageEffect(damage))
    }

}