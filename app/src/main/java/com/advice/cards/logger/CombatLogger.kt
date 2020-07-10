package com.advice.cards.logger

import com.advice.cards.Entity
import com.advice.cards.Hero
import com.advice.cards.cards.Card
import com.advice.cards.cards.Deck
import com.advice.cards.encounters.Encounter

object CombatLogger : BaseLogger() {

    var isEnabled: Boolean = false

    private fun add(string: String) {
        if (isEnabled) {
            log.add(string)
        }
    }

    private val log = ArrayList<String>()

    override fun onMessage(message: String) {
        add(message)
    }

    override fun toString() = log.joinToString(separator = "\n")

    fun reset() {
        log.clear()
    }

    fun append(message: String) {
        log.add(message)
    }

    private var hero: Hero? = null
    private var encounter: Encounter? = null

    fun setHero(hero: Hero) {
        this.hero = hero
        reset()
    }

    fun onNextEncounter(
        encounter: Encounter,
        encountersComplete: Int
    ) {
        this.encounter = encounter
        add("==========================================================")
        add("                    Next Encounter ($encountersComplete)")
        add("==========================================================")
    }


    fun onNextTurn(hand: ArrayList<Card>) {
        add("==========================================================")
        add("                Turn ${encounter?.turnCounter}")
        add("==========================================================")
        addEntitiesASCI(encounter!!)
        add("Hand: ${hand.joinToString { it.name }}")
    }


    fun onCardPlayed(card: Card, entity: Entity, target: Entity) {
        val description = card.getDescription(entity, target)
        add(
            " ${entity.javaClass.simpleName} playing [${card.name}: ${description.replace(
                "\n",
                " "
            )}]"
        )
    }

    fun onError(error: String) {
        add(error)
    }

    fun setDeck(deck: Deck) {
        add("Deck:")
        add(deck.toString())
    }

    fun addCard(rewards: List<Card>, card: Card?) {
        add("==========================================================")
        add(" Rewards: " + rewards.joinToString { it.name })
        add(" Choosing: " + card?.name)
        add("==========================================================")
    }

    fun print() {
        log.forEach {
            println(it)
        }
    }

    private fun addEntitiesASCI(encounter: Encounter) {
        val hero = getEntityASCI(encounter.hero)

        val enemies = ArrayList<String>()
        for (i in hero.indices) {
            enemies.add("    ")
        }

        encounter.enemies.map {
            val enemy = getEntityASCI(it)
            for (i in hero.indices) {
                enemies[i] = enemies[i] + " " + enemy[i]
            }
        }

        for (i in hero.indices) {
            val string = hero[i] + "  " + enemies[i]
            add(string)
        }
    }

    private fun getCenteredText(text: String = ""): String {
        val padding = (16 - text.length) / 2
        return "|" + text.padStart(padding + text.length, ' ').padEnd(16, ' ') + "|"
    }

    private fun getEntityASCI(entity: Entity): List<String> {
        val result = ArrayList<String>()

        result.add(getCenteredText())
        result.add(getCenteredText(entity.javaClass.simpleName))
        result.add(getCenteredText(entity.toString()))
        result.add(getCenteredText())

        return result
    }
}