package com.advice.cards.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.view.get
import com.advice.cards.Card
import com.advice.cards.GameManager
import com.advice.cards.R
import com.advice.cards.logger.CombatLogger
import kotlinx.android.synthetic.main.activity_combat.*

class CombatActivity : Activity(), DeckCardView.OnCardSelected {


    private val encounter = GameManager.encounter!!
    private val deck = GameManager.hero.deck

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

        encounter.onEncounterStart()

        drawHand()
        updateEntities()
    }

    private fun onEncounterComplete() {
        encounter.onEncounterEnd()
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
            view.render(GameManager.hero, encounter.target)

            hand.addView(view)
        }
    }

    override fun onCardSelected(card: Card) {
        if (!card.canPlay(GameManager.hero)) {
            CombatLogger.onMessage("Cannot use ${card.name}.")
            return
        }

        encounter.play(card)
        drawHand()
        updateEntities()

        if (encounter.isComplete) {
            onEncounterComplete()
        }
    }

    private fun updateEntities() {
        val self = GameManager.hero
        val target = encounter.target

        // hero

        energy.text = "${self.getCurrentEnergy()}/${self.getMaxEnergy()}"
        hero.render(GameManager.hero)
        deck_size.text = self.deck.toString()

        // enemy
        if (enemies.childCount == 0) {
            encounter.enemies.forEach {
                val view = EntityView(this, it)
                enemies.addView(view)
            }
        } else {
            encounter.enemies.forEachIndexed { index, enemy ->
                (enemies.get(index) as EntityView).render(enemy)
            }
        }

        // general
        combat_log.text = CombatLogger.toString()
    }

}