package com.advice.cards.red.attack

import com.advice.cards.*

class TwinStrike : Card(CardType.ATTACK, TargetType.ENEMY, count = 2) {

    private val damage = 5

    override val description: String
        get() = "Deal $damage damage twice."

    override fun play(self: Entity, entity: Entity) {
        entity.dealDamage(damage)
    }
}