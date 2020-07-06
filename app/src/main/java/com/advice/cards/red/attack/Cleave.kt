package com.advice.cards.red.attack

import com.advice.cards.Card
import com.advice.cards.CardType
import com.advice.cards.DamageEffect
import com.advice.cards.TargetType

class Cleave : Card(CardType.ATTACK, TargetType.ALL_ENEMY) {

    private val damage = 8

    init {
        effects.add(DamageEffect(damage))
    }
}