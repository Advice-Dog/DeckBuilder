package com.advice.cards.red.attack

import com.advice.cards.*
import com.advice.cards.status.Vulnerable

class Bash : Card(CardType.ATTACK, TargetType.ENEMY, energy = 2) {

    private val damage = 8
    private val vulnerable = 2

    init {
        effects.add(DamageEffect(damage))
        effects.add(ApplyStatusEffectEffect(Vulnerable(vulnerable)))
    }
}