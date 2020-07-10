package com.advice.cards.neat

import com.advice.cards.Hero

data class ActResult(
    val hero: Hero,
    val encounters: ArrayList<EncounterResult> = ArrayList()
) {
    val fitness: Int
        get() = encounters.sumBy { it.fitness } + if (hero.isAlive) 5000 else 0
}