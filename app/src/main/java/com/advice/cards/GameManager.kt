package com.advice.cards

import com.advice.cards.colourless.Finesse
import com.advice.cards.enemies.*
import com.advice.cards.hero.Ironclad
import com.advice.cards.red.attack.*
import com.advice.cards.red.skill.*
import kotlin.random.Random

object GameManager {

    var seed = Random(1024L)

    val act = act("Act I") {
        // First 3 encounters
        this + group {
            this + Cultist()
        }

        this + group {
            this + JawWorm()
        }

        this + group {
            this + Louse()
            this + Louse()
        }

        // todo: add small slimes

        // blue slaver (12.5%)
        this + group {
            this + Slaver()
        }

        this + group {
            this + Louse()
            this + Louse()
            this + Louse()
        }

        // other
        this + group {
            this + Cultist()
        }

        this + group {
            this + JawWorm()
            this + JawWorm()
        }

        this + group {
            this + Slaver()
        }

        this + group {
            this + Cultist()
        }

        this + group {
            this + JawWorm()
            this + JawWorm()
        }

        this + group {
            this + Slaver()
        }

        this + group {
            this + Slaver()
        }

        this + group {
            this + Boss()
        }
    }


    fun resetSeed() {
        seed = Random(1024L)
    }

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
        //todo: fix flex buff Flex(),
        ShrugItOff(),
        Armaments(),

        // uncommon
        SeeingRed(),
        Shockwave(),
        Uppercut(),

        // rare
        Bludgeon()
    )

    private val colourlessCards = listOf(
        Finesse()//,
        //Bandaid()
    )

    val hero = Ironclad()
    val deck = hero.deck

    var encounter: Encounter? = null

    init {
        encounter = Encounter(act.encounters[0])
    }

    fun setEnemy(enemy: Enemy) {
        val encounter = Encounter(group {
            this + enemy
        })
        this.encounter = encounter
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

    fun getCardRewards(seed: Random = this.seed): List<Card> {
        val index = seed.nextInt(100)
        val rarity = getRewardRarity(index)

        val cards = redCards + colourlessCards

        return cards
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