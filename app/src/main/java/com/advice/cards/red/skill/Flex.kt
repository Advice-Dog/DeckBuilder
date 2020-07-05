package com.advice.cards.red.skill

import com.advice.cards.Card
import com.advice.cards.CardType
import com.advice.cards.Entity
import com.advice.cards.TargetType
import com.advice.cards.status.FlexBuff

class Flex : Card(CardType.SKILL, TargetType.SELF, energy = 0) {

    private val strength = 2

    override val description: String
        get() = "Gain $strength Strength. At the end of this turn, lose 2 Strength."

    override fun play(self: Entity, entity: Entity) {
        self.applyStatusEffect(FlexBuff(strength))
    }

}