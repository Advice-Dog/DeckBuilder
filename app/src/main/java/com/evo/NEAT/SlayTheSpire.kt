package com.evo.NEAT

import com.advice.cards.GameManager
import com.advice.cards.cards.Card
import com.advice.cards.encounters.Encounter
import com.advice.cards.encounters.enemies.Boss
import com.advice.cards.hero.Ironclad
import kotlin.math.max
import kotlin.random.Random

private const val GENERATIONS = 100

var mostEncounters = 0
var mostEncountersPerGeneration = 0
var genomeStartTimer = 0L


class SlayTheSpire : Environment {

    companion object {
        var index = 0
    }

    override fun evaluateFitness(population: ArrayList<Genome>) {

        mostEncountersPerGeneration = 0
        index++

        for (genome in population) {
            GameManager.seed = Random(index)
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

        val block = "==========================================================" +
                "\nGeneration $generation" +
                "\nTop Fitness: ${top.points}" +
                "\nMost Encounters (this generation): $mostEncountersPerGeneration" +
                "\nMost Encounters: $mostEncounters" +
                "\n=========================================================="

        println(block)

        pool.breedNewGeneration()
        generation++
    }


    // Reset to previous generation
    GameManager.seed = Random(SlayTheSpire.index)
    val fitness = getEncounterFitness(top, print = true)
    println("Completed! Top: $fitness")
}

fun getEncounterFitness(genome: Genome, print: Boolean = false): Float {
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
        val encounter =
            Encounter(act.encounters[encountersComplete])


        val target = encounter.target

        encounter.setPlayer(hero)
        encounter.onEncounterStart()

        var turnCounter = 0
        var previousHealth = hero.getHealth()

        while (!encounter.isComplete) {
            turnCounter = encounter.turnCounter

            if (System.currentTimeMillis() - genomeStartTimer > 3000L) {
                println("Taking too long! Encounters: $encountersComplete")
                hitTurnLimit = true
                break
            }

            if (encounter.turnCounter == turnLimit) {
                hitTurnLimit = true
                break
            }

            if (print) {
                println("Encounter ${encountersComplete + 1} -- Turn ${encounter.turnCounter}  [$hero] vs [${encounter.enemies.joinToString { it.toString() }}]")
            }

            var tempHealth = target.getHealth()


            var attempts = 0

            if (print) {
                println("Hand: ${hero.deck.hand.joinToString { it.name }}")
            }


            val hand = ArrayList<Card>()
            hand.addAll(hero.deck.hand)



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
                    if (print) {
                        println("Invalid card [${card.name}]")
                    }
                    card = hand.firstOrNull { it.energy <= hero.getCurrentEnergy() } ?: continue
                }
                if (print) {
                    println(
                        "Play [${card.name}: ${card.getDescription(hero, target)
                            .replace("\n", " ")}]"
                    )
                }

                encounter.play(card)

                // todo: remove this, shouldn't need it.
                hand.remove(card)
            }



            damageDone += tempHealth - target.getHealth()

            if(target is Boss) {
                fitness += 10 * (target.getMaxHealth() - target.getHealth())
            }

            encounter.endTurn()

            if (print && hero.isDead) {
                println("Hero has died!")
            }
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
                max(0, 100 - (damageTaken * 15))

            encountersComplete++

            addCard(genome, print, hero)

            hero.healDamage(6)
        }
    }

    // Bonus for completing it all
    if (hero.isAlive) {
        fitness += 5000
    }

    if (encountersComplete > mostEncountersPerGeneration) {
        mostEncountersPerGeneration = encountersComplete
    }

    if (encountersComplete > mostEncounters) {
        mostEncounters = encountersComplete
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
        floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f) + rewards.map { -it.hashCode().toFloat() }
            .toFloatArray()

    val result = genome.evaluateNetwork(ids)[0]

    val card = when {
        result < 0.25 -> rewards[0]
        result < 0.50 -> rewards[1]
        result < 0.75 -> rewards[2]
        else -> null
    }

    if (print) {
        val block = "==========================================================" +
                "\n Rewards: " + rewards.joinToString { it.name } +
                "\n Choosing: " + card?.name +
                "\n=========================================================="
        println(block)
    }

    if (card != null) {
        hero.deck.addCard(card)
    }
}