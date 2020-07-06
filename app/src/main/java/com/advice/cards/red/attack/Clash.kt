package com.advice.cards.red.attack

import com.advice.cards.*

class Clash : Card(CardType.ATTACK, TargetType.ENEMY) {

    private val damage = 14
    
    init {
        effects.add(DamageEffect(damage))
    }

    override fun canPlay(hero: Hero): Boolean {
        return super.canPlay(hero) && hero.deck.hand.all { it.type == CardType.ATTACK }
    }

    override fun getDescription(self: Entity, target: Entity?): String {
        val description = super.getDescription(self, target)
        return "Can only be played if every card in your hand is an Attack.\n$description"
    }
}