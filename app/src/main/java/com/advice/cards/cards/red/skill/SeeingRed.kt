package com.advice.cards.cards.red.skill

import com.advice.cards.cards.*

class SeeingRed : Card(CardType.SKILL, TargetType.SELF, CardRarity.UNCOMMON) {

    private val gain = 2

    init {
        effects.add(AddEnergyEffect(gain))
    }


}