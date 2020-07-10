package com.advice.cards.encounters.enemies

import com.advice.cards.Enemy
import com.advice.cards.Entity
import com.advice.cards.R
import com.advice.cards.RNG
import com.advice.cards.cards.*

class JawWorm(hp: Int = RNG.nextInt(40, 44)) : Enemy(hp) {

    override val image = R.drawable.jaw_worm

    private val cards = listOf(
        Chomp(),
        Thrash()
    )

    override var intent = RNG.random(cards)

    override fun play(target: Entity, self: Entity) {
        intent.play(self, target)
        intent = RNG.random(cards)
    }

    private class Chomp : Card(CardType.ATTACK, TargetType.ENEMY) {

        private val damage = 11

        init {
            effects.add(DamageEffect(damage))
        }
    }

    private class Thrash : Card(CardType.ATTACK, TargetType.ENEMY) {

        private val damage = 6
        private val block = 5

        init {
            effects.add(DamageEffect(damage))
            effects.add(BlockEffect(block))
        }
    }
}