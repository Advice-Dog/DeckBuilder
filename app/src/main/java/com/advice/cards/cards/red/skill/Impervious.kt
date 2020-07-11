package com.advice.cards.cards.red.skill

import com.advice.cards.cards.*

class Impervious : Card(CardType.SKILL, TargetType.SELF, CardRarity.RARE) {

    private val amount = 30

    init {
        effects.add(BlockEffect(amount))
        effects.add(ExhaustEffect(this))
    }
}