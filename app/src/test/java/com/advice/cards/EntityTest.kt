package com.advice.cards

import org.junit.Assert.*
import org.junit.Test

class EntityTest {

    @Test
    fun `deal damage`() {
        val entity = Entity(10)

        entity.dealDamage(5)

        assertEquals(0, entity.getBlock())
        assertEquals(5, entity.getHealth())
    }

    @Test
    fun `deal damage through armor`() {
        val entity = Entity(10)
        entity.addBlock(5)

        entity.dealDamage(10)

        assertEquals(0, entity.getBlock())
        assertEquals(5, entity.getHealth())
    }

    @Test
    fun `deal damage does not go through armor`() {
        val entity = Entity(10)
        entity.addBlock(20)

        entity.dealDamage(10)

        assertEquals(10, entity.getBlock())
        assertEquals(10, entity.getHealth())
    }

}




