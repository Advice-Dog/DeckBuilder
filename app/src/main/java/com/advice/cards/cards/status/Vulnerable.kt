package com.advice.cards.cards.status

class Vulnerable(private val initial: Int) : StatusEffect(initial) {

    override fun toString(): String {
        return "Apply $initial Vulnerable."
    }

}