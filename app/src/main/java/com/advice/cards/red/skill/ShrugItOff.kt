package com.advice.cards.red.skill

import com.advice.cards.*

class ShrugItOff : Card(CardType.SKILL, TargetType.ENEMY) {

    private val block = 8
    private val draw = 1

    override val description: String
        get() = "Gain $block Block. Draw $draw card."

    override fun play(self: Entity, entity: Entity) {
        self.addBlock(block)
        (self as Hero).deck.drawCard(draw)
    }
}