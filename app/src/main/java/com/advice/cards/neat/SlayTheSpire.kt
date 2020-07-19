package com.advice.cards.neat

import com.advice.cards.RNG
import com.advice.cards.logger.CombatLogger
import com.evo.NEAT.Environment
import com.evo.NEAT.Genome
import com.evo.NEAT.Pool
import com.evo.NEAT.config.NEATConfig
import java.util.*

const val GENERATIONS = 50

const val TURN_LIMIT = 25
const val TIME_LIMIT = 5_000L

var mostEncounters = 0
var mostEncountersPerGeneration = 0
var genomeStartTimer = 0L


class SlayTheSpire : Environment {

    companion object {

        var index = 0
    }

    override fun evaluateFitness(population: List<Genome>) {
        mostEncountersPerGeneration = 0
        //index++

        val results = TreeSet<String>()

        for (genome in population) {
            //GameManager.seed = Random(index)
            RNG.reset()
            val result = getGameResult(genome)

            //val message = "Result: ${result.completedEncounters} -- ${result.fitness}"
            //results.add(message)
            genome.fitness = result.fitness.toFloat()
        }

        //results.forEach { println(it) }
    }
}

fun main() {
    val instance = SlayTheSpire()

    val config = NEATConfig.Builder()
        .setPopulationSize(1500)
        .setBatchSize(1500)
        .setInputs(9)
        .setOutputs(5)
        .build()

    val pool = Pool(config)
    pool.initializePool()

    var generation = 0

    var top: Genome = pool.topGenome

    while (generation < GENERATIONS) {
        pool.evaluateFitness(instance)
        top = pool.topGenome

        RNG.reset()
        val best = getGameResult(top)
        RNG.reset()
        val best2 = getGameResult(top)

        if (best.fitness != best2.fitness) {
            println(best.toString())
            println(best2.toString())
            //throw IllegalStateException("Whoopies")
        }

        var block = "==========================================================" +
                "\nGeneration $generation" +
                "\nTop Fitness: ${top.points}" +
                "\nTop Deck: ${best.hero.deck}"

        val encounter = best.encounters.last().encounter
        if (encounter.isBossFight) {
            block += "\n Boss Health: " + encounter.target.toString()
        }


        block +=
            "\nCompleted Encounters: ${best.completedEncounters}" +
                    "\nMost Encounters: $mostEncounters" +
                    "\n=========================================================="

        println(block)


        pool.breedNewGeneration()
        generation++

    }


    // Reset to previous generation
    RNG.reset()
    CombatLogger.isEnabled = true

    getGameResult(pool.topGenome)

    // print the last combat
    CombatLogger.print()
}