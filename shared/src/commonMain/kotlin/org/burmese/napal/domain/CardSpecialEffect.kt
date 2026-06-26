package org.burmese.napal.domain

import kotlin.random.Random

sealed class CardSpecialEffect {
    data object BorderGlow : CardSpecialEffect()
    data object Pulse : CardSpecialEffect()
    data object Spin : CardSpecialEffect()
    data object LightBeam : CardSpecialEffect()
    data object FloatingEmojis : CardSpecialEffect()

    companion object {
        private val all = listOf(BorderGlow, Pulse, Spin, LightBeam, FloatingEmojis)

        fun rollRandom(): CardSpecialEffect? {
            if (Random.nextFloat() >= 0.1f) return null
            return all.random()
        }
    }
}
