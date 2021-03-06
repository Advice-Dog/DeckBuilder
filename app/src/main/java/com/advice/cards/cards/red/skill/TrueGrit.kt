package com.advice.cards.cards.red.skill

import com.advice.cards.cards.*

class TrueGrit : Card(CardType.SKILL, TargetType.SELF) {

    private val block = 7

    init {
        effects.add(BlockEffect(block))
        effects.add(ExhaustCardEffect(ExhaustCardEffect.Target.RANDOM))
    }

}