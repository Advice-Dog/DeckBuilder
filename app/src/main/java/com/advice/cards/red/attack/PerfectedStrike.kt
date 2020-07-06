package com.advice.cards.red.attack

import com.advice.cards.Card
import com.advice.cards.CardType
import com.advice.cards.PerfectedStrikeDamageEffect
import com.advice.cards.TargetType

class PerfectedStrike : Card(CardType.ATTACK, TargetType.ENEMY, energy = 2) {

    private val base = 6
    private val bonus = 2

    init {
        effects.add(PerfectedStrikeDamageEffect(base, bonus))
    }
}