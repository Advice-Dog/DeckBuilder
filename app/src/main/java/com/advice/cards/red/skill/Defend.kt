package com.advice.cards.red.skill

import com.advice.cards.BlockEffect
import com.advice.cards.Card
import com.advice.cards.CardType
import com.advice.cards.TargetType

class Defend : Card(CardType.SKILL, TargetType.SELF) {

    private val amount = 5

    init {
        effects.add(BlockEffect(amount))
    }
}