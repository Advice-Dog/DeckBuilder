package com.advice.cards.encounters.enemies

import com.advice.cards.Enemy

class EnemyGroup {
    val enemies = ArrayList<Enemy>()

//    infix fun EnemyGroup.add(enemy: Enemy) {
//        enemies.add(enemy)
//    }

    operator fun plus(enemy: Enemy) {
        enemies.add(enemy)
    }
}

fun group(init: EnemyGroup.() -> Unit): EnemyGroup {
    val group = EnemyGroup()
    group.init()
    return group
}


fun test() {

    1 shl 3

    group {
        this + Cultist()
    }


}