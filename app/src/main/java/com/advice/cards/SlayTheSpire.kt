package com.advice.cards

import com.advice.cards.hero.Ironclad
import com.evo.NEAT.Environment
import com.evo.NEAT.Genome
import com.evo.NEAT.Pool
import kotlin.math.max

private const val GENERATIONS = 300

var mostEncounters = 0
var mostEncountersPerGeneration = 0
var mostEncountersGenome: Genome? = null
var genomeStartTimer = 0L


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

    while (generation < GENERATIONS) {
        pool.evaluateFitness(instance)
        top = pool.topGenome

        println("Generation $generation")
        println("Top Fitness: " + top.points)
        println("Most Encounters (this generation): $mostEncountersPerGeneration")
        println("Most Encounters: $mostEncounters")


        pool.breedNewGeneration()
        generation++
    }

    val fitness = getEncounterFitness(top, print = true)
    println("Completed! Top: $fitness")
}

fun getEncounterFitness(genome: Genome, print: Boolean = false): Float {
    // reset seed to be the same.
    GameManager.resetSeed()

    val hero = Ironclad()


    var fitness = 0.0f

    val turnLimit = 15

    var damageDone = 0
    var encountersComplete = 0
    var hitTurnLimit = false

    genomeStartTimer = System.currentTimeMillis()


    val act = GameManager.act
    act.reset()


    while (hero.isAlive && encountersComplete < act.encounters.size) {
        val encounter = Encounter(act.encounters[encountersComplete])


        val target = encounter.target

        encounter.setPlayer(hero)
        encounter.onEncounterStart()

        var turnCounter = 0
        var previousHealth = hero.getHealth()

        while (!encounter.isComplete) {
            turnCounter = encounter.turnCounter

            if (System.currentTimeMillis() - genomeStartTimer > 1000L) {
                println("Taking too long! Encounters: $encountersComplete")
                hitTurnLimit = true
                break
            }

            if (encounter.turnCounter == turnLimit) {
                hitTurnLimit = true
                break
            }

            if (print) {
                println("Encounter ${encountersComplete + 1} -- Turn ${encounter.turnCounter}  [$hero] vs [$target]")
            }

            var tempHealth = target.getHealth()


            var attempts = 0

            if (print) {
                println("Hand: ${hero.deck.hand.joinToString { it.name }}")
            }

            while (hero.getCurrentEnergy() > 0 && attempts < 8) {
                attempts++

                val hand = hero.deck.hand

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


                val output = genome.evaluateNetwork(ids.toFloatArray())[0]

                var card: Card

                try {
                    card = when {
                        output < 0.20 -> hand[0]
                        output < 0.40 -> hand[1]
                        output < 0.60 -> hand[2]
                        output < 0.80 -> hand[3]
                        output < 1.00 -> hand[4]
                        else -> hand[0]
                    }
                } catch (ex: IndexOutOfBoundsException) {
                    continue
                }



                if (hero.getCurrentEnergy() < card.energy) {
                    if (print) {
                        println("Invalid card [${card.name}]")
                    }
                    card = hand.firstOrNull { it.energy <= hero.getCurrentEnergy() } ?: continue
                }
                if (print) {
                    println("Play [${card.name}]")
                }

                encounter.play(card)
            }



            damageDone += tempHealth - target.getHealth()

            encounter.endTurn()
        }

        if (hitTurnLimit) {
            break
        }

        encounter.onEncounterEnd()

        if (hero.isAlive) {
            val damageTaken = previousHealth - hero.getHealth()

            fitness += //2.0.pow((turnLimit - turnCounter).toDouble()).toFloat()
                turnLimit - turnCounter
            fitness += //16.0.pow(hero.getHealth() - damageTaken).toFloat()
                max(0, 100 - (damageTaken * 5))

            encountersComplete++
            //addCard(genome, print, hero)
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

    return fitness
}

private fun addCard(
    genome: Genome,
    print: Boolean,
    hero: Ironclad
) {
    val rewards = GameManager.getCardRewards()
    val ids =
        floatArrayOf(0f) + rewards.map { -it.hashCode().toFloat() }.toFloatArray()

    val result = genome.evaluateNetwork(ids)[0]

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