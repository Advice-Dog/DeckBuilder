package com.advice.cards.neat

import com.advice.cards.Hero

data class ActResult(
    val hero: Hero,
    val encounters: ArrayList<EncounterResult> = ArrayList()
) {
    val fitness: Int
        get() = encounters.sumBy { it.fitness }

    val completedEncounters: Int
        get() = encounters.count { it.isComplete }

    override fun toString(): String {
        return "ActResult(fitness=$fitness, completedEncounters=$completedEncounters, encounters=[${encounters.joinToString { it.toString() }}])"
    }
}