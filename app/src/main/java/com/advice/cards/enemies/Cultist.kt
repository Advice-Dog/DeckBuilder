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

    private val effect = DamageEffect(damage)

    init {
        effects.add(effect)
    }

    override val description: String
        get() = effect.toString()

    override fun play(self: Entity, entity: Entity) {
        effects.forEach { it.apply(self, entity) }
    }
}

class Ritual(private val amount: Int) : StatusEffect(0) {


}
