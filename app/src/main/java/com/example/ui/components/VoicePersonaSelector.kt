package com.example.ui.components

import android.speech.tts.Voice
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.SettingsVoice
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.AppLanguage
import com.example.model.VoicePersona
import com.example.model.VoicePersonas

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoicePersonaSelector(
    selectedPersona: VoicePersona,
    currentLanguage: AppLanguage,
    availableSystemVoices: List<Voice>,
    selectedSystemVoice: Voice?,
    onPersonaSelected: (VoicePersona) -> Unit,
    onSystemVoiceSelected: (Voice?) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAllLanguages by remember { mutableStateOf(false) }

    val displayedPersonas = remember(currentLanguage, showAllLanguages) {
        if (showAllLanguages) {
            VoicePersonas.ALL
        } else {
            when (currentLanguage) {
                AppLanguage.TAMIL -> VoicePersonas.TAMIL_PERSONAS
                AppLanguage.ENGLISH -> VoicePersonas.ENGLISH_PERSONAS
            }
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("voice_persona_selector_card"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f)
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.RecordVoiceOver,
                        contentDescription = "Voice Personas",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = if (currentLanguage == AppLanguage.TAMIL) "குரல் பாத்திரங்கள் (Voice Personas)" else "Voice Personas & Timbre",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (currentLanguage == AppLanguage.TAMIL) "தமிழ் மற்றும் உலகளாவிய குரல்கள்" else "Select from handcrafted voices or device engines",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                FilterChip(
                    selected = showAllLanguages,
                    onClick = { showAllLanguages = !showAllLanguages },
                    label = {
                        Text(
                            if (showAllLanguages) "All" else currentLanguage.nativeName,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag("toggle_all_voices_button")
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Personas Horizontal Carousel
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 2.dp)
            ) {
                items(displayedPersonas, key = { it.id }) { persona ->
                    val isSelected = persona.id == selectedPersona.id
                    PersonaChipItem(
                        persona = persona,
                        isSelected = isSelected,
                        onClick = { onPersonaSelected(persona) }
                    )
                }
            }

            // Device Voice Engine Picker
            if (availableSystemVoices.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))
                SystemVoiceDropdown(
                    availableVoices = availableSystemVoices,
                    selectedVoice = selectedSystemVoice,
                    currentLanguage = currentLanguage,
                    onVoiceSelected = onSystemVoiceSelected
                )
            }
        }
    }
}

@Composable
private fun PersonaChipItem(
    persona: VoicePersona,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val accentColor = Color(persona.tagColorHex)
    val borderColor = if (isSelected) accentColor else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    val bgColor = if (isSelected) accentColor.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface

    Box(
        modifier = Modifier
            .width(175.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .border(if (isSelected) 2.dp else 1.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(12.dp)
            .testTag("persona_item_${persona.id}")
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(accentColor.copy(alpha = 0.25f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = accentColor.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = persona.gender,
                        style = MaterialTheme.typography.labelSmall,
                        color = accentColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = persona.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = persona.title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = persona.description,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                maxLines = 2
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SystemVoiceDropdown(
    availableVoices: List<Voice>,
    selectedVoice: Voice?,
    currentLanguage: AppLanguage,
    onVoiceSelected: (Voice?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selectedVoice?.name?.substringAfterLast("_") ?: if (currentLanguage == AppLanguage.TAMIL) "இயல்புநிலை குரல் (Default Engine)" else "Default Engine Voice",
            onValueChange = {},
            readOnly = true,
            label = {
                Text(if (currentLanguage == AppLanguage.TAMIL) "சாதனத்தின் குரல் இயந்திரம் (Device Voices)" else "Device Voice Engine (Optional)")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.SettingsVoice,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .testTag("system_voice_selector"),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Default Engine Voice") },
                onClick = {
                    onVoiceSelected(null)
                    expanded = false
                }
            )
            availableVoices.take(15).forEach { voice ->
                val isTamilVoice = voice.locale.language.equals("ta", ignoreCase = true)
                DropdownMenuItem(
                    text = {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(voice.name, fontWeight = FontWeight.Medium)
                                if (isTamilVoice) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            "தமிழ்",
                                            style = MaterialTheme.typography.labelSmall,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                            }
                            Text(
                                "Locale: ${voice.locale.displayName}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    onClick = {
                        onVoiceSelected(voice)
                        expanded = false
                    }
                )
            }
        }
    }
}
