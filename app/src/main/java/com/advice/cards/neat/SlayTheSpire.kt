package com.advice.cards.neat

import com.advice.cards.GameManager
import com.advice.cards.logger.CombatLogger
import com.evo.NEAT.Genome
import com.evo.NEAT.Pool
import com.evo.NEAT.SuspendEnvironment
import com.evo.NEAT.config.NEAT_Config
import kotlin.random.Random

const val TURN_LIMIT = 15
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

    var generation = 0

    var top: Genome = pool.topGenome

    while (generation < NEAT_Config.GENERATIONS) {
        pool.evaluateFitness(instance)

        val block = "==========================================================" +
                "\nGeneration $generation" +
                "\nTop Fitness: ${pool.topGenome.points}" +
                "\nMost Encounters (this generation): $mostEncountersPerGeneration" +
                "\nMost Encounters: $mostEncounters" +
                "\n=========================================================="

        println(block)

        top = pool.topGenome

        pool.breedNewGeneration()
        generation++

    }


    // Reset to previous generation
    GameManager.seed = Random(SlayTheSpire.index)
    CombatLogger.isEnabled = true

    getEncounterFitness(pool.topGenome)

    // print the last combat
    CombatLogger.print()
}