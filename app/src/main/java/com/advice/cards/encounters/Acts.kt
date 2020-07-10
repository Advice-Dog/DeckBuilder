package com.advice.cards.encounters

import com.advice.cards.encounters.enemies.*

val act = act("Act I") {
    // First 3 encounters
    this + group {
        this + Cultist()
    }

    this + group {
        this + JawWorm()
    }

    this + group {
        this + Louse()
        this + Louse()
    }

    // todo: add small slimes

    // blue slaver (12.5%)
    this + group {
        this + Slaver()
    }

    this + group {
        this + Louse()
        this + Louse()
        this + Louse()
    }

    // other
    this + group {
        this + Cultist()
    }

    this + group {
        this + JawWorm()
        this + JawWorm()
    }

    this + group {
        this + Slaver()
    }

    this + group {
        this + Cultist()
    }

    this + group {
        this + JawWorm()
        this + JawWorm()
    }

    this + group {
        this + Slaver()
    }

    this + group {
        this + Slaver()
    }

    this + group {
        this + Boss()
    }
}