package com.advice.cards.colourless

import com.advice.cards.*

class Finesse : Card(CardType.SKILL, TargetType.SELF, energy = 0) {

    init {
        effects.add(BlockEffect(2))
        effects.add(DrawCardEffect(1))
    }

}