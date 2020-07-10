package com.advice.cards.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.advice.cards.cards.Card
import com.advice.cards.GameManager
import com.advice.cards.R
import com.advice.cards.encounters.enemies.Cultist
import com.advice.cards.encounters.enemies.JawWorm
import com.advice.cards.encounters.enemies.group
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

        val groups = listOf(
            group {
                this + Cultist()
            },
            group {
                this + JawWorm()
                this + JawWorm()
            }
        )

        GameManager.setEnemyGroup(groups.random())

        val intent = Intent(this, CombatActivity::class.java)
        startActivity(intent)
        finish()
    }

}