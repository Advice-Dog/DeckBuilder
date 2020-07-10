package com.advice.cards.encounters

import com.advice.cards.Enemy
import com.advice.cards.GameManager
import com.advice.cards.Hero
import com.advice.cards.cards.Card
import com.advice.cards.cards.TargetType.*
import com.advice.cards.encounters.enemies.EnemyGroup
import com.advice.cards.logger.CombatLogger

class Encounter(enemyGroup: EnemyGroup) {

    val id: Int
        get() = 1
    
    var hero: Hero =
        GameManager.hero
    private val deck = hero.deck

    val enemies = ArrayList<Enemy>()

    var turnCounter = 1

    val isComplete: Boolean
        get() = enemies.none { it.isAlive } || hero.isDead

    // todo: remove
    val target: Enemy
        get() = enemies.firstOrNull { it.isAlive } ?: enemies.first()

    init {
        enemies.addAll(enemyGroup.enemies)
    }

    fun setPlayer(hero: Hero) {
        this.hero = hero
    }

    fun play(card: Card) {
        hero.useEnergy(card.energy)

        when (card.target) {
            SELF -> card.play(hero, target)
            ENEMY -> card.play(hero, target)
            ALL_ENEMY -> enemies.forEach { card.play(hero, it) }
            RANDOM_ENEMY -> card.play(hero, enemies.random())
        }

        deck.play(card)
    }

    fun endTurn() {
        onTurnEnd()

        hero.endTurn()

        onTurnStart()
    }

    fun onEncounterStart() {
        hero.start()
    }

    fun onTurnStart() {
        CombatLogger.onMessage("Turn ${turnCounter++}")
        hero.onTurn()
    }

    fun onTurnEnd() {
        // clear any block
        target.endTurn()
        if (target.isAlive) {
            target.play(hero, target)
        }
        // remove any buffs
        target.tick()
    }

    fun reset(enemy: Enemy) {
        hero.healDamage(100)

        enemies.clear()
        enemies.add(target)
    }

    fun onEncounterEnd() {
        hero.end()
    }
}