package com.advice.cards

import com.advice.cards.enemies.Cultist
import com.advice.cards.hero.Ironclad
import com.advice.cards.red.attack.*
import com.advice.cards.red.skill.*
import java.util.*

object GameManager {

    val seed = Random(1024L)

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
        ShrugItOff(),
        Armaments(),

        // uncommon
        SeeingRed(),
        Shockwave(),
        Uppercut(),

        // rare
        Bludgeon()
    )

    val hero = Ironclad()
    val deck = hero.deck

    var encounter: Encounter? = null

    init {
        encounter = Encounter(hero)
        encounter?.reset(Cultist())


        val bonus = redCards.shuffled(seed).take(5)
        //deck.addCards(bonus)
        //deck.addCard(Thunderclap())
        //deck.addCard(Anger())

        deck.addCard(PerfectedStrike())
        deck.addCard(TrueGrit())
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
        val index = seed.nextInt(100)
        val rarity = getRewardRarity(index)

        return redCards
            //.filter { it.rarity == rarity }
            .shuffled(seed).take(3)
    }

    private fun getRewardRarity(index: Int): CardRarity {
        return when {
            index > 90 -> CardRarity.RARE
            index > 80 -> CardRarity.UNCOMMON
            else -> CardRarity.COMMON
        }
    }
}