package com.advice.cards


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
    val isUpgraded: Boolean
        get() = level > 1

    fun upgrade() {
        if (level < maxLevel) {
            level++
        }
    }

    val name: String
        get() = javaClass.simpleName + (if (isUpgraded) "+" else "")

    abstract val description: String

    abstract fun play(self: Entity, entity: Entity)

    override fun toString(): String {
        return "$name: $description"
    }

    open fun canPlay(hero: Hero): Boolean {
        return energy <= hero.getCurrentEnergy()
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