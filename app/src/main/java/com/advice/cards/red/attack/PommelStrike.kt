package com.advice.cards.red.attack

import com.advice.cards.*

class PommelStrike : Card(CardType.ATTACK, TargetType.ENEMY) {

    private val damage = 9
    private val draw = 1

    override val description: String
        get() = "Deal $damage damage. Draw $draw card."

    override fun play(self: Entity, entity: Entity) {
        entity.dealDamage(damage)
        (self as Hero).deck.drawCard(draw)
    }
}