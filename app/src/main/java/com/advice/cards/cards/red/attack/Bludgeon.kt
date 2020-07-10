package com.advice.cards.cards.red.attack

import com.advice.cards.cards.*

class Bludgeon : Card(CardType.ATTACK, TargetType.ENEMY, CardRarity.RARE, energy = 3) {

    private val damage = 32

    init {
        effects.add(DamageEffect(damage))
    }

}