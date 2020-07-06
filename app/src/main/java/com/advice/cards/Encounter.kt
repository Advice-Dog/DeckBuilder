package com.advice.cards

import com.advice.cards.TargetType.*
import com.advice.cards.enemies.JawWorm
import com.advice.cards.logger.BaseLogger
import com.advice.cards.logger.CombatLogger

class Encounter(val hero: Hero = Hero(), val logger: BaseLogger = BaseLogger()) {

    var target: Enemy = JawWorm()

    val enemies = mutableListOf(target)

    private var turnCounter = 1

    init {
        hero.deck.shuffle()

        CombatLogger.onMessage("Turn $turnCounter")
    }

    val isComplete: Boolean
        get() = enemies.none { it.isAlive } || hero.isDead

    fun playCard(card: Card) {
        if (!card.canPlay(hero)) {
            logger.onMessage("Cannot use ${card.name}.")
            return
        }

        logger.onMessage("Playing $card")

        hero.useEnergy(card.energy)

        for (i in 1..card.count) {
            when (card.target) {
                SELF -> card.play(hero, target)
                ENEMY -> card.play(hero, target)
                ALL_ENEMY -> enemies.forEach { card.play(hero, it) }
                RANDOM_ENEMY -> TODO()
            }
        }

        hero.deck.discardCard(card)
    }

    fun endTurn() {
        target.endTurn()

        target.play(hero, target)

        target.tick()

        hero.endTurn()

        turnCounter++
        CombatLogger.onMessage("Turn $turnCounter")

        hero.startTurn()
    }

    fun start() {
        hero.startTurn()
        hero.deck.startCombat()
    }

    fun reset(enemy: Enemy) {
        hero.healDamage(100)

        target = enemy
        enemies.clear()
        enemies.add(target)
    }

    fun end() {
        hero.deck.endCombat()
    }
}