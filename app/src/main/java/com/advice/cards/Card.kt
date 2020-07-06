package com.advice.cards

import com.advice.cards.status.StatusEffect


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
        return amount + self.getStrength()
    }

    override fun getDescription(self: Entity, target: Entity?): String {
        return "Deal ${getScaledValue(self, target)} damage."
    }

    override fun apply(self: Entity, target: Entity) {
        val damage = getScaledValue(self, target)
        target.dealDamage(damage)
    }

    override fun toString() = "Deal $amount damage."
}

class ApplyStatusEffectEffect(private val effect: StatusEffect) : Effect() {

    override fun getScaledValue(self: Entity, target: Entity?) = -1

    override fun getDescription(self: Entity, target: Entity?): String {
        return effect.toString()
    }

    override fun apply(self: Entity, target: Entity) {
        target.applyStatusEffect(effect)
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