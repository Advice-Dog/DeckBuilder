package com.advice.cards.red.skill

import com.advice.cards.*

class SeeingRed : Card(CardType.SKILL, TargetType.SELF, CardRarity.UNCOMMON) {

    private val gain = 2

    init {
        effects.add(AddEnergyEffect(gain))
    }


}