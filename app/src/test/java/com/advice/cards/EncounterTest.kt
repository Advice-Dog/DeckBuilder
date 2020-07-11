package com.advice.cards

import com.advice.cards.encounters.enemies.JawWorm
import com.advice.cards.encounters.enemies.group
import com.advice.cards.cards.red.skill.Defend
import com.advice.cards.cards.red.attack.Strike
import com.advice.cards.cards.status.Vulnerable
import com.advice.cards.encounters.Encounter
import com.advice.cards.hero.Ironclad
import junit.framework.Assert.assertEquals
import org.junit.Test

class EncounterTest {

    private val group = group {
        this + JawWorm()
    }

    @Test
    fun `is not complete at start`() {
        val encounter = Encounter(group)

        assert(encounter.hero.isAlive)
        assert(encounter.target.isAlive)
        assert(!encounter.isComplete)
    }

    @Test
    fun `kill target and is complete`() {
        val encounter = Encounter(group)
        val card = Strike()

        encounter.play(card)

        assert(encounter.hero.isAlive)
        assert(encounter.target.isDead)
        assert(encounter.isComplete)
    }

    @Test
    fun `end turn and take damage`() {
        val encounter = Encounter(group)

        encounter.endTurn()

        assert(encounter.hero.isDead)
        assert(encounter.target.isAlive)
        assert(encounter.isComplete)
    }

    @Test
    fun `add armor and end turn and take no damage`() {
        val encounter = Encounter(group)
        val card = Defend()

        encounter.play(card)
        encounter.endTurn()

        assert(encounter.hero.isAlive)
        assert(encounter.target.isAlive)
        assert(!encounter.isComplete)
    }

    @Test
    fun `status effects are cleared between encounters`() {
        val hero = Ironclad()
        val encounter = Encounter(group.clone())
        encounter.hero = hero

        encounter.onEncounterStart()
        hero.statusEffects.add(Vulnerable(10))
        encounter.onEncounterEnd()

        val encounter2 = Encounter(group.clone())
        encounter2.hero = hero
        encounter2.onEncounterStart()

        assertEquals(0, encounter2.hero.statusEffects.size)

    }
}