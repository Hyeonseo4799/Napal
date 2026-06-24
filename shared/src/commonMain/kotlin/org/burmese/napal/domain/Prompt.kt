package org.burmese.napal.domain

sealed class Prompt(val text: String) {
    data class OilPainting(val prompt: String = "${DEFAULT_PROMPT}oil painting") : Prompt(prompt)
    data class Photorealistic(val prompt: String = "${DEFAULT_PROMPT}photorealistic") : Prompt(prompt)
    data class AnimeStyle(val prompt: String = "${DEFAULT_PROMPT}anime style") : Prompt(prompt)
    data class PixelArt(val prompt: String = "${DEFAULT_PROMPT}pixel art") : Prompt(prompt)

    companion object {
        const val DEFAULT_PROMPT = "same pose\n" + "same composition\n" + "preserve details\n" +
                "preserve structure\n" + "keep original layout\n" + "maintain facial features\n" +
                "retain original image\n"

        const val NEGATIVE_PROMPT = "deformed, distorted, bad anatomy,\n" +
                "extra limbs, bad hands,\n" +
                "extra fingers, blurry"
    }
}