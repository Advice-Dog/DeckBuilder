package com.advice.cards.enemies

import com.advice.cards.*
import com.advice.cards.status.FlexBuff

class Louse : Enemy(10) {

    override val image = R.drawable.jaw_worm

    private val cards = listOf(
        Bite(),
        Grow()
    )

    override var intent = cards.random(GameManager.seed)

    override fun play(target: Entity, self: Entity) {
        intent.play(self, target)
        intent = cards.random(GameManager.seed)
    }

    private class Bite : Card(CardType.ATTACK, TargetType.ENEMY) {

        private val damage = GameManager.seed.nextInt(5, 7)

        override val description: String
            get() = "Deal $damage damage."

        override fun play(self: Entity, entity: Entity) {
            entity.dealDamage(damage)
        }
    }

    private class Grow : Card(CardType.SKILL, TargetType.SELF) {

        private val strength = 3

        override val description: String
            get() = "Gain $strength Strength."

        override fun play(self: Entity, entity: Entity) {
            self.applyStatusEffect(FlexBuff(strength))
        }
    }
}

