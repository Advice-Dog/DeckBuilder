package com.advice.cards.encounters.enemies

import com.advice.cards.*
import com.advice.cards.cards.*
import com.advice.cards.cards.status.Vulnerable

class Slaver : Enemy(GameManager.seed.nextInt(46, 50)) {

    override val image = R.drawable.cultist

    private val cards = listOf(
        Stab(),
        Scrape()
    )

    override var intent = cards.first()

    override fun play(target: Entity, self: Entity) {
        intent.play(self, target)
        intent = cards.last()
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
    
    // todo: add in Entangle (Applies 1 Entangled)
}

