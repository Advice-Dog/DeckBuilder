package com.advice.cards.neat

import com.advice.cards.Hero
import kotlin.math.max

data class EncounterResult(
    val hero: Hero,
    val turnsTaken: Int,
    val damageTaken: Int
) {
    val fitness: Int
        get() {
            val damageMod = (TURN_LIMIT - turnsTaken) * 10
            val healthMod = max(10, 100 - damageTaken * 10)
            return damageMod + healthMod
        }

    override fun toString() = super.toString() + " $fitness"
}