package com.advice.cards.neat

import com.advice.cards.GameManager
import com.advice.cards.cards.Card
import com.advice.cards.encounters.Encounter
import com.advice.cards.encounters.act
import com.advice.cards.encounters.enemies.Boss
import com.advice.cards.hero.Ironclad
import com.advice.cards.logger.CombatLogger
import com.evo.NEAT.Genome
import kotlin.math.max

fun getEncounterFitness(genome: Genome): Float {
    val hero = Ironclad()

    var fitness = 0.0f

    var damageDone = 0
    var encountersComplete = 0
    var hitTurnLimit = false

    genomeStartTimer = System.currentTimeMillis()


    val act = act.clone()

    while (hero.isAlive && encountersComplete < act.encounters.size) {


        val encounter =
            Encounter(act.encounters[encountersComplete])

        if (encountersComplete == 6 || encountersComplete == 9 || encounter.target is Boss) {
            hero.healDamage((80 * 0.30f).toInt())
        }

        CombatLogger.onNextEncounter(encounter, encountersComplete)

        val target = encounter.target

        encounter.setPlayer(hero)
        encounter.onEncounterStart()

        var turnCounter = 0
        var previousHealth = hero.getHealth()

        while (!encounter.isComplete) {
            turnCounter = encounter.turnCounter

            if (System.currentTimeMillis() - genomeStartTimer > TIME_LIMIT) {
                CombatLogger.onError("Taking too long to think. Completed $encountersComplete")
                hitTurnLimit = true
                break
            }

            if (encounter.turnCounter == TURN_LIMIT) {
                CombatLogger.onError("Hit turn limit ($TURN_LIMIT)!")
                hitTurnLimit = true
                break
            }

            var tempHealth = target.getHealth()


            var attempts = 0

            val hand = ArrayList<Card>()
            hand.addAll(hero.deck.hand)

            CombatLogger.onNextTurn(hand)

            while (hero.getCurrentEnergy() > 0 && attempts < 8) {
                attempts++


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

                if (card == null)
                    continue

                if (hero.getCurrentEnergy() < card.energy) {
                    CombatLogger.onError("Invalid card choice: ${card.name}")
                    card = hand.firstOrNull { it.energy <= hero.getCurrentEnergy() } ?: continue
                }
                CombatLogger.onCardPlayed(card, hero, target)

                encounter.play(card)

                // todo: remove this, shouldn't need it.
                hand.remove(card)
            }



            damageDone += tempHealth - target.getHealth()

            if (target is Boss) {
                val bossDamageBonus = 10 * (target.getMaxHealth() - target.getHealth())
                fitness += bossDamageBonus
            }

            encounter.endTurn()

            if (hero.isDead) {
                CombatLogger.onMessage("Hero has died.")
            }
        }

        if (hitTurnLimit) {
            break
        }

        encounter.onEncounterEnd()

        if (hero.isAlive) {
            val damageTaken = previousHealth - hero.getHealth()

            fitness += //2.0.pow((turnLimit - turnCounter).toDouble()).toFloat()
                TURN_LIMIT - turnCounter
            fitness += //16.0.pow(hero.getHealth() - damageTaken).toFloat()
                max(0, 100 - (damageTaken * 15))

            encountersComplete++

            addCard(genome, hero)


            // todo: remove, this is hardcoded artifact
            hero.healDamage(6)
        }
    }

    if (CombatLogger.isEnabled) {
        println("top!")
    }

    // Bonus for completing it all
    if (!hitTurnLimit && hero.isAlive) {
        //println(" >>>>>>>>>>>> HERO HAS KILLED THE FINAL BOSS. ${hero.getHealth()}")
        fitness += 100_000
        fitness += hero.getHealth() * 10_000

        //println("final fitness: $fitness")
    }

    if (encountersComplete > mostEncountersPerGeneration) {
        mostEncountersPerGeneration = encountersComplete
    }

    if (encountersComplete > mostEncounters) {
        mostEncounters = encountersComplete
    }

    CombatLogger.onMessage("FINAL FITNESS: $fitness")

    return fitness
//    if (hero.isAlive) {
//        //CombatLogger.print()
//    }

    //return result
}

private fun addCard(genome: Genome, hero: Ironclad) {
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