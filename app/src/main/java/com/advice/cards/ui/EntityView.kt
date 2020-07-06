package com.advice.cards.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.advice.cards.Enemy
import com.advice.cards.Entity
import com.advice.cards.GameManager
import com.advice.cards.R
import com.advice.cards.enemies.Cultist
import kotlinx.android.synthetic.main.view_entity.view.*

class EntityView : FrameLayout {

    private val entity: Entity

    constructor(context: Context, entity: Entity) : super(context) {
        this.entity = entity
        View.inflate(context, R.layout.view_entity, this)
        render()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        this.entity = Cultist()
        View.inflate(context, R.layout.view_entity, this)
        render()
    }


    fun render(entity: Entity = this.entity) {
        enemy_text.text = entity.toString()
        enemy.setImageResource(entity.image)
        if (entity is Enemy) {
            val self = GameManager.hero
            enemy_intent.text = entity.intent.getDescription(entity, self)
        }
    }
}