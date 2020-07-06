package com.advice.cards

import com.advice.cards.status.FlexBuff
import com.advice.cards.status.Vulnerable
import org.junit.Assert.assertEquals
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

    @Test
    fun `get scaled damage base`() {
        val self = Entity(10)
        val entity = Entity(10)

        val amount = entity.getScaledDamage(self, 10)

        assertEquals(10, amount)
    }

    @Test
    fun `get scaled damage with vulnerable`() {
        val self = Entity(10)
        val entity = Entity(10)
        entity.applyStatusEffect(Vulnerable(1))

        val amount = entity.getScaledDamage(self, 10)

        assertEquals(15, amount)
    }

    @Test
    fun `get scaled damage with one strength stack`() {
        val self = Entity(10)
        self.applyStatusEffect(FlexBuff(1))

        val entity = Entity(10)

        val amount = entity.getScaledDamage(self, 10)

        assertEquals(12, amount)
    }

    @Test
    fun `get scaled damage with two strength stacks`() {
        val self = Entity(10)
        self.applyStatusEffect(FlexBuff(1))
        self.applyStatusEffect(FlexBuff(1))

        val entity = Entity(10)

        val amount = entity.getScaledDamage(self, 10)

        assertEquals(14, amount)
    }

    @Test
    fun `get scaled damage with one strength stack and vulnerable`() {
        val self = Entity(10)
        self.applyStatusEffect(FlexBuff(1))

        val entity = Entity(10)
        entity.applyStatusEffect(Vulnerable(1))

        val amount = entity.getScaledDamage(self, 10)

        assertEquals(18, amount)
    }

}




