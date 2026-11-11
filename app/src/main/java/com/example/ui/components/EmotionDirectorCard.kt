package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.AppLanguage
import com.example.model.EmotionDirectorResult
import com.example.model.EmotionPreset
import com.example.model.EmotionPresets
import com.example.viewmodel.EmotionDirectorState
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EmotionDirectorCard(
    emotionComment: String,
    directorState: EmotionDirectorState,
    currentLanguage: AppLanguage = AppLanguage.ENGLISH,
    onCommentChange: (String) -> Unit,
    onApplyPreset: (EmotionPreset) -> Unit,
    onEnhanceWithAi: () -> Unit,
    onApplyRecommendations: (EmotionDirectorResult) -> Unit,
    modifier: Modifier = Modifier
) {
    val isTamil = currentLanguage == AppLanguage.TAMIL
    val presets = if (isTamil) EmotionPresets.TAMIL_PRESETS else EmotionPresets.ENGLISH_PRESETS

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("emotion_director_card"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f)
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Title Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = "Emotion Director",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = if (isTamil) "உணர்ச்சி & குரல் வழிகாட்டுதல் (Emotion Director)" else "Emotion & Directorial Comments",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (isTamil) "குரல் ஏற்ற இறக்கம், உணர்ச்சி மற்றும் அழுத்தத்திற்கான குறிப்புகள்" else "Guide voice intensity, feelings, and phrasing with commentary",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Emotion Presets
            Text(
                text = if (isTamil) "முன்மாதிரி உணர்ச்சிகள் (Emotion Presets)" else "Directorial Emotion Presets",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                presets.forEach { preset ->
                    FilterChip(
                        selected = emotionComment == preset.commentPrompt,
                        onClick = { onApplyPreset(preset) },
                        label = {
                            Text("${preset.emoji} ${preset.title}")
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        modifier = Modifier.testTag("emotion_preset_${preset.id}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Comment Input
            OutlinedTextField(
                value = emotionComment,
                onValueChange = onCommentChange,
                label = { Text(if (isTamil) "குரலுக்கான உணர்ச்சிக் குறிப்புகள் (Comments)" else "Directorial Comments for Voice Generator") },
                placeholder = { Text(if (isTamil) "எ.கா. திரைப்பட மாஸ் ட்ரைலர் போன்று, கம்பீரமான மற்றும் கணத்த குரலில் பேசவும்..." else "e.g. Speak with trembling awe, pausing before key revelations...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("emotion_comment_input"),
                minLines = 2,
                maxLines = 4,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // AI Emotion Enhancer Button
            Button(
                onClick = onEnhanceWithAi,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("enhance_emotion_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                ),
                enabled = directorState !is EmotionDirectorState.Loading && emotionComment.isNotBlank()
            ) {
                if (directorState is EmotionDirectorState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isTamil) "AI இயக்குநர் ஆய்வு செய்கிறார்..." else "AI Director Analyzing Script & Emotion...")
                } else {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (isTamil) "AI இயக்குநர் மூலம் உணர்ச்சியை மேம்படுத்து" else "Enhance Emotion via AI Director",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Results / Feedback
            AnimatedVisibility(
                visible = directorState is EmotionDirectorState.Success,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                if (directorState is EmotionDirectorState.Success) {
                    val result = directorState.result
                    DirectorResultCard(
                        result = result,
                        isTamil = isTamil,
                        onApply = { onApplyRecommendations(result) }
                    )
                }
            }

            if (directorState is EmotionDirectorState.Error) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Error: ${directorState.message}",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun DirectorResultCard(
    result: EmotionDirectorResult,
    isTamil: Boolean,
    onApply: () -> Unit
) {
    val gradientBorder = Brush.horizontalGradient(
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.tertiary
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .border(1.5.dp, gradientBorder, RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
            .padding(14.dp)
            .testTag("director_result_card")
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isTamil) "இயக்குநரின் குரல் பரிந்துரை" else "Director's Vocal Cut",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Button(
                    onClick = onApply,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("apply_director_recs_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isTamil) "பயன்படுத்து" else "Apply to Studio", style = MaterialTheme.typography.labelSmall)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "\"${result.directorsNote}\"",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Recommended parameters row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ParamBadge(
                    label = if (isTamil) "சுருதி (Pitch)" else "Pitch",
                    value = "${String.format(Locale.US, "%.2f", result.recommendedPitch)}x"
                )
                ParamBadge(
                    label = if (isTamil) "வேகம் (Speed)" else "Speed",
                    value = "${String.format(Locale.US, "%.2f", result.recommendedSpeed)}x"
                )
                ParamBadge(
                    label = if (isTamil) "அதிர்வு (Modulation)" else "Modulation",
                    value = if (isTamil) result.recommendedModulation.tamilName else result.recommendedModulation.displayName
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = if (isTamil) "மேம்படுத்தப்பட்ட உரை முன்னோட்டம்:" else "Enhanced Script Preview:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = result.enhancedScript,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun ParamBadge(label: String, value: String) {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(6.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}
