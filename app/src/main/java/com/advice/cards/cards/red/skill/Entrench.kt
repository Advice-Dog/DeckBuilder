package com.advice.cards.cards.red.skill

import com.advice.cards.cards.Card
import com.advice.cards.cards.CardType
import com.advice.cards.cards.DoubleBlockEffect
import com.advice.cards.cards.TargetType

class Entrench : Card(CardType.SKILL, TargetType.SELF, energy = 2) {

    init {
        effects.add(DoubleBlockEffect())
    }

}