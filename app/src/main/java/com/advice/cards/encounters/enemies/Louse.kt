package com.advice.cards.encounters.enemies

import com.advice.cards.Enemy
import com.advice.cards.Entity
import com.advice.cards.R
import com.advice.cards.RNG
import com.advice.cards.cards.*
import com.advice.cards.cards.status.FlexBuff

class Louse : Enemy(10) {

    override val image = R.drawable.jaw_worm

    private val cards = listOf(
        Bite(),
        Grow()
    )

    override var intent = cards.first()

    override fun play(target: Entity, self: Entity) {
        intent.play(self, target)
        intent = cards.last()
    }

    private class Bite : Card(CardType.ATTACK, TargetType.ENEMY) {

        private val damage = RNG.nextInt(5, 7)

        init {
            effects.add(DamageEffect(damage))
        }
    }

    private class Grow : Card(CardType.SKILL, TargetType.SELF) {

        private val strength = 3

        init {
            effects.add(ApplyStatusEffectEffect(FlexBuff(strength)))
        }
    }
}

