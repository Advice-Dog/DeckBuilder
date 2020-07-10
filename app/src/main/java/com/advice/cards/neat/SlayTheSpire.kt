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
import com.evo.NEAT.Pool
import com.evo.NEAT.SuspendEnvironment
import kotlinx.coroutines.runBlocking
import kotlin.math.max
import kotlin.random.Random

private const val GENERATIONS = 20
private const val TURN_LIMIT = 15
private const val TIME_LIMIT = 5_000L

var mostEncounters = 0
var mostEncountersPerGeneration = 0
var genomeStartTimer = 0L


class SlayTheSpire : SuspendEnvironment {

    companion object {
        private const val BATCH_SIZE = 50

        var index = 0
    }

    override suspend fun evaluateFitness(population: List<Genome>) {

        mostEncountersPerGeneration = 0
        index++

        for (genome in population) {
            GameManager.seed = Random(index)
            genome.fitness = getEncounterFitness(genome)
//            val result = getEncounterResult(genome)
//            if (result.hero.isAlive) {
//                println("fitness: ${result.fitness}")
//
//                genome.fitness = 99999f
//                //CombatLogger.print()
//                break
//            }


//            genome.fitness = result.fitness.toFloat()
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

        runBlocking {
            pool.evaluateFitness(instance)
        }

        val block = "==========================================================" +
                "\nGeneration $generation" +
                "\nTop Fitness: ${pool.topGenome.points}" +
                "\nMost Encounters (this generation): $mostEncountersPerGeneration" +
                "\nMost Encounters: $mostEncounters" +
                "\n=========================================================="

        println(block)

        pool.breedNewGeneration()
        generation++

    }


    // Reset to previous generation
    GameManager.seed = Random(SlayTheSpire.index)
    CombatLogger.isEnabled = true

    val fitness = getEncounterFitness(top)
    println("Completed! Top: $fitness")

    // print the last combat
    CombatLogger.print()
}

fun getEncounterFitness(genome: Genome): Float {
    val hero = Ironclad()

    var fitness = 0.0f

    val turnLimit = 15

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

            if (System.currentTimeMillis() - genomeStartTimer > 3000L) {
                println("Taking too long! Encounters: $encountersComplete")
                hitTurnLimit = true
                break
            }

            if (encounter.turnCounter == turnLimit) {
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
                turnLimit - turnCounter
            fitness += //16.0.pow(hero.getHealth() - damageTaken).toFloat()
                max(0, 100 - (damageTaken * 15))

            encountersComplete++

            addCard(genome, hero)

            //hero.healDamage(6)
        }
    }

    // Bonus for completing it all
    if (hero.isAlive) {
        fitness += 50000
    }

    if (encountersComplete > mostEncountersPerGeneration) {
        mostEncountersPerGeneration = encountersComplete
    }

    if (encountersComplete > mostEncounters) {
        mostEncounters = encountersComplete
    }

    return fitness
//    if (hero.isAlive) {
//        //CombatLogger.print()
//    }

    //return result
}

private fun addCard(
    genome: Genome,
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

    CombatLogger.addCard(rewards, card)

    if (card != null) {
        hero.deck.addCard(card)
    }

    CombatLogger.addCard(rewards, card)
}

data class EncounterResult(
    val hero: Hero,
    val turnsTaken: Int,
    val damageTaken: Int
) {
    val fitness: Int
        get() {
            val damageMod = (TURN_LIMIT - turnsTaken) * 10
            val healthMod = max(10, 100 - damageTaken * 10)
            return damageMod + healthMod
        }

    override fun toString() = super.toString() + " $fitness"
}

data class ActResult(
    val hero: Hero,
    val encounters: ArrayList<EncounterResult> = ArrayList()
) {
    val fitness: Int
        get() = encounters.sumBy { it.fitness } + if (hero.isAlive) 5000 else 0
}