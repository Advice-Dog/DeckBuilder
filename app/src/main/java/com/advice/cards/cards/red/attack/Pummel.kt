package com.advice.cards.cards.red.attack

import com.advice.cards.cards.Card
import com.advice.cards.cards.CardType
import com.advice.cards.cards.DamageEffect
import com.advice.cards.cards.TargetType

class Pummel : Card(CardType.ATTACK, TargetType.ENEMY) {

    init {
        effects.add(DamageEffect(2))
        effects.add(DamageEffect(2))
        effects.add(DamageEffect(2))
        effects.add(DamageEffect(2))

        // todo: exhaust
    }

}