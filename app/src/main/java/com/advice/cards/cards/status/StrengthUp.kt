package com.advice.cards.cards.status

class StrengthUp(val strength: Int, stacks: Int) : StatusEffect(stacks) {

    override fun toString(): String {
        return "Gain $strength Strength."
    }

}