package com.advice.cards.neat

import com.advice.cards.RNG
import com.advice.cards.logger.CombatLogger
import com.evo.NEAT.Genome
import com.evo.NEAT.Pool
import com.evo.NEAT.SuspendEnvironment
import com.evo.NEAT.config.NEAT_Config
import java.util.*

const val TURN_LIMIT = 25
const val TIME_LIMIT = 5_000L

var mostEncounters = 0
var mostEncountersPerGeneration = 0
var genomeStartTimer = 0L


class SlayTheSpire : SuspendEnvironment {

    companion object {

        var index = 0
    }

    override suspend fun evaluateFitness(population: List<Genome>) {
        mostEncountersPerGeneration = 0
        //index++

        val results = TreeSet<String>()

        for (genome in population) {
            //GameManager.seed = Random(index)
            RNG.reset()
            val result = getGameResult(genome)

            val message = "Result: ${result.completedEncounters} -- ${result.fitness}"
            results.add(message)
            genome.fitness = result.fitness.toFloat()
        }

        //results.forEach { println(it) }
    }
}

fun main() {
    val instance = SlayTheSpire()

    val pool = Pool()
    pool.initializePool()

    var generation = 0

    var top: Genome = pool.topGenome

    while (generation < NEAT_Config.GENERATIONS) {
        pool.evaluateFitness(instance)
        top = pool.topGenome

        CombatLogger.reset()

        RNG.reset()

        val best = getGameResult(top)

        RNG.reset()

        val best2 = getGameResult(top)

        CombatLogger.print()

        if (best.fitness != best2.fitness) {
            println(best.toString())
            println(best2.toString())
            throw IllegalStateException("Whoopies")
        }


        val block = "==========================================================" +
                "\nGeneration $generation" +
                "\nTop Fitness: ${top.points} -- ${best.fitness} -- ${best2.fitness}" +
                "\nTop Deck: ${best.hero.deck}" +
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