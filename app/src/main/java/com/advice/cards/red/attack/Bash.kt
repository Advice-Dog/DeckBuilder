package com.advice.cards.red.attack

import com.advice.cards.*
import com.advice.cards.status.Vulnerable

class Bash : Card(CardType.ATTACK, TargetType.ENEMY, energy = 2) {

    private val damage = 8
    private val vulnerable = 2

    override fun play(self: Entity, entity: Entity) {
        entity.dealDamage(damage)
        entity.applyStatusEffect(Vulnerable(vulnerable))
    }

    override val description: String
        get() = "Deal $damage damage. Apply $vulnerable Vulnerable."


}