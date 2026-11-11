package com.example.model

import java.util.Locale

enum class AppLanguage(
    val code: String,
    val displayName: String,
    val nativeName: String,
    val locale: Locale
) {
    ENGLISH("en", "English", "English", Locale.US),
    TAMIL("ta", "Tamil", "தமிழ்", Locale("ta", "IN"))
}

enum class ModulationType(val displayName: String, val tamilName: String, val description: String) {
    NATURAL("Natural Smooth", "இயல்பான மென்மை", "Organic cadence with smooth, balanced transitions"),
    VIBRATO("Vibrato Modulation", "அதிர்வு ஏற்ற இறக்கம்", "Gentle frequency oscillation and undulating resonance"),
    STACCATO("Staccato & Punchy", "துடிப்பான அழுத்தம்", "Crisp, rhythmic articulation with punctuated micro-pauses"),
    ROBOTIC("Vocoder Cyber", "ரோபோடிக் குரல்", "Flat synthetic pitch contour with monotonic cadence"),
    CINEMATIC_ECHO("Cinematic Trailer", "திரைப்பட எதிரொலி", "Expansive dramatic phrasing with deep acoustic gravity")
}

data class VoicePersona(
    val id: String,
    val name: String,
    val title: String,
    val gender: String,
    val basePitch: Float,
    val baseRate: Float,
    val defaultModulation: ModulationType,
    val description: String,
    val accentTag: String,
    val tagColorHex: Long,
    val language: AppLanguage = AppLanguage.ENGLISH
)

object VoicePersonas {
    val ENGLISH_PERSONAS = listOf(
        VoicePersona(
            id = "aura",
            name = "Aura",
            title = "Warm Soprano",
            gender = "Female",
            basePitch = 1.25f,
            baseRate = 1.0f,
            defaultModulation = ModulationType.NATURAL,
            description = "Bright, melodious and engaging for storytelling and guides.",
            accentTag = "Bright & Warm",
            tagColorHex = 0xFFFF7675,
            language = AppLanguage.ENGLISH
        ),
        VoicePersona(
            id = "titan",
            name = "Titan",
            title = "Deep Resonant Baritone",
            gender = "Male",
            basePitch = 0.72f,
            baseRate = 0.90f,
            defaultModulation = ModulationType.CINEMATIC_ECHO,
            description = "Rich, subwoofer-deep authority for movie trailers and history.",
            accentTag = "Deep Cinema",
            tagColorHex = 0xFF6C5CE7,
            language = AppLanguage.ENGLISH
        ),
        VoicePersona(
            id = "verve",
            name = "Verve",
            title = "Enthusiastic Host",
            gender = "Dynamic",
            basePitch = 1.15f,
            baseRate = 1.18f,
            defaultModulation = ModulationType.STACCATO,
            description = "High energy, punchy pace ideal for commercials and podcasts.",
            accentTag = "Upbeat Energetic",
            tagColorHex = 0xFF00CEC9,
            language = AppLanguage.ENGLISH
        ),
        VoicePersona(
            id = "zephyr",
            name = "Zephyr",
            title = "Gentle Whisperer",
            gender = "Calm",
            basePitch = 0.95f,
            baseRate = 0.82f,
            defaultModulation = ModulationType.NATURAL,
            description = "Soothing, intimate and airy for meditations and bedtime tales.",
            accentTag = "Serene & Airy",
            tagColorHex = 0xFF81ECEC,
            language = AppLanguage.ENGLISH
        ),
        VoicePersona(
            id = "nova",
            name = "Nova",
            title = "Cyber Synth Android",
            gender = "Synthetic",
            basePitch = 1.40f,
            baseRate = 1.05f,
            defaultModulation = ModulationType.ROBOTIC,
            description = "Futuristic AI agent tone with sharp electronic frequencies.",
            accentTag = "Sci-Fi Android",
            tagColorHex = 0xFFFD79A8,
            language = AppLanguage.ENGLISH
        ),
        VoicePersona(
            id = "echo",
            name = "Echo",
            title = "Dramatic Narrator",
            gender = "Epic",
            basePitch = 0.65f,
            baseRate = 0.85f,
            defaultModulation = ModulationType.CINEMATIC_ECHO,
            description = "Monumental epic tone with suspenseful pauses and weight.",
            accentTag = "Epic Drama",
            tagColorHex = 0xFFA29BFE,
            language = AppLanguage.ENGLISH
        ),
        VoicePersona(
            id = "serena",
            name = "Serena",
            title = "Articulate Presenter",
            gender = "Female",
            basePitch = 1.05f,
            baseRate = 1.00f,
            defaultModulation = ModulationType.NATURAL,
            description = "Refined broadcast-quality clarity with steady cadence.",
            accentTag = "Clear Broadcast",
            tagColorHex = 0xFFFDCB6E,
            language = AppLanguage.ENGLISH
        ),
        VoicePersona(
            id = "shadow",
            name = "Shadow",
            title = "Mysterious Enigma",
            gender = "Dark",
            basePitch = 0.60f,
            baseRate = 0.78f,
            defaultModulation = ModulationType.VIBRATO,
            description = "Hushed, raspy noir suspense with subtle eerie resonance.",
            accentTag = "Dark Noir",
            tagColorHex = 0xFFB2BEC3,
            language = AppLanguage.ENGLISH
        )
    )

    val TAMIL_PERSONAS = listOf(
        VoicePersona(
            id = "maran_ta",
            name = "மாறன் (Maran)",
            title = "கம்பீரமான குரல் (Resonant Baritone)",
            gender = "ஆண் (Male)",
            basePitch = 0.75f,
            baseRate = 0.88f,
            defaultModulation = ModulationType.CINEMATIC_ECHO,
            description = "கம்பீரமான தமிழ் ஆடியோ குரல், சினிமா ட்ரைலர் மற்றும் வரலாற்று காவியங்களுக்கு ஏற்றது.",
            accentTag = "கம்பீரம் (Cinema Epic)",
            tagColorHex = 0xFF6C5CE7,
            language = AppLanguage.TAMIL
        ),
        VoicePersona(
            id = "yazhini_ta",
            name = "யாழினி (Yazhini)",
            title = "இனிய மெல்லிசை (Melodious Soprano)",
            gender = "பெண் (Female)",
            basePitch = 1.20f,
            baseRate = 0.96f,
            defaultModulation = ModulationType.NATURAL,
            description = "தெளிவான, கனிவான மற்றும் இனிமையான தூய தமிழ் குரல், கவிதைகள் மற்றும் கதைசொல்லலுக்கு சிறந்தது.",
            accentTag = "மெல்லிசை (Melodious)",
            tagColorHex = 0xFFFF7675,
            language = AppLanguage.TAMIL
        ),
        VoicePersona(
            id = "kavin_ta",
            name = "கவின் (Kavin)",
            title = "துடிப்பான குரல் (Energetic Host)",
            gender = "நவீன (Dynamic)",
            basePitch = 1.10f,
            baseRate = 1.15f,
            defaultModulation = ModulationType.STACCATO,
            description = "வேகமான, சுறுசுறுப்பான வானொலி நிகழ்ச்சி மற்றும் விளம்பர குரல்.",
            accentTag = "துடிப்பு (Energetic)",
            tagColorHex = 0xFF00CEC9,
            language = AppLanguage.TAMIL
        ),
        VoicePersona(
            id = "thendral_ta",
            name = "தென்றல் (Thendral)",
            title = "மென்மையான கதைசொல்லி (Gentle Calm)",
            gender = "அமைதி (Calm)",
            basePitch = 0.92f,
            baseRate = 0.82f,
            defaultModulation = ModulationType.NATURAL,
            description = "அமைதியான, இதயத்தை வருடும் இதமான தாலாட்டு மற்றும் தியான குரல்.",
            accentTag = "அமைதி (Serene)",
            tagColorHex = 0xFF81ECEC,
            language = AppLanguage.TAMIL
        )
    )

    val ALL = ENGLISH_PERSONAS + TAMIL_PERSONAS
}

data class EmotionPreset(
    val id: String,
    val title: String,
    val commentPrompt: String,
    val pitchDelta: Float,
    val speedDelta: Float,
    val modulation: ModulationType,
    val emoji: String,
    val language: AppLanguage = AppLanguage.ENGLISH
)

object EmotionPresets {
    val ENGLISH_PRESETS = listOf(
        EmotionPreset(
            id = "dramatic_epic",
            title = "Epic Cinema",
            commentPrompt = "Deliver with thunderous grandeur, slow pregnant pauses, and intense emotional gravity like a blockbuster trailer narrator.",
            pitchDelta = -0.28f,
            speedDelta = -0.15f,
            modulation = ModulationType.CINEMATIC_ECHO,
            emoji = "🎬",
            language = AppLanguage.ENGLISH
        ),
        EmotionPreset(
            id = "whisper_suspense",
            title = "Tense Whisper",
            commentPrompt = "Speak in a hushed, trembling whisper with breathy pacing, as if hiding from a lurking shadow in the dark.",
            pitchDelta = -0.15f,
            speedDelta = -0.22f,
            modulation = ModulationType.NATURAL,
            emoji = "🤫",
            language = AppLanguage.ENGLISH
        ),
        EmotionPreset(
            id = "triumphant_joy",
            title = "Triumphant Joy",
            commentPrompt = "Bursting with triumphant celebration, infectious optimism, smiling inflection, and lively bright upward pitch lilt!",
            pitchDelta = +0.30f,
            speedDelta = +0.15f,
            modulation = ModulationType.STACCATO,
            emoji = "🎉",
            language = AppLanguage.ENGLISH
        ),
        EmotionPreset(
            id = "cyber_android",
            title = "Cyborg Protocol",
            commentPrompt = "Strictly analytical synthetic AI unit, zero human emotion, monotonic clipped cadence, surgical precision.",
            pitchDelta = +0.25f,
            speedDelta = +0.05f,
            modulation = ModulationType.ROBOTIC,
            emoji = "🤖",
            language = AppLanguage.ENGLISH
        ),
        EmotionPreset(
            id = "poetic_melancholy",
            title = "Nostalgic Sigh",
            commentPrompt = "Reflective, tender and nostalgic, like remembering an old forgotten love on a rainy autumn evening.",
            pitchDelta = -0.10f,
            speedDelta = -0.18f,
            modulation = ModulationType.VIBRATO,
            emoji = "🍂",
            language = AppLanguage.ENGLISH
        ),
        EmotionPreset(
            id = "frantic_urgency",
            title = "Alarm Urgency",
            commentPrompt = "Breathless urgent warning! Heart racing, breathless gasps, rapid clipped tempo warning everyone to take cover immediately!",
            pitchDelta = +0.35f,
            speedDelta = +0.30f,
            modulation = ModulationType.STACCATO,
            emoji = "🚨",
            language = AppLanguage.ENGLISH
        )
    )

    val TAMIL_PRESETS = listOf(
        EmotionPreset(
            id = "dramatic_epic_ta",
            title = "வீர கம்பீரம் (Epic Cinema)",
            commentPrompt = "திரைப்பட மாஸ் ட்ரைலர் போன்று, கம்பீரமான மற்றும் கணத்த குரலில், ஆழமான இடைவெளிகளுடன் பேசவும்.",
            pitchDelta = -0.25f,
            speedDelta = -0.15f,
            modulation = ModulationType.CINEMATIC_ECHO,
            emoji = "🎬",
            language = AppLanguage.TAMIL
        ),
        EmotionPreset(
            id = "whisper_suspense_ta",
            title = "திகில் ரகசியம் (Tense Whisper)",
            commentPrompt = "அமைதியான மெல்லிய கிசுகிசுப்பான திகில் குரலில், மூச்சிரைக்கும் இடைவெளிகளுடன் பயத்துடன் பேசவும்.",
            pitchDelta = -0.15f,
            speedDelta = -0.20f,
            modulation = ModulationType.NATURAL,
            emoji = "🤫",
            language = AppLanguage.TAMIL
        ),
        EmotionPreset(
            id = "triumphant_joy_ta",
            title = "வெற்றி கொண்டாட்டம் (Joyous Triumph)",
            commentPrompt = "வெற்றி பெற்ற மட்டற்ற மகிழ்ச்சியுடன், உற்சாகமான புன்னகையுடன் கூடிய துள்ளலான உச்சரிப்பில் பேசவும்!",
            pitchDelta = +0.28f,
            speedDelta = +0.15f,
            modulation = ModulationType.STACCATO,
            emoji = "🎉",
            language = AppLanguage.TAMIL
        ),
        EmotionPreset(
            id = "poetic_love_ta",
            title = "கவிதை நயம் (Poetic Melody)",
            commentPrompt = "சங்கத் தமிழ் கவிதை நயத்துடன், மனதை வருடும் இனிமையான மென்மையான குரலில் பேசவும்.",
            pitchDelta = +0.05f,
            speedDelta = -0.10f,
            modulation = ModulationType.NATURAL,
            emoji = "🪷",
            language = AppLanguage.TAMIL
        ),
        EmotionPreset(
            id = "cyber_ai_ta",
            title = "செயற்கை நுண்ணறிவு (AI Cyber)",
            commentPrompt = "எதிர்கால ரோபோடிக் கணினி குரல் போன்று உணர்ச்சியற்ற, துல்லியமான வேகத்தில் பேசவும்.",
            pitchDelta = +0.22f,
            speedDelta = +0.05f,
            modulation = ModulationType.ROBOTIC,
            emoji = "🤖",
            language = AppLanguage.TAMIL
        )
    )

    val ALL = ENGLISH_PRESETS + TAMIL_PRESETS
}

data class EmotionDirectorResult(
    val enhancedScript: String,
    val recommendedPitch: Float,
    val recommendedSpeed: Float,
    val recommendedModulation: ModulationType,
    val directorsNote: String
)

data class VoiceSnippet(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val personaName: String,
    val pitch: Float,
    val rate: Float,
    val modulationType: ModulationType,
    val emotionComment: String,
    val language: AppLanguage = AppLanguage.ENGLISH,
    val timestamp: Long = System.currentTimeMillis()
)
