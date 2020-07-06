package com.advice.cards.red.attack

import com.advice.cards.*
import com.advice.cards.status.Weak

class Clothesline : Card(CardType.ATTACK, TargetType.ENEMY, energy = 2) {

    private val damage = 12
    private val weak = 2

    init {
        effects.add(DamageEffect(damage))
        effects.add(ApplyStatusEffectEffect(Weak(weak)))
    }
}