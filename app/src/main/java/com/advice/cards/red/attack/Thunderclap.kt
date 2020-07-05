package com.advice.cards.red.attack

import com.advice.cards.*
import com.advice.cards.status.Vulnerable

class Thunderclap : Card(CardType.ATTACK, TargetType.ALL_ENEMY) {

    private val damage = 4
    private val vulnerable = 1

    override val description: String
        get() = "Deal $damage damage and apply $vulnerable Vulnerable to ALL enemies."

    override fun play(self: Entity, entity: Entity) {
        entity.dealDamage(damage)
        entity.applyStatusEffect(Vulnerable(vulnerable))
    }
}