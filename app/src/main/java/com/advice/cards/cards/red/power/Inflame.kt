package com.advice.cards.cards.red.power

import com.advice.cards.cards.ApplyStatusEffectEffect
import com.advice.cards.cards.Card
import com.advice.cards.cards.CardType
import com.advice.cards.cards.TargetType
import com.advice.cards.cards.status.StrengthUp

class Inflame : Card(CardType.POWER, TargetType.SELF) {

    private val strength = 2

    init {
        effects.add(ApplyStatusEffectEffect(StrengthUp(strength, -1)))
    }
}