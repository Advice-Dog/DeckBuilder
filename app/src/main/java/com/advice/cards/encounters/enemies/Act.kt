package com.advice.cards.encounters.enemies

class Act {

    val encounters = ArrayList<EnemyGroup>()

    operator fun plus(group: EnemyGroup) {
        encounters.add(group)
    }

    // todo: remove
    fun reset() {
        encounters.forEach {
            it.enemies.forEach {
                it.healDamage(100)
            }
        }
    }
}

fun act(name: String, init: Act.() -> Unit): Act {
    val act = Act()
    act.init()
    return act
}

