package com.example.service

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import android.util.Log
import com.example.model.ModulationType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.UUID

private const val TAG = "TtsVoiceService"

class TtsVoiceService(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _currentWord = MutableStateFlow("")
    val currentWord: StateFlow<String> = _currentWord.asStateFlow()

    private val _audioAmplitude = MutableStateFlow(0f)
    val audioAmplitude: StateFlow<Float> = _audioAmplitude.asStateFlow()

    private val _availableVoices = MutableStateFlow<List<Voice>>(emptyList())
    val availableVoices: StateFlow<List<Voice>> = _availableVoices.asStateFlow()

    private val _selectedSystemVoice = MutableStateFlow<Voice?>(null)
    val selectedSystemVoice: StateFlow<Voice?> = _selectedSystemVoice.asStateFlow()

    private var currentTargetLocale: Locale = Locale.US

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var waveAnimationJob: Job? = null

    init {
        tts = TextToSpeech(context.applicationContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val engine = tts ?: return
            engine.language = currentTargetLocale
            isInitialized = true

            refreshVoicesForLocale(currentTargetLocale)
            setupProgressListener(engine)
            Log.d(TAG, "TextToSpeech successfully initialized with locale: $currentTargetLocale")
        } else {
            Log.e(TAG, "TextToSpeech initialization failed with status $status")
            isInitialized = false
        }
    }

    fun setLanguageLocale(locale: Locale) {
        currentTargetLocale = locale
        val engine = tts ?: return
        if (isInitialized) {
            val result = engine.setLanguage(locale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.w(TAG, "Locale $locale has missing data or is not supported by default engine.")
            }
            refreshVoicesForLocale(locale)
        }
    }

    private fun refreshVoicesForLocale(locale: Locale) {
        val engine = tts ?: return
        try {
            val allVoices = engine.voices?.filter { !it.isNetworkConnectionRequired } ?: emptyList()
            // Find voices matching the language code (e.g., "ta" or "en")
            val matchingVoices = allVoices.filter {
                it.locale.language.equals(locale.language, ignoreCase = true)
            }.sortedBy { it.name }

            _availableVoices.value = if (matchingVoices.isNotEmpty()) matchingVoices else allVoices
            _selectedSystemVoice.value = matchingVoices.firstOrNull() ?: engine.voice
        } catch (e: Exception) {
            Log.w(TAG, "Failed to load system voices for locale: $locale", e)
        }
    }

    private fun setupProgressListener(engine: TextToSpeech) {
        engine.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                _isSpeaking.value = true
                startWaveformPulsing()
            }

            override fun onDone(utteranceId: String?) {
                _isSpeaking.value = false
                _currentWord.value = ""
                stopWaveformPulsing()
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                _isSpeaking.value = false
                stopWaveformPulsing()
                Log.e(TAG, "TTS utterance error: $utteranceId")
            }

            override fun onError(utteranceId: String?, errorCode: Int) {
                _isSpeaking.value = false
                stopWaveformPulsing()
                Log.e(TAG, "TTS utterance error $errorCode on $utteranceId")
            }

            override fun onRangeStart(utteranceId: String?, start: Int, end: Int, frame: Int) {
                // Word boundaries callback
            }
        })
    }

    fun selectSystemVoice(voice: Voice?) {
        _selectedSystemVoice.value = voice
        voice?.let { tts?.voice = it }
    }

    fun speak(
        text: String,
        pitch: Float,
        speedRate: Float,
        modulation: ModulationType,
        locale: Locale = currentTargetLocale,
        modulationIntensity: Float = 1.0f
    ) {
        if (!isInitialized || tts == null) {
            Log.w(TAG, "TTS not ready yet")
            return
        }

        stop()

        val engine = tts ?: return
        engine.language = locale

        val clampedPitch = pitch.coerceIn(0.5f, 2.0f)
        val clampedRate = speedRate.coerceIn(0.5f, 2.0f)

        // Selected system voice if applicable
        _selectedSystemVoice.value?.let { engine.voice = it }

        // Apply pitch & rate
        engine.setPitch(clampedPitch)
        engine.setSpeechRate(clampedRate)

        // Process text based on modulation
        val processedText = applyModulationToText(text, modulation, modulationIntensity)

        val params = Bundle().apply {
            putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1.0f)
        }

        val utteranceId = "vox_${UUID.randomUUID()}"
        engine.speak(processedText, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
    }

    private fun applyModulationToText(
        original: String,
        modulation: ModulationType,
        intensity: Float
    ): String {
        return when (modulation) {
            ModulationType.NATURAL -> original
            ModulationType.STACCATO -> {
                original.replace(", ", ", ... ")
                    .replace("; ", "; ... ")
                    .replace(". ", ". ... ")
                    .replace("! ", "! ... ")
                    .replace("? ", "? ... ")
            }
            ModulationType.CINEMATIC_ECHO -> {
                val words = original.split(" ")
                if (words.size > 6) {
                    val half = words.size / 2
                    val firstHalf = words.subList(0, half).joinToString(" ")
                    val secondHalf = words.subList(half, words.size).joinToString(" ")
                    "$firstHalf ... ... $secondHalf"
                } else {
                    original
                }
            }
            ModulationType.ROBOTIC -> {
                original.replace(".", " ... ")
            }
            ModulationType.VIBRATO -> original
        }
    }

    fun stop() {
        tts?.stop()
        _isSpeaking.value = false
        _currentWord.value = ""
        stopWaveformPulsing()
    }

    private fun startWaveformPulsing() {
        waveAnimationJob?.cancel()
        waveAnimationJob = coroutineScope.launch {
            var step = 0
            while (isActive && _isSpeaking.value) {
                val base = (kotlin.math.sin(step * 0.4) * 0.4 + 0.5).toFloat()
                val flutter = ((step % 5) * 0.08f)
                _audioAmplitude.value = (base + flutter).coerceIn(0.15f, 0.95f)
                step++
                delay(75)
            }
            _audioAmplitude.value = 0f
        }
    }

    private fun stopWaveformPulsing() {
        waveAnimationJob?.cancel()
        waveAnimationJob = null
        _audioAmplitude.value = 0f
    }

    fun release() {
        stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
    }
}
