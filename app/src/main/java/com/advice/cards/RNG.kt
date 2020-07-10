package com.advice.cards

import kotlin.random.Random

object RNG {

    private var seed = Random(1024L)

    fun reset() {
        seed = Random(1024L)
    }

    fun <T> random(list: List<T>) = list.random(seed)

    fun <T> shuffled(list: List<T>) = list.shuffled(seed)

    fun <T> shuffle(list: MutableList<T>) {
        list.shuffle(seed)
    }

    fun nextInt() = seed.nextInt()

    fun nextInt(until: Int) = seed.nextInt(until)

    fun nextInt(from: Int, until: Int) = seed.nextInt(from, until)

}