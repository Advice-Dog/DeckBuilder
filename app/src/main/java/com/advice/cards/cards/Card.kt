package com.advice.cards.cards

import com.advice.cards.Entity
import com.advice.cards.Hero
import com.advice.cards.RNG
import com.advice.cards.cards.status.StatusEffect
import com.advice.cards.encounters.enemies.Boss
import com.advice.cards.logger.CombatLogger


abstract class Card(
    val type: CardType,
    val target: TargetType,
    val rarity: CardRarity = CardRarity.STARTER,
    val colour: CardColour = CardColour.RED,
    val energy: Int = 1,
    val count: Int = 1,
    var level: Int = 1,
    val maxLevel: Int = 2
) {
    val effects = ArrayList<Effect>()

    val isUpgraded: Boolean
        get() = level > 1

    fun upgrade() {
        if (level < maxLevel) {
            level++
        }
    }

    val name: String
        get() = javaClass.simpleName + (if (isUpgraded) "+" else "")

    open val description: String
        get() = effects.joinToString { it.toString() }

    open fun play(self: Entity, entity: Entity) {
        CombatLogger.onCardPlayed(this, self, entity)
        effects.forEach { it.apply(self, entity) }
    }

    override fun toString(): String {
        return "$name: $description"
    }

    open fun canPlay(hero: Hero): Boolean {
        return energy <= hero.getCurrentEnergy()
    }

    open fun getDescription(self: Entity, target: Entity?): String {
        if (effects.isNotEmpty()) {
            if (this.target == TargetType.ALL_ENEMY && effects.size == 2) {
                val first = effects.first().getDescription(self, target).replace(".", " and ")
                val last = effects.last().getDescription(self, target).toLowerCase()
                    .replace(".", " to ALL enemies.")
                return first + last
            }

            return effects.joinToString(separator = "\n") { it.getDescription(self, target) }
        }
        return description
    }
}

enum class TargetType {
    SELF,
    ENEMY,
    ALL_ENEMY,
    RANDOM_ENEMY
}


enum class CardType {
    ATTACK,
    SKILL,
    POWER,
    STATUS,
    CURSE
}

enum class CardRarity {
    STARTER,
    COMMON,
    UNCOMMON,
    RARE
}

enum class CardColour {
    RED,
    GREEN,
    BLUE,
    COLOURLESS,
    CURSE,
    STATUS
}

abstract class Effect {

    abstract fun getScaledValue(self: Entity, target: Entity?): Int

    abstract fun getDescription(self: Entity, target: Entity?): String

    open fun apply(self: Entity, target: Entity) {
        // do nothing
    }

    open fun remove(self: Entity, target: Entity) {
        // do nothing
    }

}

class DamageEffect(private val amount: Int) : Effect() {

    override fun getScaledValue(self: Entity, target: Entity?): Int {
        if (target != null) {
            return target.getScaledDamage(self, amount)
        }

        if (self is Boss || target is Boss) {
            println("target damage = $amount")
        }

        return amount + self.getStrength()
    }

    override fun getDescription(self: Entity, target: Entity?): String {
        return "Deal ${getScaledValue(self, target)} damage."
    }

    override fun apply(self: Entity, target: Entity) {
        val damage = getScaledValue(self, target)
        target.dealDamage(self, damage)
    }

    override fun toString() = "Deal $amount damage."
}

class DamageRampEffect(private val base: Int, private val ramp: Int) : Effect() {

    var count = 0

    override fun getScaledValue(self: Entity, target: Entity?): Int {
        val amount = base + (ramp * count)
        if (target != null) {
            return target.getScaledDamage(self, amount)
        }

        return amount + self.getStrength()
    }

    override fun getDescription(self: Entity, target: Entity?): String {
        return "Deal ${getScaledValue(self, target)} damage. Increase this card's damage by $ramp this combat."
    }

    override fun apply(self: Entity, target: Entity) {
        val damage = getScaledValue(self, target)
        target.dealDamage(self, damage)
        count++
    }

    override fun toString() = "Deal ${base + ramp * count} damage. Increase this card's damage by $ramp this combat."
}

class HealEffect(private val amount: Int) : Effect() {

    override fun getScaledValue(self: Entity, target: Entity?) = amount

    override fun getDescription(self: Entity, target: Entity?): String {
        return "Heal ${getScaledValue(self, target)} damage."
    }

    override fun apply(self: Entity, target: Entity) {
        val damage = getScaledValue(self, target)
        self.healDamage(damage)
    }

    override fun toString() = "Heal $amount damage."

}

class ScaledDamageEffect(private val amount: Int, private val scale: Int = 1) : Effect() {
    override fun getScaledValue(self: Entity, target: Entity?): Int {
        if (target != null) {
            return target.getScaledDamage(self, amount, scale)
        }
        return amount + (self.getStrength() * scale)
    }

    override fun getDescription(self: Entity, target: Entity?): String {
        return "Deal ${getScaledValue(
            self,
            target
        )} damage. Strength affects this card $scale times."
    }

    override fun apply(self: Entity, target: Entity) {
        val damage = getScaledValue(self, target)
        target.dealDamage(self, damage)
    }

    override fun toString() = "Deal $amount damage. Strength affects this card $scale times."
}

class PerfectedStrikeDamageEffect(private val amount: Int, private val bonus: Int = 2) : Effect() {
    override fun getScaledValue(self: Entity, target: Entity?): Int {
        val count =
            (self as Hero).deck.deck.count { it.name.contains("strike", ignoreCase = true) }

        val bonus = count * bonus

        if (target != null) {
            return target.getScaledDamage(self, amount + bonus)
        }
        return amount + self.getStrength() + bonus
    }

    override fun getDescription(self: Entity, target: Entity?): String {
        return "Deal ${getScaledValue(
            self,
            target
        )} damage.\nDeals $bonus additional damage for ALL your cards containing \"Strike\"."
    }

    override fun apply(self: Entity, target: Entity) {
        val damage = getScaledValue(self, target)
        target.dealDamage(self, damage)
    }

    override fun toString() =
        "Deal $amount damage.\nDeals $bonus additional damage for ALL your cards containing \"Strike\"."
}

class BlockEffect(private val amount: Int) : Effect() {
    override fun getScaledValue(self: Entity, target: Entity?): Int {
        return amount + self.getAgility()
    }

    override fun getDescription(self: Entity, target: Entity?): String {
        return "Gain ${getScaledValue(self, target)} Block."
    }

    override fun apply(self: Entity, target: Entity) {
        self.addBlock(getScaledValue(self, target))
    }

    override fun toString(): String {
        return "Gain $amount Block."
    }
}

class DoubleBlockEffect() : Effect() {
    override fun getScaledValue(self: Entity, target: Entity?) = self.getBlock()

    override fun getDescription(self: Entity, target: Entity?): String {
        return "Double your Block."
    }

    override fun apply(self: Entity, target: Entity) {
        self.addBlock(getScaledValue(self, target))
    }

    override fun toString(): String {
        return "Double your Block."
    }
}

class BlockDamageEffect : Effect() {
    override fun getScaledValue(self: Entity, target: Entity?): Int {
        return target?.getScaledDamage(self, self.getBlock()) ?: self.getBlock()
    }

    override fun getDescription(self: Entity, target: Entity?): String {
        return "Deal damage equal to your Block."
    }

    override fun apply(self: Entity, target: Entity) {
        val amount = getScaledValue(self, target)
        target.dealDamage(self, amount)
    }

    override fun toString(): String {
        return "Deal damage equal to your Block."
    }
}

class ApplyStatusEffectEffect(
    private val effect: StatusEffect,
    private val targetType: TargetType = TargetType.ENEMY
) : Effect() {

    override fun getScaledValue(self: Entity, target: Entity?) = -1

    override fun getDescription(self: Entity, target: Entity?): String {
        return effect.toString()
    }

    override fun apply(self: Entity, target: Entity) {
        if (targetType == TargetType.ENEMY) {
            target.applyStatusEffect(effect)
        } else {
            self.applyStatusEffect(effect)
        }
    }

    override fun toString(): String {
        return effect.toString()
    }
}

class AddEnergyEffect(private val amount: Int) : Effect() {

    override fun getScaledValue(self: Entity, target: Entity?) = -1

    override fun getDescription(self: Entity, target: Entity?): String {
        return "Gain $amount energy."
    }

    override fun apply(self: Entity, target: Entity) {
        (self as Hero).addEnergy(amount)
    }
}

class UpdateHandCardEffect(private val update: Upgrade) : Effect() {

    enum class Upgrade {
        ONE,
        ALL
    }

    override fun getScaledValue(self: Entity, target: Entity?) = -1

    override fun getDescription(self: Entity, target: Entity?): String {
        return "Upgrade a card in your hand for the rest of combat."
    }

    override fun apply(self: Entity, target: Entity) {
        val hand = (self as Hero).deck.hand
        if (update == Upgrade.ONE) {
            RNG.random(hand).upgrade()
        } else {
            hand.forEach { it.upgrade() }
        }
    }
}

class DrawCardEffect(private val count: Int) : Effect() {
    override fun getScaledValue(self: Entity, target: Entity?) = -1

    override fun getDescription(self: Entity, target: Entity?): String {
        if (count == 1) {
            return "Draw $count card."
        }
        return "Draw $count cards."
    }

    override fun apply(self: Entity, target: Entity) {
        (self as Hero).deck.drawCard(count)
    }
}

class ExhaustCardEffect(private val cardTarget: Target) : Effect() {

    enum class Target {
        RANDOM,
        NOT_RANDOM
    }

    override fun getScaledValue(self: Entity, target: Entity?) = -1

    override fun getDescription(self: Entity, target: Entity?): String {
        return "Exhaust a random card in your hand."
    }

    override fun apply(self: Entity, target: Entity) {
        val deck = (self as Hero).deck
        val hand = deck.hand
        if (cardTarget == Target.RANDOM) {
            deck.exhaustCard(RNG.random(hand))
        } else {
            TODO()
        }
    }

}

class CopyToDiscardEffect(private val card: Card) : Effect() {
    override fun getScaledValue(self: Entity, target: Entity?) = -1

    override fun getDescription(self: Entity, target: Entity?): String {
        return "Add a copy of this card into your discard pile."
    }

    override fun apply(self: Entity, target: Entity) {
        (self as Hero).deck.discardCard(card)
    }
}

class ExhaustEffect(private val card: Card) : Effect() {
    override fun getScaledValue(self: Entity, target: Entity?) = -1

    override fun getDescription(self: Entity, target: Entity?): String {
        return "Exhaust."
    }

    override fun apply(self: Entity, target: Entity) {
        (self as Hero).deck.exhaustCard(card)
    }
}