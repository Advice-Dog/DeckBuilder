package com.advice.cards

import com.advice.cards.red.skill.Defend
import com.advice.cards.red.attack.Strike
import org.junit.Test

class EncounterTest {

    @Test
    fun `is not complete at start`() {
        val encounter = Encounter()

        assert(encounter.hero.isAlive)
        assert(encounter.target.isAlive)
        assert(!encounter.isComplete)
    }

    @Test
    fun `kill target and is complete`() {
        val encounter = Encounter()
        val card = Strike()

        encounter.playCard(card)

        assert(encounter.hero.isAlive)
        assert(encounter.target.isDead)
        assert(encounter.isComplete)
    }

    @Test
    fun `end turn and take damage`() {
        val encounter = Encounter()

        encounter.endTurn()

        assert(encounter.hero.isDead)
        assert(encounter.target.isAlive)
        assert(encounter.isComplete)
    }

    @Test
    fun `add armor and end turn and take no damage`() {
        val encounter = Encounter()
        val card = Defend()

        encounter.playCard(card)
        encounter.endTurn()

        assert(encounter.hero.isAlive)
        assert(encounter.target.isAlive)
        assert(!encounter.isComplete)
    }
}