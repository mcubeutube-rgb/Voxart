package com.example.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.speech.tts.Voice
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.AppLanguage
import com.example.model.EmotionDirectorResult
import com.example.model.EmotionPreset
import com.example.model.EmotionPresets
import com.example.model.GeneratedPictureItem
import com.example.model.ImageStylePreset
import com.example.model.ImageStylePresets
import com.example.model.ModulationType
import com.example.model.StudioAspectRatio
import com.example.model.VoicePersona
import com.example.model.VoicePersonas
import com.example.model.VoiceSnippet
import com.example.service.AiService
import com.example.service.TtsVoiceService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "StudioViewModel"

sealed interface ImageGenState {
    data object Idle : ImageGenState
    data class Loading(val message: String = "Synthesizing image with Gemini...") : ImageGenState
    data class Success(val picture: GeneratedPictureItem) : ImageGenState
    data class Error(val message: String) : ImageGenState
}

sealed interface EmotionDirectorState {
    data object Idle : EmotionDirectorState
    data object Loading : EmotionDirectorState
    data class Success(val result: EmotionDirectorResult) : EmotionDirectorState
    data class Error(val message: String) : EmotionDirectorState
}

class StudioViewModel(application: Application) : AndroidViewModel(application) {

    private val ttsService = TtsVoiceService(application)
    private val aiService = AiService()

    // 0. Language State (English / Tamil)
    private val _currentLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    // 1. Text Script State
    private val _scriptText = MutableStateFlow(
        "Across the celestial dunes of the neon nebula, an ancient signal whispers through the starlight, awakening a forgotten world."
    )
    val scriptText: StateFlow<String> = _scriptText.asStateFlow()

    // 2. Voice Persona & Parameters
    private val _selectedPersona = MutableStateFlow<VoicePersona>(VoicePersonas.ENGLISH_PERSONAS[0])
    val selectedPersona: StateFlow<VoicePersona> = _selectedPersona.asStateFlow()

    private val _pitch = MutableStateFlow(1.25f)
    val pitch: StateFlow<Float> = _pitch.asStateFlow()

    private val _speechRate = MutableStateFlow(1.0f)
    val speechRate: StateFlow<Float> = _speechRate.asStateFlow()

    private val _modulationType = MutableStateFlow(ModulationType.NATURAL)
    val modulationType: StateFlow<ModulationType> = _modulationType.asStateFlow()

    private val _modulationIntensity = MutableStateFlow(0.85f)
    val modulationIntensity: StateFlow<Float> = _modulationIntensity.asStateFlow()

    // Emotion Comments for Voice
    private val _emotionComment = MutableStateFlow(
        "Deliver with breathless cosmic wonder, subtle suspense, and majestic pauses."
    )
    val emotionComment: StateFlow<String> = _emotionComment.asStateFlow()

    private val _emotionDirectorState = MutableStateFlow<EmotionDirectorState>(EmotionDirectorState.Idle)
    val emotionDirectorState: StateFlow<EmotionDirectorState> = _emotionDirectorState.asStateFlow()

    // 3. Image Generation State
    private val _imagePrompt = MutableStateFlow(
        "A breathtaking glowing crystal observatory on a mystical starlit desert planet with twin luminous moons"
    )
    val imagePrompt: StateFlow<String> = _imagePrompt.asStateFlow()

    private val _customStylePrompt = MutableStateFlow(
        "Cyberpunk synthwave glow, wet reflections, vibrant cyan and magenta lights"
    )
    val customStylePrompt: StateFlow<String> = _customStylePrompt.asStateFlow()

    private val _selectedStylePreset = MutableStateFlow<ImageStylePreset?>(ImageStylePresets.ALL[5])
    val selectedStylePreset: StateFlow<ImageStylePreset?> = _selectedStylePreset.asStateFlow()

    private val _selectedAspectRatio = MutableStateFlow(StudioAspectRatio.SQUARE_1_1)
    val selectedAspectRatio: StateFlow<StudioAspectRatio> = _selectedAspectRatio.asStateFlow()

    private val _imageGenState = MutableStateFlow<ImageGenState>(ImageGenState.Idle)
    val imageGenState: StateFlow<ImageGenState> = _imageGenState.asStateFlow()

    // History and Collections
    private val _imageGallery = MutableStateFlow<List<GeneratedPictureItem>>(emptyList())
    val imageGallery: StateFlow<List<GeneratedPictureItem>> = _imageGallery.asStateFlow()

    private val _voiceSnippets = MutableStateFlow<List<VoiceSnippet>>(emptyList())
    val voiceSnippets: StateFlow<List<VoiceSnippet>> = _voiceSnippets.asStateFlow()

    // Audiovisual Showcase pairing
    private val _showcasePicture = MutableStateFlow<GeneratedPictureItem?>(null)
    val showcasePicture: StateFlow<GeneratedPictureItem?> = _showcasePicture.asStateFlow()

    // TTS Flow delegation
    val isSpeaking: StateFlow<Boolean> = ttsService.isSpeaking
    val audioAmplitude: StateFlow<Float> = ttsService.audioAmplitude
    val availableVoices: StateFlow<List<Voice>> = ttsService.availableVoices
    val selectedSystemVoice: StateFlow<Voice?> = ttsService.selectedSystemVoice

    // Language Toggle & Presets Switcher
    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language
        ttsService.setLanguageLocale(language.locale)

        if (language == AppLanguage.TAMIL) {
            val tamilPersona = VoicePersonas.TAMIL_PERSONAS[0]
            _selectedPersona.value = tamilPersona
            _pitch.value = tamilPersona.basePitch
            _speechRate.value = tamilPersona.baseRate
            _modulationType.value = tamilPersona.defaultModulation

            _scriptText.value = "யாதும் ஊரே யாவரும் கேளிர்... தீதும் நன்றும் பிறர்தர வாரா! அகிலமெங்கும் எதிரொலிக்கும் கம்பீரமான குரல்."
            _emotionComment.value = "திரைப்பட மாஸ் ட்ரைலர் போன்று, கம்பீரமான மற்றும் கணத்த குரலில், ஆழமான இடைவெளிகளுடன் பேசவும்."
            _imagePrompt.value = "தஞ்சாவூர் பெரிய கோயில் ராஜகோபுரம், தங்க நிலவொளியில் ஒளிரும் கம்பீரமான சோழர் சிற்பங்கள்"
            _selectedStylePreset.value = ImageStylePresets.ALL[0]
            _customStylePrompt.value = ImageStylePresets.ALL[0].stylePromptSuffix
        } else {
            val englishPersona = VoicePersonas.ENGLISH_PERSONAS[0]
            _selectedPersona.value = englishPersona
            _pitch.value = englishPersona.basePitch
            _speechRate.value = englishPersona.baseRate
            _modulationType.value = englishPersona.defaultModulation

            _scriptText.value = "Across the celestial dunes of the neon nebula, an ancient signal whispers through the starlight, awakening a forgotten world."
            _emotionComment.value = "Deliver with breathless cosmic wonder, subtle suspense, and majestic pauses."
            _imagePrompt.value = "A breathtaking glowing crystal observatory on a mystical starlit desert planet with twin luminous moons"
            _selectedStylePreset.value = ImageStylePresets.ALL[5]
            _customStylePrompt.value = ImageStylePresets.ALL[5].stylePromptSuffix
        }
    }

    // Update Setters
    fun updateScriptText(newText: String) {
        _scriptText.value = newText
    }

    fun updateImagePrompt(newPrompt: String) {
        _imagePrompt.value = newPrompt
    }

    fun syncTextToImagePrompt() {
        _imagePrompt.value = _scriptText.value
    }

    fun updateCustomStylePrompt(style: String) {
        _customStylePrompt.value = style
    }

    fun selectStylePreset(preset: ImageStylePreset?) {
        _selectedStylePreset.value = preset
        preset?.let {
            _customStylePrompt.value = it.stylePromptSuffix
        }
    }

    fun selectAspectRatio(ratio: StudioAspectRatio) {
        _selectedAspectRatio.value = ratio
    }

    fun selectPersona(persona: VoicePersona) {
        _selectedPersona.value = persona
        _pitch.value = persona.basePitch
        _speechRate.value = persona.baseRate
        _modulationType.value = persona.defaultModulation
    }

    fun selectSystemVoice(voice: Voice?) {
        ttsService.selectSystemVoice(voice)
    }

    fun updatePitch(newPitch: Float) {
        _pitch.value = newPitch.coerceIn(0.5f, 2.0f)
    }

    fun updateSpeechRate(newRate: Float) {
        _speechRate.value = newRate.coerceIn(0.5f, 2.0f)
    }

    fun updateModulationType(modulation: ModulationType) {
        _modulationType.value = modulation
    }

    fun updateModulationIntensity(intensity: Float) {
        _modulationIntensity.value = intensity.coerceIn(0.1f, 1.0f)
    }

    fun updateEmotionComment(comment: String) {
        _emotionComment.value = comment
    }

    fun applyEmotionPreset(preset: EmotionPreset) {
        _emotionComment.value = preset.commentPrompt
        val currentP = _selectedPersona.value.basePitch
        val currentS = _selectedPersona.value.baseRate
        _pitch.value = (currentP + preset.pitchDelta).coerceIn(0.5f, 2.0f)
        _speechRate.value = (currentS + preset.speedDelta).coerceIn(0.5f, 2.0f)
        _modulationType.value = preset.modulation
    }

    // AI Director: Enhance Emotion
    fun enhanceVoiceEmotionWithAi() {
        val text = _scriptText.value.trim()
        val comment = _emotionComment.value.trim()
        if (text.isBlank()) return

        viewModelScope.launch {
            _emotionDirectorState.value = EmotionDirectorState.Loading
            try {
                val result = aiService.enhanceVoiceWithEmotion(
                    originalText = text,
                    emotionComment = comment,
                    currentPitch = _pitch.value,
                    currentSpeed = _speechRate.value,
                    languageCode = _currentLanguage.value.code
                )
                _emotionDirectorState.value = EmotionDirectorState.Success(result)
            } catch (e: TimeoutCancellationException) {
                Log.e(TAG, "Emotion Director timed out", e)
                _emotionDirectorState.value = EmotionDirectorState.Error("Director analysis timed out. Please try again.")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Emotion Director failed", e)
                _emotionDirectorState.value = EmotionDirectorState.Error(e.localizedMessage ?: "Failed to enhance emotion.")
            }
        }
    }

    fun applyDirectorRecommendations(result: EmotionDirectorResult) {
        _scriptText.value = result.enhancedScript
        _pitch.value = result.recommendedPitch
        _speechRate.value = result.recommendedSpeed
        _modulationType.value = result.recommendedModulation
    }

    // Voice Playback
    fun playVoice() {
        val text = _scriptText.value
        ttsService.speak(
            text = text,
            pitch = _pitch.value,
            speedRate = _speechRate.value,
            modulation = _modulationType.value,
            locale = _currentLanguage.value.locale,
            modulationIntensity = _modulationIntensity.value
        )

        // Save to snippet history
        val snippet = VoiceSnippet(
            text = text,
            personaName = _selectedPersona.value.name,
            pitch = _pitch.value,
            rate = _speechRate.value,
            modulationType = _modulationType.value,
            emotionComment = _emotionComment.value,
            language = _currentLanguage.value
        )
        _voiceSnippets.value = listOf(snippet) + _voiceSnippets.value
    }

    fun stopVoice() {
        ttsService.stop()
    }

    // Image Generation with Gemini Nano Banana 2.1
    fun generatePicture() {
        val baseText = _imagePrompt.value.trim()
        val stylePrompt = _customStylePrompt.value.trim()
        if (baseText.isBlank()) return

        viewModelScope.launch {
            _imageGenState.value = ImageGenState.Loading("Synthesizing artwork with Gemini...")
            try {
                val bitmap: Bitmap = aiService.generateImage(
                    basePrompt = baseText,
                    stylePrompt = stylePrompt,
                    aspectRatio = _selectedAspectRatio.value
                )
                val item = GeneratedPictureItem(
                    baseText = baseText,
                    stylePrompt = stylePrompt,
                    fullPrompt = "$baseText, style: $stylePrompt",
                    bitmap = bitmap,
                    aspectRatio = _selectedAspectRatio.value
                )
                _imageGenState.value = ImageGenState.Success(item)
                _imageGallery.value = listOf(item) + _imageGallery.value
                _showcasePicture.value = item
            } catch (e: TimeoutCancellationException) {
                Log.e(TAG, "Image generation timed out", e)
                _imageGenState.value = ImageGenState.Error("Image synthesis timed out after 60s. Please try again.")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Image generation error", e)
                _imageGenState.value = ImageGenState.Error(e.localizedMessage ?: "Image generation failed.")
            }
        }
    }

    fun selectShowcasePicture(picture: GeneratedPictureItem) {
        _showcasePicture.value = picture
    }

    override fun onCleared() {
        super.onCleared()
        ttsService.release()
    }
}
