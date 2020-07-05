package com.advice.cards.red.attack

import com.advice.cards.*

class Cleave : Card(CardType.ATTACK, TargetType.ALL_ENEMY) {

    private val damage = 8

    override val description: String
        get() = "Deal $damage damage to ALL enemies."

    override fun play(self: Entity, entity: Entity) {
        entity.dealDamage(damage)
    }

}