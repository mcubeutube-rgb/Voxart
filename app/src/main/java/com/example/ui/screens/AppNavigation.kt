package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Slideshow
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.AppLanguage
import com.example.viewmodel.StudioViewModel

enum class StudioDestination(val titleEn: String, val titleTa: String, val testTag: String) {
    VOICE_STUDIO("Voice Studio", "குரல் கூடம்", "nav_voice_studio"),
    PICTURE_STUDIO("Picture Studio", "சித்திரக் கூடம்", "nav_picture_studio"),
    SHOWCASE("Showcase", "காட்சி அரங்கம்", "nav_showcase")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    viewModel: StudioViewModel = viewModel()
) {
    var currentDestination by rememberSaveable { mutableStateOf(StudioDestination.VOICE_STUDIO) }
    val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()
    var showLanguageMenu by remember { mutableStateOf(false) }

    // Android back navigation handler
    BackHandler(enabled = currentDestination != StudioDestination.VOICE_STUDIO) {
        currentDestination = StudioDestination.VOICE_STUDIO
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (currentLanguage == AppLanguage.TAMIL) "வாக்ஸ்ஆர்ட் (VoxArt)" else "VoxArt Studio",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                },
                actions = {
                    // Language Switcher Pill
                    Box {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .clickable { showLanguageMenu = true }
                                .testTag("language_switcher_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Language,
                                    contentDescription = "Switch Language",
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = currentLanguage.nativeName,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = showLanguageMenu,
                            onDismissRequest = { showLanguageMenu = false }
                        ) {
                            AppLanguage.values().forEach { lang ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = "${lang.displayName} (${lang.nativeName})",
                                            fontWeight = if (lang == currentLanguage) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    onClick = {
                                        viewModel.setLanguage(lang)
                                        showLanguageMenu = false
                                    },
                                    modifier = Modifier.testTag("lang_option_${lang.code}")
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.testTag("top_app_bar")
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                NavigationBarItem(
                    selected = currentDestination == StudioDestination.VOICE_STUDIO,
                    onClick = { currentDestination = StudioDestination.VOICE_STUDIO },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.RecordVoiceOver,
                            contentDescription = "Voice Studio"
                        )
                    },
                    label = {
                        Text(if (currentLanguage == AppLanguage.TAMIL) StudioDestination.VOICE_STUDIO.titleTa else StudioDestination.VOICE_STUDIO.titleEn)
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    modifier = Modifier.testTag(StudioDestination.VOICE_STUDIO.testTag)
                )

                NavigationBarItem(
                    selected = currentDestination == StudioDestination.PICTURE_STUDIO,
                    onClick = { currentDestination = StudioDestination.PICTURE_STUDIO },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "Picture Studio"
                        )
                    },
                    label = {
                        Text(if (currentLanguage == AppLanguage.TAMIL) StudioDestination.PICTURE_STUDIO.titleTa else StudioDestination.PICTURE_STUDIO.titleEn)
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                        selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    modifier = Modifier.testTag(StudioDestination.PICTURE_STUDIO.testTag)
                )

                NavigationBarItem(
                    selected = currentDestination == StudioDestination.SHOWCASE,
                    onClick = { currentDestination = StudioDestination.SHOWCASE },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Slideshow,
                            contentDescription = "Showcase"
                        )
                    },
                    label = {
                        Text(if (currentLanguage == AppLanguage.TAMIL) StudioDestination.SHOWCASE.titleTa else StudioDestination.SHOWCASE.titleEn)
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.tertiaryContainer,
                        selectedIconColor = MaterialTheme.colorScheme.onTertiaryContainer
                    ),
                    modifier = Modifier.testTag(StudioDestination.SHOWCASE.testTag)
                )
            }
        }
    ) { innerPadding ->
        AnimatedContent(
            targetState = currentDestination,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "screen_transition",
            modifier = Modifier.padding(innerPadding)
        ) { destination ->
            when (destination) {
                StudioDestination.VOICE_STUDIO -> {
                    VoiceStudioScreen(viewModel = viewModel)
                }
                StudioDestination.PICTURE_STUDIO -> {
                    PictureStudioScreen(
                        viewModel = viewModel,
                        onNavigateToShowcase = {
                            currentDestination = StudioDestination.SHOWCASE
                        }
                    )
                }
                StudioDestination.SHOWCASE -> {
                    ShowcaseScreen(viewModel = viewModel)
                }
            }
        }
    }
}
