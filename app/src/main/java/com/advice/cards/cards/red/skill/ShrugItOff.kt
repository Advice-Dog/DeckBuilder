package com.advice.cards.cards.red.skill

import com.advice.cards.cards.*

class ShrugItOff : Card(CardType.SKILL, TargetType.ENEMY) {

    private val block = 8
    private val draw = 1

    init {
        effects.add(BlockEffect(block))
        effects.add(DrawCardEffect(draw))
    }
}