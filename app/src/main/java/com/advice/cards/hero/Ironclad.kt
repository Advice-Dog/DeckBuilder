package com.advice.cards.hero

import com.advice.cards.Deck
import com.advice.cards.Hero
import com.advice.cards.R
import com.advice.cards.red.attack.Bash
import com.advice.cards.red.attack.Strike
import com.advice.cards.red.skill.Defend

class Ironclad : Hero(
    Deck(
        listOf(
            Strike(),
            Strike(),
            Strike(),
            Strike(),
            Strike(),
            Defend(),
            Defend(),
            Defend(),
            Defend(),
            Bash()
        )
    ), 80
) {
    override val image = R.drawable.ironclad
}