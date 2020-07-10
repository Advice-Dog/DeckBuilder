package com.advice.cards.cards.red.attack

import com.advice.cards.cards.Card
import com.advice.cards.cards.CardType
import com.advice.cards.cards.DamageEffect
import com.advice.cards.cards.TargetType

class Cleave : Card(CardType.ATTACK, TargetType.ALL_ENEMY) {

    private val damage = 8

    init {
        effects.add(DamageEffect(damage))
    }
}