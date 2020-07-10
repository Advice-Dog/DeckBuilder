package com.advice.cards.encounters.enemies

import com.advice.cards.*
import com.advice.cards.cards.*

class JawWorm(hp: Int = GameManager.seed.nextInt(40, 44)) : Enemy(hp) {

    override val image = R.drawable.jaw_worm

    private val cards = listOf(
        Chomp(),
        Thrash()
    )

    override var intent = cards.random(GameManager.seed)

    override fun play(target: Entity, self: Entity) {
        intent.play(self, target)
        intent = cards.random(GameManager.seed)
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