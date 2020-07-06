package com.advice.cards

import com.advice.cards.logger.CombatLogger
import java.util.*
import kotlin.collections.ArrayList

class Deck(val cards: List<Card> = emptyList()) {

    private val deck = ArrayList<Card>()

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
        deck.shuffle(GameManager.seed)
    }

    fun startCombat() {
        deck.shuffle(GameManager.seed)
        draw.addAll(deck)
        drawCard(5)
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
        draw.shuffle(GameManager.seed)
        discard.clear()
    }

    fun endTurn() {
        discard.addAll(hand)
        hand.clear()

        drawCard(5)
    }

    fun endCombat() {
        hand.clear()
        draw.clear()
        exhaust.clear()
        discard.clear()
    }

    fun addCards(cards: List<Card>) {
        deck.addAll(cards)
        deck.shuffle(GameManager.seed)
    }

    override fun toString(): String {
        return "${deck.size} (${draw.size}/${discard.size})"
    }


}