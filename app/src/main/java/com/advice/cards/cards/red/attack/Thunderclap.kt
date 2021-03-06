package com.advice.cards.cards.red.attack

import com.advice.cards.cards.*
import com.advice.cards.cards.status.Vulnerable

class Thunderclap : Card(CardType.ATTACK, TargetType.ALL_ENEMY) {

    private val damage = 4
    private val vulnerable = 1

    init {
        effects.add(DamageEffect(damage))
        effects.add(
            ApplyStatusEffectEffect(
                Vulnerable(
                    vulnerable
                )
            )
        )
    }
}