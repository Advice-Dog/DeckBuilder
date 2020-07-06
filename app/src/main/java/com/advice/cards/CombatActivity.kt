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
            view.render(encounter.hero, encounter.target)

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
        val self = encounter.hero
        val target = encounter.target

        // hero

        energy.text = "${self.getCurrentEnergy()}/${self.getMaxEnergy()}"
        hero.setImageResource(self.image)
        hero_text.text = self.toString()
        deck_size.text = self.deck.toString()

        // enemy

        enemy_text.text = target.toString()
        enemy.setImageResource(target.image)
        enemy_intent.text = target.intent.getDescription(target, self)

        // general
        combat_log.text = CombatLogger.toString()
    }

}