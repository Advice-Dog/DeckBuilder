package com.advice.cards.neat

import com.advice.cards.encounters.Encounter
import com.advice.cards.encounters.enemies.Boss
import kotlin.math.max

data class EncounterResult(
    val previousHealth: Int,
    val currentHealth: Int,
    val encounter: Encounter,
    val turnsTaken: Int
) {

    val isComplete: Boolean
        get() = encounter.enemies.all { it.isDead }

    val hasHitTurnLimit: Boolean = turnsTaken == TURN_LIMIT

    private val isAlive: Boolean = currentHealth > 0

    private val damageTaken = previousHealth - currentHealth

    val fitness: Int
        get() {
            // boss bonus
            if (encounter.target is Boss) {
                if (encounter.target.isDead && isAlive) {
                    return 25_000 + currentHealth * 10_000
                }

                return 100 * (encounter.target.getMaxHealth() - encounter.target.getHealth())
            }

            if (turnsTaken == TURN_LIMIT) {
                return -100
            }

//            if (!isAlive) {
//                return 0
//            }


            val damageMod = encounter.enemies.sumBy { it.getMaxHealth() - it.getHealth() }
            //val speedMod = (TURN_LIMIT - turnsTaken) * 10
            val healthMod = max(10, 100 - damageTaken * 10)



            return encounter.id * (healthMod + damageMod)
        }

    override fun toString() =
        "EncounterResult(enemies=[${encounter.enemies.joinToString { it.javaClass.simpleName }}], fitness=$fitness, damageTaken=$damageTaken, turnsTaken=$turnsTaken)"
}