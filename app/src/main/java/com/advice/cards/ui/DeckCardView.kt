package com.advice.cards.ui

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
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
        val text = card.getDescription(GameManager.hero, GameManager.encounter?.target)

        val spannable = SpannableString(text)
        highlightText(text, "Block", spannable)
        highlightText(text, "Weak", spannable)
        highlightText(text, "Vulnerable", spannable)



        description.text = spannable

        alpha = if (card.canPlay(GameManager.hero)) {
            1.0f
        } else {
            0.35f
        }
    }

    private fun highlightText(
        text: String,
        string: String,
        spannable: SpannableString
    ) {
        val block = text.indexOf(string)
        if (block != -1) {
            spannable.setSpan(
                ForegroundColorSpan(Color.YELLOW),
                block,
                block + string.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    fun render(self: Entity, target: Entity) {
        //description.text = card.getDescription(self, target)
    }
}