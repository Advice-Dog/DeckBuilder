package com.advice.cards.red.attack

import com.advice.cards.*

class HeavyBlade : Card(CardType.ATTACK, TargetType.ENEMY, energy = 2) {

    private val damage = 14
    private val scale = 3

    override val description: String
        get() = "Deal $damage damage. Strength affects this card $scale times."

    override fun play(self: Entity, entity: Entity) {
        // todo: scale damage with strength
        val strength = self.getStrength()
        entity.dealDamage(damage)
    }
}