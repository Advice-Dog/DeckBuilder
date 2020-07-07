package com.advice.cards.status

class Weak(private val initial: Int) : StatusEffect(initial) {

    override fun toString(): String {
        return "Apply $initial Weak."
    }
}