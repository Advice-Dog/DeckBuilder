package com.advice.cards.red.attack

import com.advice.cards.*

class IronWave : Card(CardType.ATTACK, TargetType.ENEMY) {

    private val block = 5
    private val damage = 5

    override val description: String
        get() = "Gain $block Block. Deal $damage damage."

    override fun play(self: Entity, entity: Entity) {
        self.addBlock(block)
        entity.dealDamage(damage)
    }
}