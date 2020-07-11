package com.advice.cards.cards

import com.advice.cards.RNG
import com.advice.cards.cards.colourless.Bandaid
import com.advice.cards.logger.CombatLogger

class Deck(cards: List<Card> = emptyList()) {

    companion object {
        private const val DRAW_AMOUNT = 5
    }

    val deck = ArrayList<Card>()

    val size: Int
        get() = deck.size

    val hand = ArrayList<Card>()
    val draw = ArrayList<Card>()
    val discard = ArrayList<Card>()
    val exhaust = ArrayList<Card>()

    init {
        deck.addAll(cards)
    }

    fun addCard(card: Card) {
        deck.add(card)
    }

    fun removeCard(card: Card) {
        val hasRemoved = deck.remove(card)
        if (!hasRemoved) {
            throw IllegalStateException("Could not remove ${card.name} from deck.")
        }
    }

    fun shuffle() {
        RNG.shuffle(deck)
    }

    fun startCombat() {
        RNG.shuffle(deck)
        draw.addAll(deck)
        drawCard(DRAW_AMOUNT)
    }

    fun drawCard(count: Int) {
        val cards = draw.take(count)
        draw.removeAll(cards)
        hand.addAll(cards)

        val remainder = count - cards.size
        if (remainder > 0 && discard.isNotEmpty()) {
            shuffleDiscardIntoDraw()
            drawCard(remainder)
        }
    }

    fun discardCard(card: Card) {
        hand.remove(card)
        discard.add(card)
    }

    fun exhaustCard(card: Card) {
        CombatLogger.onMessage("${card.name} has been exhausted.")
        hand.remove(card)
        exhaust.add(card)
    }

    fun shuffleDiscardIntoDraw() {
        draw.addAll(discard)
        RNG.shuffle(draw)
        discard.clear()
    }

    fun endTurn() {
        discard.addAll(hand)
        hand.clear()

        drawCard(DRAW_AMOUNT)
    }

    fun endCombat() {
        hand.clear()
        draw.clear()
        exhaust.clear()
        discard.clear()
    }

    fun addCards(cards: List<Card>) {
        deck.addAll(cards)
        RNG.shuffle(deck)
    }

    override fun toString(): String {
        val cards = deck.groupBy { it.name.replace("+", "") }.map {
            it.key to it.value.size
        }.sortedByDescending { it.second }
            .toMap()
        val names = cards.map { "${it.value}x ${it.key}" }


        return "${deck.size}: ${names.joinToString()}"
    }

    fun play(card: Card) {
        // Only play powers once.
        if (card.type == CardType.POWER) {
            exhaustCard(card)
            return
        }

        // todo: add in other logic for post-play
        if (card is Bandaid) {
            exhaustCard(card)
        } else {
            discardCard(card)
        }
    }

    fun setCards(cards: List<Card>) {
        deck.clear()
        deck.addAll(cards)
    }


}