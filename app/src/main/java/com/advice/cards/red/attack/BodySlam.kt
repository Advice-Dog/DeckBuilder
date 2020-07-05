package com.advice.cards.red.attack

import com.advice.cards.*

class BodySlam : Card(CardType.ATTACK, TargetType.ENEMY) {

    override val description: String
        get() = "Deal damage equal to your Block."

    override fun play(self: Entity, entity: Entity) {
        entity.dealDamage(self.getBlock())
    }
}