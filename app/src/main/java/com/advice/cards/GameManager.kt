package com.advice.cards

import com.advice.cards.cards.Card
import com.advice.cards.cards.CardRarity
import com.advice.cards.cards.colourless.Bandaid
import com.advice.cards.cards.colourless.Finesse
import com.advice.cards.cards.red.attack.*
import com.advice.cards.cards.red.power.Inflame
import com.advice.cards.cards.red.skill.*
import com.advice.cards.encounters.Encounter
import com.advice.cards.encounters.enemies.EnemyGroup
import com.advice.cards.hero.Ironclad

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
        ShrugItOff(),
        Armaments(),
        PerfectedStrike(),


        // uncommon
        SeeingRed(),
        Shockwave(),
        Uppercut(),
        Entrench(),
        FlameBarrier(),
        Inflame(),


        // rare
        Bludgeon()
    )

    private val colourlessCards = listOf(
        Finesse(),
        Bandaid()
    )

    val hero = Ironclad()
    val deck = hero.deck

    var encounter: Encounter? = null

    init {
        encounter =
            Encounter(com.advice.cards.encounters.act.encounters[0])
    }

    fun setEnemyGroup(enemyGroup: EnemyGroup) {
        this.encounter = Encounter(enemyGroup)
    }

    fun startNewEncounter(encounter: Encounter) {
        this.encounter = encounter
    }

    fun endEncounter() {
        this.encounter = null
    }

    fun getCardRewards(): List<Card> {
        val index = RNG.nextInt(100)
        val rarity = getRewardRarity(index)

        val cards = redCards + colourlessCards
        //.filter { it.rarity == rarity }
        return RNG.shuffled(cards).take(3)

    }

    private fun getRewardRarity(index: Int): CardRarity {
        return when {
            index > 90 -> CardRarity.RARE
            index > 80 -> CardRarity.UNCOMMON
            else -> CardRarity.COMMON
        }
    }
}
