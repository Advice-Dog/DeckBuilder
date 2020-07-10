package com.advice.cards.cards.red.attack

import com.advice.cards.cards.BlockDamageEffect
import com.advice.cards.cards.Card
import com.advice.cards.cards.CardType
import com.advice.cards.cards.TargetType

class BodySlam : Card(CardType.ATTACK, TargetType.ENEMY) {

    init {
        effects.add(BlockDamageEffect())
    }
}