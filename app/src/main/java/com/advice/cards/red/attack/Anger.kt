package com.advice.cards.red.attack

import com.advice.cards.*

class Anger : Card(CardType.ATTACK, TargetType.ENEMY, energy = 0) {

    private val damage = 6

    override val description: String
        get() = "Deal $damage damage. Add a copy of this card into your discard pile."

    override fun play(self: Entity, entity: Entity) {
        entity.dealDamage(damage)
        // todo: make this copy only last this encounter
        (self as Hero).deck.addCard(this)
    }
}