package com.advice.cards.encounters.enemies

class Act : Cloneable {

    val encounters = ArrayList<EnemyGroup>()

    operator fun plus(group: EnemyGroup) {
        encounters.add(group)
    }

    public override fun clone(): Act {
        val previous = encounters

        return act("act") {
            previous.forEach { group ->
                this + group.clone()
            }
        }
    }
}

fun act(name: String, init: Act.() -> Unit): Act {
    val act = Act()
    act.init()
    return act
}

