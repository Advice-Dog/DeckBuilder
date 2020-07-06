package com.advice.cards.status

class FlexBuff(val strength: Int) : StatusEffect(1) {

    override fun toString(): String {
        return "Gain $strength Strength.\nAt the end of this turn, lose $strength Strength."
    }

}