package com.advice.cards.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.advice.cards.Card
import com.advice.cards.Entity
import com.advice.cards.GameManager
import com.advice.cards.R
import kotlinx.android.synthetic.main.view_card.view.*

class DeckCardView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private lateinit var card: Card

    interface OnCardSelected {
        fun onCardSelected(card: Card)
    }

    constructor(context: Context, card: Card, listener: OnCardSelected) : this(context, null) {
        render(card)

        setOnClickListener {
            listener.onCardSelected(card)
        }
    }

    init {
        View.inflate(context, R.layout.view_card, this)
    }

    fun render(card: Card) {
        this.card = card
        cost.text = card.energy.toString()
        title.text = card.name
        description.text = card.getDescription(GameManager.hero, GameManager.encounter?.target)

        alpha = if (card.canPlay(GameManager.hero)) {
            1.0f
        } else {
            0.35f
        }
    }

    fun render(self: Entity, target: Entity) {
        //description.text = card.getDescription(self, target)
    }
}