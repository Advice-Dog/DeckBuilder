package com.advice.cards.cards.red.attack

import com.advice.cards.cards.Card
import com.advice.cards.cards.CardType
import com.advice.cards.cards.DamageEffect
import com.advice.cards.cards.TargetType

class Strike : Card(CardType.ATTACK, TargetType.ENEMY) {

    private val damage = 5

    init {
        effects.add(DamageEffect(damage))
    }
}