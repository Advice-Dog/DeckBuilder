package com.advice.cards

import com.advice.cards.enemies.JawWorm
import com.advice.cards.enemies.group
import com.advice.cards.hero.Ironclad
import com.evo.NEAT.Environment
import com.evo.NEAT.Genome
import com.evo.NEAT.Pool
import java.util.*

var mostEncounters = 0
var mostEncountersPerGeneration = 0
var mostEncountersGenome: Genome? = null

class SlayTheSpire : Environment {

    override fun evaluateFitness(population: ArrayList<Genome>) {
        mostEncountersPerGeneration = 0
        for (genome in population) {
            genome.fitness = getEncounterFitness(genome)
        }
    }
}

fun main() {
    val instance = SlayTheSpire()

    val pool = Pool()
    pool.initializePool()

    var top = Genome()
    var generation = 0

    while (generation < 500) {
        pool.evaluateFitness(instance)
        top = pool.topGenome

        println("Generation $generation")
        println("Top Fitness: " + top.points)
        println("Most Encounters (this generation): $mostEncountersPerGeneration")
        println("Most Encounters: $mostEncounters")


        pool.breedNewGeneration()
        generation++
    }

    var fitness = getEncounterFitness(mostEncountersGenome!!, print = true)
    println("Completed! Top: $fitness")
    fitness = getEncounterFitness(mostEncountersGenome!!, print = false)
    println("Completed! Top: $fitness")
    fitness = getEncounterFitness(mostEncountersGenome!!, print = false)
    println("Completed! Top: $fitness")


}

fun getEncounterFitness(genome: Genome, print: Boolean = false): Float {
    // reset seed to be the same.
    GameManager.resetSeed()

    val hero = Ironclad()
    //hero.deck.setCards(deck)

    val turnLimit = 15

    var damageDone = 0
    var encountersComplete = 0
    var hitTurnLimit = false

    while (hero.isAlive) {
        val encounter = Encounter(group {
            this + JawWorm()
        })

        val target = encounter.target

        if (print) {
            println("$target")
        }

        encounter.setPlayer(hero)
        encounter.onEncounterStart()

        while (!encounter.isComplete) {

            if (encounter.turnCounter == turnLimit) {
                hitTurnLimit = true
                break
            }

            if (print) {
                println("Encounter $encountersComplete -- Turn ${encounter.turnCounter}")
            }

            val hand = hero.deck.hand
            val ids = hand.map {
                it.hashCode().toFloat()
            }.toFloatArray()


            val inputs = ids +
                    // add the target's intent
                    listOf(target.intent.hashCode().toFloat())


            val outputs = genome.evaluateNetwork(inputs)

            if (print) {
                //println(outputs.joinToString { it.toString() })
            }

            var tempHealth = target.getHealth()

            for (output in outputs) {

                var card: Card

                try {
                    card = when {
                        output < 0.25 -> hand[0]
                        output < 0.55 -> hand[1]
                        output < 0.75 -> hand[2]
                        output < 1.00 -> hand[3]
                        else -> hand[0]
                    }
                } catch (ex: IndexOutOfBoundsException) {
                    continue
                }

                if (hero.getCurrentEnergy() >= card.energy) {
                    if (print) {
                        print("  Playing ${card.name}.")
                    }
                    encounter.play(card)

                } else {
                    if (print) {
                        println("  Trying to use an expensive card.")
                    }
                }
            }

            if (print) {
                println()
                println("$hero versus $target")
            }

            damageDone += tempHealth - target.getHealth()

            encounter.endTurn()
        }

        if (hitTurnLimit) {
            break
        }

        encounter.onEncounterEnd()

        if (hero.isAlive) {
            encountersComplete++

            val result = genome.evaluateNetwork(floatArrayOf(-1f, -2f, -3f, -4f))[0]
            val rewards = GameManager.getCardRewards()

            if (print) {
                println("REWARDS: " + rewards.joinToString { it.name })
            }

            val card = when {
                result < 0.25 -> rewards[0]
                result < 0.50 -> rewards[1]
                result < 0.75 -> rewards[2]
                else -> null
            }

            if (card != null) {
                if (print) {
                    println(" >>>> Adding ${card.name} to deck.")
                }

                hero.deck.addCard(card)
            } else {
                if (print) {
                    println(" >>>>> Not adding any card to the deck.")
                }
            }
        }
    }

    if (encountersComplete > mostEncountersPerGeneration) {
        mostEncountersPerGeneration = encountersComplete
    }

    if (encountersComplete > mostEncounters) {
        mostEncounters = encountersComplete
        mostEncountersGenome = genome
    }

    if (print) {
        println("Final Deck:")
        println(hero.deck)
    }

    return (encountersComplete * 5000 + damageDone).toFloat() * (if (hitTurnLimit) 0.25f else 1f)
}