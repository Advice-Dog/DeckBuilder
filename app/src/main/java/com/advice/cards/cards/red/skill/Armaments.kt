package com.advice.cards.cards.red.skill

import com.advice.cards.cards.*

class Armaments : Card(CardType.SKILL, TargetType.SELF) {

    private val block = 5
    private val update = UpdateHandCardEffect.Upgrade.ONE

    init {
        effects.add(BlockEffect(block))
        effects.add(UpdateHandCardEffect(update))
    }
}