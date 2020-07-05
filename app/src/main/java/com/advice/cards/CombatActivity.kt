package com.advice.cards

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.advice.cards.logger.CombatLogger
import kotlinx.android.synthetic.main.activity_combat.*

class CombatActivity : Activity(), DeckCardView.OnCardSelected {


    private val encounter = GameManager.encounter!!
    private val deck = encounter.hero.deck

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_combat)

        end_turn.setOnClickListener {
            encounter.endTurn()

            if (encounter.isComplete) {
                onEncounterComplete()
            } else {
                drawHand()
                updateEntities()
            }
        }

        encounter.start()

        drawHand()
        updateEntities()
    }

    private fun onEncounterComplete() {
        encounter.end()
        CombatLogger.onMessage("Encounter is COMPLETE.")
        CombatLogger.reset()

        val intent = Intent(this, RewardsActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun drawHand() {
        hand.removeAllViews()
        val currentHand = deck.hand
        currentHand.forEach {
            val view = DeckCardView(this, it, this)
            hand.addView(view)
        }
    }

    override fun onCardSelected(card: Card) {
        encounter.playCard(card)
        drawHand()
        updateEntities()

        if (encounter.isComplete) {
            onEncounterComplete()
        }
    }

    private fun updateEntities() {
        // hero
        energy.text = "${encounter.hero.getCurrentEnergy()}/${encounter.hero.getMaxEnergy()}"
        hero.setImageResource(encounter.hero.image)
        hero_text.text = encounter.hero.toString()

        // enemy
        enemy_text.text = encounter.target.toString()
        enemy.setImageResource(encounter.target.image)
        enemy_intent.text = encounter.target.intent.toString()

        // general
        combat_log.text = CombatLogger.toString()
    }

}