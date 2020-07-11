package com.advice.cards

import com.advice.cards.cards.red.attack.Strike
import com.advice.cards.cards.red.skill.Flex
import com.advice.cards.cards.status.FlexBuff
import com.advice.cards.cards.status.Vulnerable
import com.advice.cards.encounters.enemies.Cultist
import com.advice.cards.hero.Ironclad
import org.junit.Assert.assertEquals
import org.junit.Test

class EntityTest {

    @Test
    fun `deal damage`() {
        val attacker = Entity(10)
        val entity = Entity(10)

        entity.dealDamage(attacker, 5)

        assertEquals(0, entity.getBlock())
        assertEquals(5, entity.getHealth())
    }

    @Test
    fun `deal damage through armor`() {
        val attacker = Entity(10)
        val entity = Entity(10)
        entity.addBlock(5)

        entity.dealDamage(attacker, 10)

        assertEquals(0, entity.getBlock())
        assertEquals(5, entity.getHealth())
    }

    @Test
    fun `deal damage does not go through armor`() {
        val attacker = Entity(10)
        val entity = Entity(10)
        entity.addBlock(20)

        entity.dealDamage(attacker, 10)

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
        self.applyStatusEffect(FlexBuff(2))

        val entity = Entity(10)

        val amount = entity.getScaledDamage(self, 10)

        assertEquals(12, amount)
    }

    @Test
    fun `get scaled damage with two strength stacks`() {
        val self = Entity(10)
        self.applyStatusEffect(FlexBuff(2))
        self.applyStatusEffect(FlexBuff(2))

        val entity = Entity(10)

        val amount = entity.getScaledDamage(self, 10)

        assertEquals(14, amount)
    }

    @Test
    fun `get scaled damage with one strength stack and vulnerable`() {
        val self = Entity(10)
        self.applyStatusEffect(FlexBuff(2))

        val entity = Entity(10)
        entity.applyStatusEffect(Vulnerable(1))

        val amount = entity.getScaledDamage(self, 10)

        assertEquals(18, amount)
    }


    @Test
    fun `creating two heroes will have different decks`() {
        val lhs = Ironclad()
        val rhs = Ironclad()
        lhs.deck.addCard(Flex())


        assertEquals(11, lhs.deck.size)
        assertEquals(10, rhs.deck.size)
    }

    @Test
    fun `creating two heroes will have different cards`() {
        val lhs = Ironclad()
        lhs.applyStatusEffect(FlexBuff(2))
        val rhs = Ironclad()
        val target = Cultist()
        val strike = Strike()

        val description = strike.getDescription(lhs, target)
        val other = strike.getDescription(rhs, target)

        assertEquals("Deal 7 damage.", description)
        assertEquals("Deal 5 damage.", other)
    }
}




