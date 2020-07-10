package com.evo.NEAT

/**
 * assign Fitness to each genome
 * Created by vishnu on 12/1/17.
 *
 */
interface SuspendEnvironment {
    suspend fun evaluateFitness(population: List<Genome>)
}