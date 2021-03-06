package com.advice.cards

import androidx.annotation.CallSuper
import com.advice.cards.cards.Card
import com.advice.cards.cards.Deck
import com.advice.cards.cards.red.attack.Strike
import com.advice.cards.cards.status.*
import com.advice.cards.encounters.enemies.Cultist
import com.advice.cards.logger.CombatLogger
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

open class Entity(
    private val maxHp: Int
) : Cloneable {

    private var hp: Int = maxHp
    private var armor: Int = 0
    private var strength = 0
    private var agility = 0

    val statusEffects = ArrayList<StatusEffect>()

    val isAlive: Boolean
        get() = hp > 0

    val isDead: Boolean
        get() = !isAlive

    open val image = R.drawable.cultist

    fun dealDamage(attacker: Entity, amount: Int) {
        CombatLogger.onMessage("${this.javaClass.simpleName} is hurt for $amount damage.")

        if (armor > 0) {
            val armorDamage = min(amount, armor)
            armor -= armorDamage
            hp -= amount - armorDamage
        } else {
            hp -= amount
        }

        hp = max(hp, 0)

        val thornsDamage = statusEffects.filterIsInstance<Thorns>().sumBy { it.amount }
        if (thornsDamage > 0) {
            attacker.dealDamage(this, thornsDamage)
        }
    }

    fun healDamage(amount: Int) {
        CombatLogger.onMessage("${this.javaClass.simpleName} heals for $amount.")
        hp += amount
        hp = min(hp, maxHp)
    }

    fun addBlock(amount: Int) {
        CombatLogger.onMessage("${this.javaClass.simpleName} adds $amount Block.")
        armor += amount
    }

    private fun clearBlock() {
        armor = 0
    }

    override fun toString(): String {
        if (isDead) {
            return "(dead)"
        }
        if (getBlock() > 0) {
            return "[$armor] $hp/$maxHp"
        }
        return "$hp/$maxHp"
    }

    fun applyStatusEffect(effect: StatusEffect) {
        CombatLogger.onMessage("${this.javaClass.simpleName} has been effected by ${effect.javaClass.simpleName} (${effect.getStacks()}).")
        statusEffects.add(effect.clone())
    }

    fun getBlock() = armor

    fun getStrength(): Int {
        // cultist
        val ritual = statusEffects.find { it is Cultist.Ritual }
        if (ritual != null) {
            val i = abs(ritual.getStacks()) - 1
            return i * 3
        }

        return statusEffects.filterIsInstance<FlexBuff>().sumBy { it.strength } +
                statusEffects.filterIsInstance<StrengthUp>().sumBy { it.strength }
    }

    fun getAgility() = agility
    fun getHealth() = hp

    @CallSuper
    open fun endTurn() {
        clearBlock()

    }

    fun tick() {
        statusEffects.forEach {
            it.tick()
        }

        statusEffects.removeAll { it.hasExpired() }
    }

    fun getScaledDamage(entity: Entity, damage: Int, scale: Int = 1): Int {
        val base = damage + (entity.getStrength() * scale)

        val isVulnerable = statusEffects.any { it is Vulnerable }
        if (isVulnerable) {
            return (base * 1.5f).roundToInt()
        }
        return base
    }

    fun getMaxHealth() = maxHp


    public override fun clone(): Entity {
        val entity = super.clone() as Entity
        // todo: figure out why this is cloning over status effects.
        entity.statusEffects.clear()
        return entity
    }
}

open class Hero(val deck: Deck = Deck(), hp: Int) : Entity(hp) {

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

    fun start() {
        deck.startCombat()
        onTurn()
    }

    fun end() {
        statusEffects.clear()
        deck.endCombat()
    }

    fun onTurn() {
        energy = maxEnergy
    }

    fun addEnergy(amount: Int) {
        energy += amount
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

    override fun toString(): String {
        return "${javaClass.simpleName}([${getBlock()}] ${getHealth()} / ${getMaxHealth()})"
    }
}