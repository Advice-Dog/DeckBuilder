package com.advice.cards.red.skill

import com.advice.cards.*

class Defend : Card(CardType.SKILL, TargetType.SELF) {

    private val amount = 5

    override fun play(self: Entity, entity: Entity) {
        self.addBlock(amount)
    }

    override val description: String
        get() = "Gain $amount Block."

}