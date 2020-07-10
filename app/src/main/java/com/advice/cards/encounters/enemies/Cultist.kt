package com.advice.cards.encounters.enemies

import com.advice.cards.*
import com.advice.cards.cards.*
import com.advice.cards.cards.status.StatusEffect

class Cultist : Enemy(GameManager.seed.nextInt(48, 54)) {

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

    private class Incantation : Card(CardType.POWER, TargetType.SELF) {

        private val ritual = 3

        init {
            effects.add(
                ApplyStatusEffectEffect(
                    Ritual(
                        ritual
                    )
                )
            )
        }
    }

    private class DarkStrike : Card(CardType.ATTACK, TargetType.ENEMY) {

        private val damage = 6

        init {
            effects.add(DamageEffect(damage))
        }
    }

    class Ritual(private val amount: Int) : StatusEffect(0) {
        // todo: implement

        override fun toString(): String {
            return "Gain Ritual $amount"
        }
    }
}


