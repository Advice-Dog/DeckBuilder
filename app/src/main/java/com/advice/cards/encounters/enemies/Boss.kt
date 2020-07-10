package com.advice.cards.encounters.enemies

import com.advice.cards.*
import com.advice.cards.cards.*
import com.advice.cards.cards.status.Vulnerable

class Boss : Enemy(250) {

    override val image = R.drawable.cultist

    var index = 0

    private val cards = listOf(
        Scrape(),
        Stab(),
        ArmorUp(),
        Beam()
    )

    override var intent = cards.first()

    override fun play(target: Entity, self: Entity) {
        intent.play(self, target)
        index++
        if (index >= cards.size) {
            index = 0
        }
        intent = cards[index]
    }

    private class Stab : Card(CardType.ATTACK, TargetType.ENEMY) {

        private val damage = 12

        init {
            effects.add(DamageEffect(damage))
        }
    }

    private class Scrape : Card(CardType.ATTACK, TargetType.ENEMY) {

        private val damage = 7
        private val vulnerable = 1

        init {
            effects.add(DamageEffect(damage))
            effects.add(
                ApplyStatusEffectEffect(
                    Vulnerable(
                        vulnerable
                    )
                )
            )
        }
    }

    private class ArmorUp : Card(CardType.SKILL, TargetType.SELF) {

        private val block = 20

        init {
            effects.add(BlockEffect(block))
        }
    }

    private class Beam : Card(CardType.ATTACK, TargetType.ENEMY) {

        private val damage = 35

        init {
            effects.add(DamageEffect(damage))
        }
    }

}

