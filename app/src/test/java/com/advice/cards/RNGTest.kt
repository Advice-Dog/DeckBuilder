package com.advice.cards

import org.junit.Assert.assertEquals
import org.junit.Test

class RNGTest {

    @Test
    fun `reset and give same results`() {
        RNG.reset()
        val numbers = ArrayList<Int>()
        for (i in 0 until 250) {
            numbers.add(RNG.nextInt(100))
        }

        RNG.reset()
        for (i in 0 until 250) {
            assertEquals(numbers[i], RNG.nextInt(100))
        }
    }

}