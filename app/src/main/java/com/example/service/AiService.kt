package com.example.service

import android.graphics.Bitmap
import android.util.Log
import com.example.model.EmotionDirectorResult
import com.example.model.ModulationType
import com.example.model.StudioAspectRatio
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.AspectRatio
import com.google.firebase.ai.type.ImageConfig
import com.google.firebase.ai.type.ImagePart
import com.google.firebase.ai.type.ImageSize
import com.google.firebase.ai.type.ResponseModality
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.json.JSONObject

private const val TAG = "AiService"

class AiService {

    // 1. Text & Emotion Director Model (Gemini 3.8 Flash with Structured JSON Output)
    private val emotionSchema by lazy {
        Schema.obj(
            properties = mapOf(
                "enhancedScript" to Schema.string("The emotionally adapted script with dramatic pauses, emphasis, and vocal cues (in the same language as the input text, e.g. Tamil or English)"),
                "recommendedPitch" to Schema.double("Recommended pitch value between 0.50 and 2.00 (1.0 is default)"),
                "recommendedSpeed" to Schema.double("Recommended speech speed rate between 0.50 and 2.00 (1.0 is default)"),
                "recommendedModulation" to Schema.string("One of: NATURAL, VIBRATO, STACCATO, ROBOTIC, CINEMATIC_ECHO"),
                "directorsNote" to Schema.string("Short explanation of how the pitch, speed, and emotional comments transform the voice delivery")
            ),
            optionalProperties = emptyList()
        )
    }

    private val emotionModel by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            generationConfig = generationConfig {
                responseMimeType = "application/json"
                responseSchema = emotionSchema
            }
        )
    }

    suspend fun enhanceVoiceWithEmotion(
        originalText: String,
        emotionComment: String,
        currentPitch: Float,
        currentSpeed: Float,
        languageCode: String = "en"
    ): EmotionDirectorResult = withContext(Dispatchers.IO) {
        val prompt = """
            You are a master vocal director and voice designer.
            Analyze the following text and user's emotional comments/direction to produce an enhanced performance script and voice synthesis settings.
            NOTE: If the input text or comments are in Tamil (தமிழ்), retain and enhance in authentic Tamil with appropriate pauses and expressive phrasing!

            ORIGINAL TEXT:
            "$originalText"

            USER'S EMOTION COMMENTS / DIRECTION:
            "$emotionComment"

            LANGUAGE CONTEXT: $languageCode

            CURRENT SETTINGS:
            Pitch: $currentPitch (0.50 deep bass to 2.00 high soprano, 1.0 normal)
            Speed: $currentSpeed (0.50 slow to 2.00 fast, 1.0 normal)

            REQUIREMENTS:
            1. "enhancedScript": Rewrite or annotate the text slightly to convey the requested emotion using strategic punctuation, ellipses for pregnant pauses, exclamation marks, or phonetic emphasis. Keep the language identical to the input (e.g. Tamil if Tamil, English if English).
            2. "recommendedPitch": Select a number between 0.50 and 2.00 matching the emotion (e.g. low 0.65 for ominous/epic, high 1.35 for joyful/terrified).
            3. "recommendedSpeed": Select a number between 0.50 and 2.00 (e.g. slow 0.75 for suspense, fast 1.25 for urgency).
            4. "recommendedModulation": Pick the best matching modulation: NATURAL, VIBRATO, STACCATO, ROBOTIC, or CINEMATIC_ECHO.
            5. "directorsNote": A concise 1-2 sentence directorial justification.
        """.trimIndent()

        val response = withTimeout(45_000L) {
            emotionModel.generateContent(prompt)
        }

        val jsonText = response.text ?: throw IllegalStateException("Model returned empty emotion direction.")
        Log.d(TAG, "Emotion Director response: $jsonText")

        val json = JSONObject(jsonText)
        val enhancedScript = json.optString("enhancedScript", originalText)
        val pitch = json.optDouble("recommendedPitch", currentPitch.toDouble()).toFloat().coerceIn(0.5f, 2.0f)
        val speed = json.optDouble("recommendedSpeed", currentSpeed.toDouble()).toFloat().coerceIn(0.5f, 2.0f)
        val modulationStr = json.optString("recommendedModulation", "NATURAL").uppercase()
        val modulation = try {
            ModulationType.valueOf(modulationStr)
        } catch (_: Exception) {
            ModulationType.NATURAL
        }
        val note = json.optString("directorsNote", "Emotionally adapted voice profile.")

        EmotionDirectorResult(
            enhancedScript = enhancedScript,
            recommendedPitch = pitch,
            recommendedSpeed = speed,
            recommendedModulation = modulation,
            directorsNote = note
        )
    }

    // 2. Runtime Image Generation Model (Gemini Nano Banana 2.1)
    suspend fun generateImage(
        basePrompt: String,
        stylePrompt: String,
        aspectRatio: StudioAspectRatio
    ): Bitmap = withContext(Dispatchers.IO) {
        val compositePrompt = buildString {
            append("A masterpiece visual artwork depicting: ")
            append(basePrompt.trim())
            if (stylePrompt.isNotBlank()) {
                append(". Artistic visual style: ")
                append(stylePrompt.trim())
            }
            append(". High aesthetic resolution, visually compelling composition.")
        }

        Log.d(TAG, "Generating image with prompt: $compositePrompt (Aspect: ${aspectRatio.name})")

        val apiAspectRatio = when (aspectRatio) {
            StudioAspectRatio.SQUARE_1_1 -> AspectRatio.SQUARE_1x1
            StudioAspectRatio.LANDSCAPE_16_9 -> AspectRatio.LANDSCAPE_16x9
            StudioAspectRatio.PORTRAIT_9_16 -> AspectRatio.PORTRAIT_9x16
            StudioAspectRatio.LANDSCAPE_4_3 -> AspectRatio.LANDSCAPE_4x3
            StudioAspectRatio.PORTRAIT_3_4 -> AspectRatio.PORTRAIT_3x4
        }

        val config = generationConfig {
            responseModalities = listOf(ResponseModality.IMAGE)
            imageConfig = ImageConfig.builder()
                .setAspectRatio(apiAspectRatio)
                .setImageSize(ImageSize.SIZE_1K)
                .build()
        }

        val imageModel = Firebase.ai.generativeModel(
            modelName = "gemini-nano-banana-2.1",
            generationConfig = config
        )

        val response = withTimeout(60_000L) {
            imageModel.generateContent(compositePrompt)
        }

        val candidate = response.candidates.firstOrNull()
            ?: throw IllegalStateException("No candidate returned by Gemini image model.")

        val imagePart = candidate.content.parts.filterIsInstance<ImagePart>().firstOrNull()
            ?: throw IllegalStateException("Model response did not contain an image part.")

        imagePart.image ?: throw IllegalStateException("Decoded image bitmap was null.")
    }
}
