package com.advice.cards.encounters.enemies

import com.advice.cards.Enemy

class EnemyGroup(val id: Int) : Cloneable {
    val enemies = ArrayList<Enemy>()

    operator fun plus(enemy: Enemy) {
        enemies.add(enemy)
    }

    public override fun clone(): EnemyGroup {
        val clone = enemies.map { it.clone() as Enemy }
        return group(id) {
            clone.forEach {
                this + it
            }
        }
    }

    override fun toString() = "EnemyGroup: [ " + enemies.joinToString { it.toString() } + " ]"
}

var index = 1

fun group(id: Int = index++, init: EnemyGroup.() -> Unit): EnemyGroup {
    val group = EnemyGroup(id)
    group.init()
    return group
}