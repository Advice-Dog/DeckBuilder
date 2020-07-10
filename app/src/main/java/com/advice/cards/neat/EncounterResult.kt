package com.advice.cards.neat

import com.advice.cards.Hero
import com.advice.cards.encounters.Encounter
import com.advice.cards.encounters.enemies.Boss
import kotlin.math.max

data class EncounterResult(
    val hero: Hero,
    val encounter: Encounter,
    val turnsTaken: Int,
    val damageTaken: Int
) {

    val hasHitTurnLimit: Boolean
        get() = turnsTaken == TURN_LIMIT

    val fitness: Int
        get() {
            val damageMod = (TURN_LIMIT - turnsTaken) * 10
            val healthMod = max(10, 100 - damageTaken * 10)

            // boss bonus
            if (encounter.target is Boss && encounter.target.isDead && hero.isAlive) {
                return 100_000 + hero.getHealth() * 10_000
            }

            return damageMod + healthMod
        }

    override fun toString() = super.toString() + " $fitness"
}