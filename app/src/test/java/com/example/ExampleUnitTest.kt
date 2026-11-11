package com.example

import com.example.model.AppLanguage
import com.example.model.EmotionPresets
import com.example.model.ImageStylePresets
import com.example.model.ModulationType
import com.example.model.StudioAspectRatio
import com.example.model.VoicePersonas
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun verifyVoicePersonas() {
        assertTrue(VoicePersonas.ALL.size >= 12)
        val titan = VoicePersonas.ALL.find { it.id == "titan" }
        assertNotNull(titan)
        assertEquals(ModulationType.CINEMATIC_ECHO, titan?.defaultModulation)
        assertTrue(titan!!.basePitch < 1.0f)

        // Verify Tamil personas
        val maran = VoicePersonas.TAMIL_PERSONAS.find { it.id == "maran_ta" }
        assertNotNull(maran)
        assertEquals(AppLanguage.TAMIL, maran?.language)
        assertTrue(maran!!.name.contains("மாறன்"))

        val yazhini = VoicePersonas.TAMIL_PERSONAS.find { it.id == "yazhini_ta" }
        assertNotNull(yazhini)
        assertEquals(AppLanguage.TAMIL, yazhini?.language)
    }

    @Test
    fun verifyImageStylePresets() {
        assertTrue(ImageStylePresets.ALL.size >= 10)
        val thanjavur = ImageStylePresets.ALL.find { it.id == "thanjavur_gold" }
        assertNotNull(thanjavur)
        assertTrue(thanjavur!!.tamilName.contains("தஞ்சாவூர்"))
        assertTrue(thanjavur.isTamilSpecific)

        val chola = ImageStylePresets.ALL.find { it.id == "chola_bronze" }
        assertNotNull(chola)
        assertTrue(chola!!.tamilName.contains("சோழர்"))
    }

    @Test
    fun verifyEmotionPresets() {
        assertTrue(EmotionPresets.ALL.isNotEmpty())
        assertTrue(EmotionPresets.TAMIL_PRESETS.isNotEmpty())

        val tamilEpic = EmotionPresets.TAMIL_PRESETS.find { it.id == "dramatic_epic_ta" }
        assertNotNull(tamilEpic)
        assertEquals(AppLanguage.TAMIL, tamilEpic?.language)
        assertEquals(ModulationType.CINEMATIC_ECHO, tamilEpic?.modulation)
    }

    @Test
    fun verifyAspectRatios() {
        assertEquals(1.0f, StudioAspectRatio.SQUARE_1_1.ratioFloat, 0.01f)
        assertTrue(StudioAspectRatio.LANDSCAPE_16_9.ratioFloat > 1.0f)
        assertTrue(StudioAspectRatio.PORTRAIT_9_16.ratioFloat < 1.0f)
    }

    @Test
    fun verifyAppLanguages() {
        assertEquals("ta", AppLanguage.TAMIL.code)
        assertEquals("தமிழ்", AppLanguage.TAMIL.nativeName)
        assertEquals("en", AppLanguage.ENGLISH.code)
    }
}
