package com.advice.cards.red.skill

import com.advice.cards.ApplyStatusEffectEffect
import com.advice.cards.Card
import com.advice.cards.CardType
import com.advice.cards.TargetType
import com.advice.cards.status.FlexBuff

class Flex : Card(CardType.SKILL, TargetType.SELF, energy = 0) {

    private val strength = 2

    init {
        effects.add(ApplyStatusEffectEffect(FlexBuff(strength), TargetType.SELF))
    }
}