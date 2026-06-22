package org.burmese.napal.platform

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform