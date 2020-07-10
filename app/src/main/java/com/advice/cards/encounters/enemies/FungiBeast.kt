package com.advice.cards.encounters.enemies

import com.advice.cards.*
import com.advice.cards.cards.*
import com.advice.cards.cards.status.FlexBuff

class FungiBeast : Enemy(RNG.nextInt(22, 28)) {

    override val image = R.drawable.jaw_worm

    private val cards = listOf(
        Bite(),
        Grow()
    )

    override var intent = RNG.random(cards)

    override fun play(target: Entity, self: Entity) {
        intent.play(self, target)
        intent = RNG.random(cards)
    }

    private class Bite : Card(CardType.ATTACK, TargetType.ENEMY) {

        private val damage = 7

        init {
            effects.add(DamageEffect(damage))
        }
    }

    private class Grow : Card(CardType.SKILL, TargetType.SELF) {

        private val strength = 3

        init {
            effects.add(
                ApplyStatusEffectEffect(
                    FlexBuff(
                        strength
                    )
                )
            )
        }
    }
}


