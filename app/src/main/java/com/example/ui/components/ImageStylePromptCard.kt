package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.AppLanguage
import com.example.model.ImageStylePreset
import com.example.model.ImageStylePresets
import com.example.model.StudioAspectRatio
import com.example.viewmodel.ImageGenState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ImageStylePromptCard(
    imagePrompt: String,
    customStylePrompt: String,
    selectedPreset: ImageStylePreset?,
    selectedAspectRatio: StudioAspectRatio,
    imageGenState: ImageGenState,
    currentLanguage: AppLanguage,
    onImagePromptChange: (String) -> Unit,
    onCustomStylePromptChange: (String) -> Unit,
    onPresetSelected: (ImageStylePreset?) -> Unit,
    onAspectRatioSelected: (StudioAspectRatio) -> Unit,
    onSyncWithVoiceScript: () -> Unit,
    onGeneratePicture: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isTamil = currentLanguage == AppLanguage.TAMIL

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("image_style_prompt_card"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f)
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "Picture Studio",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = if (isTamil) "சித்திரக் கூடம் & கலை பாணி" else "Picture Generator & Style",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (isTamil) "எளிய உரை மூலம் கலை பாணிகளை உருவாக்குங்கள்" else "Customize visual styles using simple text prompts",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                OutlinedButton(
                    onClick = onSyncWithVoiceScript,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("sync_script_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Sync,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        if (isTamil) "உரையை நகலெடு" else "Copy Voice Text",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Text Prompt input
            OutlinedTextField(
                value = imagePrompt,
                onValueChange = onImagePromptChange,
                label = { Text(if (isTamil) "காட்சிப் பொருள் / உரை (Subject Text)" else "What to Picture / Subject Description") },
                placeholder = { Text(if (isTamil) "வரைய வேண்டிய காட்சியை தமிழில் அல்லது ஆங்கிலத்தில் உள்ளிடவும்..." else "Enter prompt or story scene to visualize...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("image_prompt_input"),
                minLines = 2,
                maxLines = 4,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Style Customization Prompt Input
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Brush,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isTamil) "கலை பாணி தனிப்பயனாக்கம் (Style Customization Prompt)" else "Style Customization (Simple Text Prompt)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = customStylePrompt,
                onValueChange = onCustomStylePromptChange,
                label = { Text(if (isTamil) "விருப்பமான கலை பாணி உரைக்குறிப்பு" else "Custom Visual Style Prompt") },
                placeholder = { Text(if (isTamil) "எ.கா. தஞ்சாவூர் ஓவியம், சோழர் வெண்கலச் சிற்பம், அனிமே, 35மிமீ படம்..." else "e.g. Studio Ghibli anime watercolor, Cyberpunk neon wet reflections...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("custom_style_prompt_input"),
                minLines = 2,
                maxLines = 3,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.secondary
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Style Preset Chips
            Text(
                text = if (isTamil) "முன்மாதிரி கலை பாணிகள் (Artistic Presets)" else "Preset Artistic Styles",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ImageStylePresets.ALL.forEach { preset ->
                    val isSelected = selectedPreset?.id == preset.id
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            if (isSelected) onPresetSelected(null) else onPresetSelected(preset)
                        },
                        label = {
                            Text("${preset.iconEmoji} ${if (isTamil) preset.tamilName else preset.name}")
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        modifier = Modifier.testTag("style_preset_${preset.id}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Aspect Ratio Selector
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AspectRatio,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isTamil) "படத்தின் விகிதம் (Aspect Ratio)" else "Canvas Aspect Ratio",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(6.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                StudioAspectRatio.values().forEach { ratio ->
                    val isSelected = ratio == selectedAspectRatio
                    FilterChip(
                        selected = isSelected,
                        onClick = { onAspectRatioSelected(ratio) },
                        label = { Text(ratio.label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onTertiaryContainer
                        ),
                        modifier = Modifier.testTag("aspect_ratio_${ratio.name.lowercase()}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Generate Button
            val isLoading = imageGenState is ImageGenState.Loading
            Button(
                onClick = onGeneratePicture,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("generate_picture_button"),
                enabled = !isLoading && imagePrompt.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        if (isTamil) "சித்திரம் உருவாக்கப்படுகிறது (30-60 வினாடிகள்)..." else "Synthesizing Image with Gemini (30-60s)..."
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (isTamil) "வடிவமைக்கப்பட்ட சித்திரத்தை உருவாக்கு" else "Generate Styled Picture",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
