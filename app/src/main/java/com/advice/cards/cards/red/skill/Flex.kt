package com.advice.cards.cards.red.skill

import com.advice.cards.cards.ApplyStatusEffectEffect
import com.advice.cards.cards.Card
import com.advice.cards.cards.CardType
import com.advice.cards.cards.TargetType
import com.advice.cards.cards.status.FlexBuff

class Flex : Card(CardType.SKILL, TargetType.SELF, energy = 0) {

    private val strength = 2

    init {
        effects.add(
            ApplyStatusEffectEffect(
                FlexBuff(
                    strength
                ), TargetType.SELF
            )
        )
    }
}