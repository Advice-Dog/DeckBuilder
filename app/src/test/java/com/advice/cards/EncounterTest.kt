package com.advice.cards

import com.advice.cards.encounters.enemies.JawWorm
import com.advice.cards.encounters.enemies.group
import com.advice.cards.cards.red.skill.Defend
import com.advice.cards.cards.red.attack.Strike
import com.advice.cards.encounters.Encounter
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
}