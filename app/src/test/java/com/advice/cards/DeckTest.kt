package com.advice.cards

import com.advice.cards.red.skill.Defend
import com.advice.cards.red.attack.Strike
import junit.framework.Assert.assertEquals
import org.junit.Test

class DeckTest {

    // strike x 5, shield x 3
    val starterDeck = listOf(
        Strike(),
        Strike(),
        Strike(),
        Strike(),
        Strike(),
        Defend(),
        Defend(),
        Defend()
    )

    @Test
    fun `empty piles before combat start`() {
        val deck = Deck(starterDeck)


        assert(deck.hand.size == 0)
        assert(deck.draw.size == 0)
        assert(deck.discard.size == 0)
    }

    @Test
    fun `draw 5 cards on start combat`() {
        val deck = Deck(starterDeck)

        deck.startCombat()

        assert(deck.hand.size == 5)
        assert(deck.draw.size == 3)
        assert(deck.discard.size == 0)
    }

    @Test
    fun `clear piles after combat`() {
        val deck = Deck(starterDeck)
        deck.startCombat()

        deck.endCombat()

        assert(deck.hand.size == 0)
        assert(deck.draw.size == 0)
        assert(deck.discard.size == 0)
    }

    @Test
    fun `discard card after playing it`() {
        val deck = Deck(starterDeck)
        deck.startCombat()
        val card = deck.hand.first()

        deck.discardCard(card)

        assert(deck.hand.size == 4)
        assert(deck.draw.size == 3)
        assert(deck.discard.size == 1)
    }

    @Test
    fun `draw cards with empty draw pile and discard full`() {
        val deck = Deck(starterDeck)
        deck.startCombat()
        deck.endTurn()

        assertEquals(5, deck.hand.size)
        assertEquals(3, deck.draw.size)
    }


}