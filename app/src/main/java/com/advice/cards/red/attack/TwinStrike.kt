package com.advice.cards.red.attack

import com.advice.cards.*

class TwinStrike : Card(CardType.ATTACK, TargetType.ENEMY) {

    private val damage = 5

    init {
        effects.add(DamageEffect(damage))
        effects.add(DamageEffect(damage))
    }

    override fun getDescription(self: Entity, target: Entity?): String {
        return effects.first().getDescription(self, target).replace(".", " twice.")
    }
}