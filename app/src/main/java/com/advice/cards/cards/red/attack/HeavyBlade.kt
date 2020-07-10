package com.advice.cards.cards.red.attack

import com.advice.cards.cards.Card
import com.advice.cards.cards.CardType
import com.advice.cards.cards.ScaledDamageEffect
import com.advice.cards.cards.TargetType

class HeavyBlade : Card(CardType.ATTACK, TargetType.ENEMY, energy = 2) {

    private val damage = 14
    private val scale = 3

    init {
        effects.add(ScaledDamageEffect(damage, scale))
    }
}