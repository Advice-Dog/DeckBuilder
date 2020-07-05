package com.advice.cards.enemies

import com.advice.cards.*
import com.advice.cards.status.StatusEffect

class Cultist : Enemy(48) {

    override val image = R.drawable.cultist

    private val cards = listOf(
        Incantation(),
        DarkStrike()
    )

    override var intent = cards.first()

    override fun play(target: Entity, self: Entity) {
        intent.play(self, target)
        intent = cards.last()
    }
}

class Incantation : Card(CardType.POWER, TargetType.SELF) {

    private val ritual = 3

    override val description: String
        get() = "Gain Ritual $ritual"

    override fun play(self: Entity, entity: Entity) {
        self.applyStatusEffect(Ritual(ritual))
    }
}

class DarkStrike : Card(CardType.ATTACK, TargetType.ENEMY) {

    private val damage = 6

    override val description: String
        get() = "Deal $damage damage."

    override fun play(self: Entity, entity: Entity) {
        entity.dealDamage(damage)
    }
}

class Ritual(private val amount: Int) : StatusEffect(-1) {


}
