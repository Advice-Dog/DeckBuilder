package com.advice.cards

import com.advice.cards.enemies.JawWorm
import com.advice.cards.hero.Ironclad
import com.advice.cards.red.attack.*
import com.advice.cards.red.skill.Flex
import com.advice.cards.red.skill.ShrugItOff

object GameManager {

    private val redCards = listOf(
        Anger(),
        Bash(),
        BodySlam(),
        Clash(),
        Cleave(),
        Clothesline(),
        HeavyBlade(),
        IronWave(),
        PommelStrike(),
        //Strike(), starter card
        Thunderclap(),
        TwinStrike(),
        //Defend(), starter card
        Flex(),
        ShrugItOff()
    )

    val hero = Ironclad()
    val deck = hero.deck

    var encounter: Encounter? = null

    init {
        encounter = Encounter(hero)
        encounter?.reset(JawWorm())
    }

    fun setEnemy(enemy: Enemy) {
        val encounter = Encounter(hero)
        encounter.reset(enemy)
        this.encounter = encounter
    }

    fun startNewEncounter(encounter: Encounter) {
        this.encounter = encounter
    }

    fun endEncounter() {
        this.encounter = null
    }

    fun getCardRewards(): List<Card> {
        return redCards.shuffled().take(3)
    }


}