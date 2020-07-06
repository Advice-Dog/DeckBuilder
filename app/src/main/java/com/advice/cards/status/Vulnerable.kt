package com.advice.cards.status

class Vulnerable(stacks: Int) : StatusEffect(stacks) {

    override fun toString(): String {
        return "Apply ${getStacks()} Vulnerable."
    }

}