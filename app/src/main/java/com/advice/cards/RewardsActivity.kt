package com.advice.cards

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.advice.cards.enemies.Cultist
import com.advice.cards.enemies.JawWorm
import kotlinx.android.synthetic.main.activity_rewards.*

class RewardsActivity : Activity(), DeckCardView.OnCardSelected {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rewards)

        val cards = GameManager.getCardRewards()
        cards.forEach {
            val view = DeckCardView(this, it, this)
            rewards.addView(view)
        }
    }

    override fun onCardSelected(card: Card) {
        GameManager.deck.addCard(card)

        val enemies = listOf(
            Cultist(),
            JawWorm()
        )

        GameManager.setEnemy(enemies.random())

        val intent = Intent(this, CombatActivity::class.java)
        startActivity(intent)
        finish()
    }

}