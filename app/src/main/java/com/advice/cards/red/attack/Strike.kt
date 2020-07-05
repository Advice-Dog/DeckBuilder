package com.advice.cards.red.attack

import com.advice.cards.*

class Strike : Card(CardType.ATTACK, TargetType.ENEMY) {

    private val damage = 5

    override val description: String
        get() = "Deal $damage damage."

    override fun play(self: Entity, entity: Entity) {
        entity.dealDamage(damage)
    }

}