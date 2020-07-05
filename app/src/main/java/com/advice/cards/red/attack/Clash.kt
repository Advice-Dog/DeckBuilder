package com.advice.cards.red.attack

import com.advice.cards.*

class Clash : Card(CardType.ATTACK, TargetType.ENEMY) {

    private val damage = 14

    override val description: String
        get() = "Can only be played if every card in your hand is an Attack. Deal $damage damage."

    override fun canPlay(hero: Hero): Boolean {
        return super.canPlay(hero) && hero.deck.hand.all { it.type == CardType.ATTACK }
    }

    override fun play(self: Entity, entity: Entity) {
        entity.dealDamage(damage)
    }

}