package com.advice.cards

import androidx.annotation.CallSuper
import com.advice.cards.logger.CombatLogger
import com.advice.cards.red.attack.Strike
import com.advice.cards.status.StatusEffect
import kotlin.math.max
import kotlin.math.min

open class Entity(
    private val maxHp: Int
) {

    private var hp: Int = maxHp
    private var armor: Int = 0
    private var strength = 0
    private var agility = 0

    private val statusEffects = ArrayList<StatusEffect>()

    val isAlive: Boolean
        get() = hp > 0

    val isDead: Boolean
        get() = !isAlive

    open val image = R.drawable.cultist

    fun dealDamage(amount: Int) {
        CombatLogger.onMessage("${this.javaClass.simpleName} is hurt for $amount damage.")

        if (armor > 0) {
            val armorDamage = min(amount, armor)
            armor -= armorDamage
            hp -= amount - armorDamage
        } else {
            hp -= amount
        }

        hp = max(hp, 0)

        println(this)
    }

    fun healDamage(amount: Int) {
        CombatLogger.onMessage("${this.javaClass.simpleName} heals for $amount.")
    }

    fun addBlock(amount: Int) {
        CombatLogger.onMessage("${this.javaClass.simpleName} adds $amount Block.")
        armor += amount
    }

    private fun clearBlock() {
        armor = 0
    }

    override fun toString(): String {
        return "${javaClass.simpleName} ($hp/$maxHp) + $armor"
    }

    fun applyStatusEffect(effect: StatusEffect) {
        CombatLogger.onMessage("${this.javaClass.simpleName} has been effected by ${effect.javaClass.simpleName} (${effect.getStacks()}).")
        statusEffects.add(effect)
    }

    fun getBlock() = armor

    fun getStrength() = strength

    fun getAgility() = agility
    fun getHealth() = hp

    @CallSuper
    open fun endTurn() {
        clearBlock()

        statusEffects.forEach {
            it.tick()
        }

        statusEffects.removeAll { it.hasExpired() }
    }

}

open class Hero(val deck: Deck = Deck()) : Entity(50) {

    private var energy: Int
    private val maxEnergy = 3

    init {
        energy = maxEnergy
    }

    fun getCurrentEnergy() = energy
    fun getMaxEnergy() = maxEnergy

    fun useEnergy(amount: Int) {
        energy -= amount
    }

    override fun endTurn() {
        super.endTurn()
        deck.endTurn()
    }

    fun startTurn() {
        energy = maxEnergy
    }
}

abstract class Enemy(maxHp: Int = 15) : Entity(maxHp) {

    abstract var intent: Card

    private val hand = listOf(
        Strike()
    )

    open fun play(target: Entity, self: Entity) {
        hand.first().play(self, target)
    }

}