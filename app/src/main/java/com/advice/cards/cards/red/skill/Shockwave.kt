package com.advice.cards.cards.red.skill

import com.advice.cards.cards.*
import com.advice.cards.cards.status.Vulnerable
import com.advice.cards.cards.status.Weak

class Shockwave : Card(CardType.SKILL, TargetType.ALL_ENEMY, CardRarity.UNCOMMON, energy = 2) {

    private val amount = 3

    init {
        effects.add(ApplyStatusEffectEffect(Weak(amount)))
        effects.add(
            ApplyStatusEffectEffect(
                Vulnerable(
                    amount
                )
            )
        )
    }

}