package com.advice.cards.cards.colourless

import com.advice.cards.cards.*

class Bandaid : Card(CardType.SKILL, TargetType.SELF, CardRarity.RARE, CardColour.COLOURLESS, energy = 0) {

    private val heal = 4

    init {
        effects.add(HealEffect(heal))
    }

}