package com.advice.cards

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.view_card.view.*

class DeckCardView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

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

    private fun render(card: Card) {
        cost.text = card.energy.toString()
        title.text = card.name
        description.text = card.description

        alpha = if (card.canPlay(GameManager.hero)) {
            1.0f
        } else {
            0.35f
        }
    }
}