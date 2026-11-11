package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.AppLanguage
import com.example.model.VoiceSnippet
import com.example.ui.components.EmotionDirectorCard
import com.example.ui.components.PitchModulationCard
import com.example.ui.components.VoicePersonaSelector
import com.example.ui.components.WaveformVisualizer
import com.example.viewmodel.StudioViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val ENGLISH_SAMPLE_SCRIPTS = listOf(
    "Sci-Fi Nebula" to "Across the celestial dunes of the neon nebula, an ancient signal whispers through starlight, awakening a forgotten world.",
    "Movie Trailer" to "In a realm where shadows rule the silence, one voice will defy the dark and ignite the eternal dawn.",
    "Tense Secret" to "Don't turn around... whatever you do, keep moving forward and listen carefully to the footsteps behind the wall.",
    "Joyful Triumph" to "We did it! Against every impossibility and every doubt, victory is ours today!",
    "Bedtime Wonder" to "Close your eyes as the gentle silver river carries all the quiet dreams of the night toward the stars."
)

private val TAMIL_SAMPLE_SCRIPTS = listOf(
    "யாதும் ஊரே (காவியம்)" to "யாதும் ஊரே யாவரும் கேளிர்... தீதும் நன்றும் பிறர்தர வாரா! நன்மை தீமை நமது சொந்த செயல்களிலேயே அடங்கியுள்ளன.",
    "மாஸ் ட்ரைலர் (சினிமா)" to "இருள் சூழ்ந்த உலகத்தில், ஒரு தீப்பொறி தோன்றி ஒட்டுமொத்த பிரபஞ்சத்தையும் வெளிச்சமாக்கும் நேரம் வந்துவிட்டது!",
    "கவிதை நயம் (இனிமை)" to "அலைகடலின் ஓசை கேட்கும் கடற்கரையில், மாலையின் இளங்காற்று மனதிற்கு எல்லையற்ற அமைதியைத் தருகிறது.",
    "தொழில்நுட்பம் (AI)" to "எதிர்காலத்தின் செயற்கை நுண்ணறிவு தொழில்நுட்பம், மனித கற்பனையை மெய்நிகர் உலகமாக மாற்றுகிறது.",
    "திகில் ரகசியம் (சஸ்பென்ஸ்)" to "திரும்பிப் பார்க்காதே... அமைதியாக நட, சுவருக்குப் பின்னால் யாரோ நம்மைப் பின்தொடரும் சத்தம் கேட்கிறது!"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VoiceStudioScreen(
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val scriptText by viewModel.scriptText.collectAsStateWithLifecycle()
    val selectedPersona by viewModel.selectedPersona.collectAsStateWithLifecycle()
    val pitch by viewModel.pitch.collectAsStateWithLifecycle()
    val speedRate by viewModel.speechRate.collectAsStateWithLifecycle()
    val modulationType by viewModel.modulationType.collectAsStateWithLifecycle()
    val modulationIntensity by viewModel.modulationIntensity.collectAsStateWithLifecycle()
    val emotionComment by viewModel.emotionComment.collectAsStateWithLifecycle()
    val emotionDirectorState by viewModel.emotionDirectorState.collectAsStateWithLifecycle()
    val isSpeaking by viewModel.isSpeaking.collectAsStateWithLifecycle()
    val amplitude by viewModel.audioAmplitude.collectAsStateWithLifecycle()
    val availableVoices by viewModel.availableVoices.collectAsStateWithLifecycle()
    val selectedSystemVoice by viewModel.selectedSystemVoice.collectAsStateWithLifecycle()
    val voiceSnippets by viewModel.voiceSnippets.collectAsStateWithLifecycle()

    val sampleScripts = if (currentLanguage == AppLanguage.TAMIL) TAMIL_SAMPLE_SCRIPTS else ENGLISH_SAMPLE_SCRIPTS

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("voice_studio_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Script Input Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("voice_script_card"),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (currentLanguage == AppLanguage.TAMIL) "குரல் உரை (Tamil Voice Script)" else "Text Given (Voice Script)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = "${scriptText.split("\\s+".toRegex()).filter { it.isNotBlank() }.size} ${if (currentLanguage == AppLanguage.TAMIL) "சொற்கள்" else "words"}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = scriptText,
                        onValueChange = { viewModel.updateScriptText(it) },
                        label = { Text(if (currentLanguage == AppLanguage.TAMIL) "பேச வேண்டிய உரை (தமிழ் / English)" else "Script to Speak") },
                        placeholder = { Text(if (currentLanguage == AppLanguage.TAMIL) "தமிழில் உரையை உள்ளிடவும்..." else "Enter text to synthesize with custom voice, pitch & emotion...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("script_text_input"),
                        minLines = 3,
                        maxLines = 6,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Preset Script Prompts
                    Text(
                        text = if (currentLanguage == AppLanguage.TAMIL) "மாதிரி உரைகள் (Sample Scripts):" else "Sample Scripts:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        sampleScripts.forEach { (label, text) ->
                            FilterChip(
                                selected = scriptText == text,
                                onClick = { viewModel.updateScriptText(text) },
                                label = { Text(label, style = MaterialTheme.typography.labelSmall) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer
                                ),
                                modifier = Modifier.testTag("sample_script_${label.lowercase().replace(" ", "_").take(15)}")
                            )
                        }
                    }
                }
            }
        }

        // 2. Waveform & Main Player Controls
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("playback_visualizer_card"),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    WaveformVisualizer(
                        isSpeaking = isSpeaking,
                        amplitude = amplitude,
                        pitch = pitch
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (isSpeaking) {
                                    if (currentLanguage == AppLanguage.TAMIL) "பேசுகிறது • ${selectedPersona.name}" else "Speaking • ${selectedPersona.name}"
                                } else {
                                    if (currentLanguage == AppLanguage.TAMIL) "தயாராக உள்ளது (Ready)" else "Ready to Synthesize"
                                },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Pitch: ${String.format(Locale.US, "%.2f", pitch)}x • Rate: ${String.format(Locale.US, "%.2f", speedRate)}x • ${if (currentLanguage == AppLanguage.TAMIL) modulationType.tamilName else modulationType.displayName}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Button(
                            onClick = {
                                if (isSpeaking) viewModel.stopVoice() else viewModel.playVoice()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSpeaking) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                            ),
                            shape = CircleShape,
                            modifier = Modifier
                                .height(48.dp)
                                .testTag("play_voice_button")
                        ) {
                            Icon(
                                imageVector = if (isSpeaking) Icons.Default.Stop else Icons.Default.PlayArrow,
                                contentDescription = if (isSpeaking) "Stop Voice" else "Play Voice",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                if (isSpeaking) {
                                    if (currentLanguage == AppLanguage.TAMIL) "நிறுத்து" else "Stop"
                                } else {
                                    if (currentLanguage == AppLanguage.TAMIL) "குரல் கேட்க" else "Speak Voice"
                                },
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // 3. Voice Personas & Timbre Selector (Filtered by Language)
        item {
            VoicePersonaSelector(
                selectedPersona = selectedPersona,
                currentLanguage = currentLanguage,
                availableSystemVoices = availableVoices,
                selectedSystemVoice = selectedSystemVoice,
                onPersonaSelected = { viewModel.selectPersona(it) },
                onSystemVoiceSelected = { viewModel.selectSystemVoice(it) }
            )
        }

        // 4. Pitch & Modulation Alteration Controls
        item {
            PitchModulationCard(
                pitch = pitch,
                speedRate = speedRate,
                modulationType = modulationType,
                modulationIntensity = modulationIntensity,
                onPitchChange = { viewModel.updatePitch(it) },
                onSpeedRateChange = { viewModel.updateSpeechRate(it) },
                onModulationChange = { viewModel.updateModulationType(it) },
                onModulationIntensityChange = { viewModel.updateModulationIntensity(it) },
                onResetDefaults = {
                    viewModel.updatePitch(selectedPersona.basePitch)
                    viewModel.updateSpeechRate(selectedPersona.baseRate)
                    viewModel.updateModulationType(selectedPersona.defaultModulation)
                }
            )
        }

        // 5. Emotion Comments & Directorial AI Enhancement
        item {
            EmotionDirectorCard(
                emotionComment = emotionComment,
                directorState = emotionDirectorState,
                currentLanguage = currentLanguage,
                onCommentChange = { viewModel.updateEmotionComment(it) },
                onApplyPreset = { viewModel.applyEmotionPreset(it) },
                onEnhanceWithAi = { viewModel.enhanceVoiceEmotionWithAi() },
                onApplyRecommendations = { viewModel.applyDirectorRecommendations(it) }
            )
        }

        // 6. Voice Snippet History
        if (voiceSnippets.isNotEmpty()) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (currentLanguage == AppLanguage.TAMIL) "பதிவு செய்யப்பட்ட குரல்கள் (${voiceSnippets.size})" else "Session Voice Snippets (${voiceSnippets.size})",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            items(voiceSnippets, key = { it.id }) { snippet ->
                VoiceSnippetItem(
                    snippet = snippet,
                    onReplay = {
                        viewModel.updateScriptText(snippet.text)
                        viewModel.updatePitch(snippet.pitch)
                        viewModel.updateSpeechRate(snippet.rate)
                        viewModel.updateModulationType(snippet.modulationType)
                        viewModel.playVoice()
                    }
                )
            }
        }
    }
}

@Composable
private fun VoiceSnippetItem(
    snippet: VoiceSnippet,
    onReplay: () -> Unit
) {
    val dateStr = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(snippet.timestamp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("voice_snippet_${snippet.id}"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = snippet.personaName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Pitch: ${String.format(Locale.US, "%.2f", snippet.pitch)}x • $dateStr",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = snippet.text,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2
                )
            }

            IconButton(
                onClick = onReplay,
                modifier = Modifier.testTag("replay_snippet_button")
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Replay snippet",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
