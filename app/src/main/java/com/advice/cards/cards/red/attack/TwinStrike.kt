package com.advice.cards.cards.red.attack

import com.advice.cards.*
import com.advice.cards.cards.Card
import com.advice.cards.cards.CardType
import com.advice.cards.cards.DamageEffect
import com.advice.cards.cards.TargetType

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