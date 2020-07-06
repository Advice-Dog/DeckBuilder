package com.advice.cards.red.attack

import com.advice.cards.BlockDamageEffect
import com.advice.cards.Card
import com.advice.cards.CardType
import com.advice.cards.TargetType

class BodySlam : Card(CardType.ATTACK, TargetType.ENEMY) {

    init {
        effects.add(BlockDamageEffect())
    }
}