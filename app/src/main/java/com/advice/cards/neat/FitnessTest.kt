package com.advice.cards.neat

import com.advice.cards.GameManager
import com.advice.cards.Hero
import com.advice.cards.cards.Card
import com.advice.cards.encounters.Encounter
import com.advice.cards.encounters.act
import com.advice.cards.encounters.enemies.Boss
import com.advice.cards.hero.Ironclad
import com.advice.cards.logger.CombatLogger
import com.evo.NEAT.Genome

fun getGameResult(genome: Genome): ActResult {
    val hero = Ironclad()

    genomeStartTimer = System.currentTimeMillis()

    var encountersComplete = 0

    val encounters = ArrayList<EncounterResult>()

    val act = act.clone()

    while (hero.isAlive && encountersComplete < act.encounters.size) {
        val encounter = Encounter(act.encounters[encountersComplete])

        val result = getEncounterResult(hero, encounter, genome)
        encounters.add(result)

        if (result.hasHitTurnLimit) {
            break
        }

        if (hero.isAlive) {
            encountersComplete++
            addCard(genome, hero)


            // todo: remove, this is hardcoded artifact
            hero.healDamage(6)
        }
    }

    if (encountersComplete > mostEncountersPerGeneration) {
        mostEncountersPerGeneration = encountersComplete
    }

    if (encountersComplete > mostEncounters) {
        mostEncounters = encountersComplete
    }

    val result = ActResult(hero, encounters)
    CombatLogger.onMessage("FINAL FITNESS: ${result.fitness}")
    return result
}

private fun getEncounterResult(hero: Hero, encounter: Encounter, genome: Genome): EncounterResult {
    var damageDone = 0

    if (encounter.id % 8 == 0 || encounter.target is Boss) {
        hero.healDamage((80 * 0.30f).toInt())
    }

    CombatLogger.onNextEncounter(encounter, encounter.id)

    val target = encounter.target

    encounter.setPlayer(hero)
    encounter.onEncounterStart()

    var turnCounter = 0
    var previousHealth = hero.getHealth()


    while (!encounter.isComplete) {
        turnCounter = encounter.turnCounter

        if (System.currentTimeMillis() - genomeStartTimer > TIME_LIMIT) {
            CombatLogger.onError("Taking too long to think.")
            break
        }

        if (encounter.turnCounter == TURN_LIMIT) {
            CombatLogger.onError("Hit turn limit ($TURN_LIMIT)!")
            break
        }

        var tempHealth = target.getHealth()


        var attempts = 0

        val hand = ArrayList<Card>()
        hand.addAll(hero.deck.hand)

        CombatLogger.onNextTurn(hand)

        while (hero.getCurrentEnergy() > 0 && attempts < 8) {


            val ids = ArrayList<Float>()
            hand.forEach {
                ids.add(it.hashCode().toFloat())
            }

            for (i in hand.size..5) {
                ids.add(0f)
            }

            // hero
            ids.add(hero.getHealth().toFloat())
            ids.add(hero.getCurrentEnergy().toFloat())

            // target
            ids.add(target.getHealth().toFloat())
            ids.add(target.intent.hashCode().toFloat())


            val output = genome.evaluateNetwork(ids.toFloatArray())
            val sorted = output.sortedDescending()

            var card: Card? = null

            for (i in 0..output.size) {
                try {
                    card = hand[output.indexOf(sorted[i])]
                    break
                } catch (ex: IndexOutOfBoundsException) {
                }
            }

            if (card == null) {
                attempts++
                continue
            }

            if (hero.getCurrentEnergy() < card.energy) {
                attempts++
                CombatLogger.onError("Invalid card choice: ${card.name}")
                card = hand.firstOrNull { it.energy <= hero.getCurrentEnergy() } ?: continue
            }

            encounter.play(card)

            // todo: remove this, shouldn't need it.
            hand.remove(card)
        }



        damageDone += tempHealth - target.getHealth()


        encounter.endTurn()

        if (hero.isDead) {
            CombatLogger.onMessage("Hero has died.")
        }
    }

    encounter.onEncounterEnd()

    return EncounterResult(previousHealth, hero.getHealth(), encounter, turnCounter)
}

private fun addCard(genome: Genome, hero: Hero) {
    val rewards = GameManager.getCardRewards()
    val ids =
        floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f) + rewards.map { -it.hashCode().toFloat() }
            .toFloatArray()

    val result = genome.evaluateNetwork(ids)[0]

    val card = when {
        result < 0.25 -> rewards[0]
        result < 0.50 -> rewards[1]
        result < 0.75 -> rewards[2]
        else -> null
    }

    CombatLogger.addCard(rewards, card)

    if (card != null) {
        hero.deck.addCard(card)
    }

    CombatLogger.addCard(rewards, card)
}