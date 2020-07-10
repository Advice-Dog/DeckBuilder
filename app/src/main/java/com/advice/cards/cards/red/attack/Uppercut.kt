package com.advice.cards.cards.red.attack

import com.advice.cards.cards.*
import com.advice.cards.cards.status.Vulnerable
import com.advice.cards.cards.status.Weak

class Uppercut : Card(CardType.ATTACK, TargetType.ENEMY, CardRarity.UNCOMMON, energy = 2) {

    private val damage = 13
    private val weak = 1
    private val vulnerable = 2

    init {
        effects.add(DamageEffect(damage))
        effects.add(ApplyStatusEffectEffect(Weak(weak)))
        effects.add(
            ApplyStatusEffectEffect(
                Vulnerable(
                    vulnerable
                )
            )
        )
    }
}