package com.advice.cards.cards.red.skill

import com.advice.cards.cards.BlockEffect
import com.advice.cards.cards.Card
import com.advice.cards.cards.CardType
import com.advice.cards.cards.TargetType

class Defend : Card(CardType.SKILL, TargetType.SELF) {

    private val amount = 5

    init {
        effects.add(BlockEffect(amount))
    }
}