package com.advice.cards.enemies

import com.advice.cards.*

class JawWorm : Enemy(40) {

    override val image = R.drawable.jaw_worm

    private val cards = listOf(
        Chomp(),
        Thrash()
    )

    override var intent = cards.random()

    override fun play(target: Entity, self: Entity) {
        intent.play(self, target)
        intent = cards.random()
    }
}

class Chomp : Card(CardType.ATTACK, TargetType.ENEMY) {

    private val damage = 11

    override val description: String
        get() = "Deal $damage damage."

    override fun play(self: Entity, entity: Entity) {
        entity.dealDamage(damage)
    }
}

class Thrash : Card(CardType.ATTACK, TargetType.ENEMY) {

    private val damage = 7
    private val block = 5

    override val description: String
        get() = "Deal $damage damage, gain $block Block."

    override fun play(self: Entity, entity: Entity) {
        entity.dealDamage(damage)
        self.addBlock(block)
    }
}

