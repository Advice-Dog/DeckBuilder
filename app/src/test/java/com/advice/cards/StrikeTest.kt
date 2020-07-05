package com.advice.cards

import com.advice.cards.red.attack.Strike
import org.junit.Test

class StrikeTest {

    @Test
    fun `card starts at level 1`() {
        val card = Strike()

        assert(card.level == 1)
    }

    @Test
    fun `card can be upgraded to level 2`() {
        val card = Strike()

        card.upgrade()

        assert(card.level == 2)
    }

    @Test
    fun `card can only be upgraded once`() {
        val card = Strike()

        card.upgrade()
        card.upgrade()

        assert(card.level == 2)
    }

}