package com.example.model

import android.graphics.Bitmap

enum class StudioAspectRatio(val label: String, val apiValue: String, val ratioFloat: Float) {
    SQUARE_1_1("1:1 Square", "1:1", 1.0f),
    LANDSCAPE_16_9("16:9 Cinema", "16:9", 16f / 9f),
    PORTRAIT_9_16("9:16 Story", "9:16", 9f / 16f),
    LANDSCAPE_4_3("4:3 Classic", "4:3", 4f / 3f),
    PORTRAIT_3_4("3:4 Canvas", "3:4", 3f / 4f)
}

data class ImageStylePreset(
    val id: String,
    val name: String,
    val tamilName: String,
    val stylePromptSuffix: String,
    val description: String,
    val iconEmoji: String,
    val colorHex: Long,
    val isTamilSpecific: Boolean = false
)

object ImageStylePresets {
    val ALL = listOf(
        // Tamil Heritage & Cinema Styles
        ImageStylePreset(
            id = "thanjavur_gold",
            name = "Thanjavur Gold Art",
            tamilName = "தஞ்சாவூர் ஓவியம்",
            stylePromptSuffix = "Traditional Thanjavur painting style, rich 22k gold foil leaf embossed inlay, semi-precious gem ornamentation, rich natural mineral pigments, divine classical South Indian royal art",
            description = "Classical Thanjavur painting with 22k gold foil leaf and gems",
            iconEmoji = "✨",
            colorHex = 0xFFFDCB6E,
            isTamilSpecific = true
        ),
        ImageStylePreset(
            id = "chola_bronze",
            name = "Chola Bronze & Temple",
            tamilName = "சோழர் சிற்பக்கலை",
            stylePromptSuffix = "Ancient Chola bronze sculpture aesthetic, divine lost-wax casting patina, Thanjavur Brihadisvara granite stone carvings, warm golden temple oil lamp glow",
            description = "Sacred Chola bronze casting and Dravidian temple granite relief",
            iconEmoji = "🛕",
            colorHex = 0xFFE17055,
            isTamilSpecific = true
        ),
        ImageStylePreset(
            id = "kollywood_cinema",
            name = "Kollywood Cinema",
            tamilName = "தமிழ் சினிமா மாஸ்",
            stylePromptSuffix = "Kollywood cinema visual aesthetic, Mani Ratnam cinematic lighting, dramatic monsoon rain atmosphere, neon flare backlight, epic slow-motion heroic frame",
            description = "Dramatic Tamil blockbuster cinema lighting and monsoon depth",
            iconEmoji = "🎬",
            colorHex = 0xFFD63031,
            isTamilSpecific = true
        ),
        ImageStylePreset(
            id = "tamil_kolam",
            name = "Tamil Folk & Kolam",
            tamilName = "கிராமிய கோலக்கலை",
            stylePromptSuffix = "Vibrant Tamil Nadu village folk art, intricate white rice powder Kolam geometric mandala patterns, terracotta pottery warmth, festive sunny Pongal ambiance",
            description = "Intricate traditional rice flour Kolam and rural Tamil folk aesthetics",
            iconEmoji = "🪷",
            colorHex = 0xFFFF7675,
            isTamilSpecific = true
        ),

        // Global Artistic Styles
        ImageStylePreset(
            id = "anime_ghibli",
            name = "Anime Ghibli",
            tamilName = "அனிமே கிப்லி",
            stylePromptSuffix = "Studio Ghibli aesthetic, lush hand-painted watercolor background, whimsical soft sunlight, vibrant anime colors, highly detailed Makoto Shinkai sky",
            description = "Dreamy, nostalgic hand-painted anime look with lush atmospheres",
            iconEmoji = "🌸",
            colorHex = 0xFFFF7675
        ),
        ImageStylePreset(
            id = "cyberpunk_neon",
            name = "Cyberpunk Neon",
            tamilName = "சைபர்பங்க் நியான்",
            stylePromptSuffix = "High-tech cyberpunk aesthetic, wet asphalt reflections, glowing neon magenta and cyan lighting, holographic fog, dark futuristic metropolis",
            description = "Electric neon lighting, dark alleys, and synthwave tech reflections",
            iconEmoji = "⚡",
            colorHex = 0xFF00CEC9
        ),
        ImageStylePreset(
            id = "cinematic_photo",
            name = "35mm Cinema",
            tamilName = "35மிமீ திரைப்படம்",
            stylePromptSuffix = "35mm anamorphic film photography, dramatic chiaroscuro lighting, shallow depth of field, natural film grain, authentic cinematic still from a Ridley Scott film",
            description = "Photorealistic 35mm film still with master lighting & depth of field",
            iconEmoji = "📽️",
            colorHex = 0xFFFDCB6E
        ),
        ImageStylePreset(
            id = "oil_impasto",
            name = "Oil Impasto",
            tamilName = "ஆயில் பெயிண்டிங்",
            stylePromptSuffix = "Classical master oil painting, thick visible impasto palette knife brush strokes, rich textured canvas, vibrant dramatic oil pigments, Rembrandt lighting",
            description = "Richly textured oil paint with visible palette knife strokes",
            iconEmoji = "🎨",
            colorHex = 0xFFE17055
        ),
        ImageStylePreset(
            id = "pixar_3d",
            name = "3D Pixar Render",
            tamilName = "3D பிக்ஸார் அனிமேஷன்",
            stylePromptSuffix = "Stylized 3D CGI animation, Pixar Disney rendering style, warm ray-traced ambient lighting, subsurface scattering, expressive charming character art",
            description = "Playful, lovable 3D CGI render with soft studio lighting",
            iconEmoji = "🧸",
            colorHex = 0xFF6C5CE7
        ),
        ImageStylePreset(
            id = "retro_comic",
            name = "Retro Comic Pop",
            tamilName = "காமிக்ஸ் பாப் கலை",
            stylePromptSuffix = "Vintage 1970s comic book pop art, authentic Ben-Day halftone dot pattern, dynamic ink line hatching, vibrant limited CMYK palette, heroic action panel",
            description = "Classic comic book action panel with bold inks & halftone dots",
            iconEmoji = "💥",
            colorHex = 0xFFD63031
        ),
        ImageStylePreset(
            id = "watercolor_ink",
            name = "Sumi-e & Watercolor",
            tamilName = "நீர்வர்ண தூரிகை",
            stylePromptSuffix = "Traditional Japanese sumi-e black ink wash combined with delicate translucent watercolor blooms on fibrous rice paper, minimalist zen elegance",
            description = "Delicate translucent watercolor blooms and fluid ink washes",
            iconEmoji = "🖌️",
            colorHex = 0xFF0984E3
        ),
        ImageStylePreset(
            id = "dark_fantasy",
            name = "Dark Fantasy",
            tamilName = "டார்க் பேண்டஸி",
            stylePromptSuffix = "Epic dark fantasy concept art, Elden Ring and Dark Souls inspired, moody ominous fog, ancient glowing runes, intricate gothic grandeur, atmospheric lighting",
            description = "Grim gothic grandeur, ancient runes, and moody fog",
            iconEmoji = "⚔️",
            colorHex = 0xFF636E72
        )
    )
}

data class GeneratedPictureItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val baseText: String,
    val stylePrompt: String,
    val fullPrompt: String,
    val bitmap: Bitmap,
    val aspectRatio: StudioAspectRatio,
    val timestamp: Long = System.currentTimeMillis()
)
