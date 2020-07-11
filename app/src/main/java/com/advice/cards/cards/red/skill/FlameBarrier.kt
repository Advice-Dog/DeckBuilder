package com.advice.cards.cards.red.skill

import com.advice.cards.cards.*
import com.advice.cards.cards.status.Thorns

class FlameBarrier : Card(CardType.SKILL, TargetType.SELF, energy = 2) {

    private val block = 12
    private val damage = 4

    init {
        effects.add(BlockEffect(block))
        effects.add(ApplyStatusEffectEffect(Thorns(damage)))
    }
}