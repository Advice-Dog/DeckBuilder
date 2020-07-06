package com.advice.cards.red.attack

import com.advice.cards.*

class TwinStrike : Card(CardType.ATTACK, TargetType.ENEMY, count = 2) {

    private val damage = 5

    init {
        effects.add(DamageEffect(damage))
    }

    override val description: String
        get() = "Deal $damage damage twice."

    override fun play(self: Entity, entity: Entity) {
        effects.forEach { it.apply(self, entity) }
    }

    override fun getDescription(self: Entity, target: Entity?): String {
        return effects.first().getDescription(self, target).replace(".", " twice.")
    }
}