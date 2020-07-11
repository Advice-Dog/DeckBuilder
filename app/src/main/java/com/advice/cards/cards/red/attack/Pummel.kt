package com.advice.cards.cards.red.attack

import com.advice.cards.cards.*

class Pummel : Card(CardType.ATTACK, TargetType.ENEMY) {

    init {
        effects.add(DamageEffect(2))
        effects.add(DamageEffect(2))
        effects.add(DamageEffect(2))
        effects.add(DamageEffect(2))
        effects.add(ExhaustEffect(this))
    }

}