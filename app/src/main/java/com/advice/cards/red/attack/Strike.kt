package com.advice.cards.red.attack

import com.advice.cards.Card
import com.advice.cards.CardType
import com.advice.cards.DamageEffect
import com.advice.cards.TargetType

class Strike : Card(CardType.ATTACK, TargetType.ENEMY) {

    private val damage = 5

    init {
        effects.add(DamageEffect(damage))
    }
}