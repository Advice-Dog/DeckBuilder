package com.advice.cards.red.attack

import com.advice.cards.*
import com.advice.cards.status.Weak

class Clothesline : Card(CardType.ATTACK, TargetType.ENEMY, energy = 2) {

    private val damage = 12
    private val weak = 2

    override val description: String
        get() = "Deal $damage damage. Apply $weak Weak."

    override fun play(self: Entity, entity: Entity) {
        entity.dealDamage(damage)
        entity.applyStatusEffect(Weak(weak))
    }
}