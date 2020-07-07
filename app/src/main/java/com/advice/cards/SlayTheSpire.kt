package com.advice.cards

import com.advice.cards.enemies.Cultist
import com.advice.cards.enemies.group
import com.advice.cards.hero.Ironclad
import com.advice.cards.red.attack.Bash
import com.advice.cards.red.attack.Strike
import com.advice.cards.red.skill.Defend
import com.evo.NEAT.Environment
import com.evo.NEAT.Genome
import com.evo.NEAT.Pool


class SlayTheSpire : Environment {

    override fun evaluateFitness(population: ArrayList<Genome>) {
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

    while (generation < 100) {
        pool.evaluateFitness(instance)
        top = pool.topGenome

        println("Generation $generation -- Top Fitness: " + top.points)
        pool.breedNewGeneration()
        generation++
    }

    println("Completed! Top: ${top.fitness}")
    val fitness = getEncounterFitness(top, print = true)

}

fun getEncounterFitness(genome: Genome, print: Boolean = false): Float {
    val encounter = Encounter(group {
        this + Cultist()
    })

    val hero = Ironclad()
    val target = encounter.target

    if (print) {
        println("$target")
    }

    encounter.setPlayer(hero)
    encounter.onEncounterStart()

    while (!encounter.isComplete) {

        val hand = hero.deck.hand
//        val ids = hand.map {
//            it.hashCode().toFloat()
//        }
        val ids = listOf(
            0.0f, // defend
            0.5f, // strike
            1.0f // bash
        )
        val names = hand.map {
            it.name
        }


        val inputs = floatArrayOf(
            hero.getHealth().toFloat(),
            target.getHealth().toFloat()
        )


        val outputs = genome.evaluateNetwork(ids.toFloatArray())


        val output = outputs[0]
        val card = when {
            output < 0.33 -> Defend()
            output < 0.66 -> Strike()
            else -> Bash()
        }

        if (print) {
            println("Playing $card -- $hero versus $target")
        }
        encounter.play(card)
        encounter.endTurn()
    }

    return hero.getHealth().toFloat() * 2 + (target.getMaxHealth() - target.getHealth())
}