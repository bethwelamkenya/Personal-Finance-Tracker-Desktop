package ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

enum class AppColorScheme(
    val lightColorsProvider: @Composable () -> ColorScheme,
    val darkColorsProvider: @Composable () -> ColorScheme,
    val displayName: String
) {
    DEFAULT_BLUE({ LightThemeColors }, { DarkThemeColors }, "Default Blue"),
    EARTHLY_COLOR({ EarthlyLightColors }, { EarthlyDarkColors }, "Earthly Color"),
    EMERALD_COURT({ EmeraldCourtLightColors }, { EmeraldCourtDarkColors }, "Emerald Court"),
    ROSE_BLOSSOM({ RoseBlossomLightColors }, { RoseBlossomDarkColors }, "Rose Blossom"),
    OCEAN_MIST({ OceanMistLightColors }, { OceanMistDarkColors }, "Ocean Mist"),
    AUTUMN_EMBER({ AutumnEmberLightColors }, { AutumnEmberDarkColors }, "Autumn Ember"),
    BLACK_WHITE({ BlackWhiteLightColors }, { BlackWhiteDarkColors }, "Black & White"),;

    @Composable
    fun getColors(isDark: Boolean): ColorScheme {
        return if (isDark) darkColorsProvider() else lightColorsProvider()
    }

    companion object {
        fun fromString(value: String): AppColorScheme {
            return entries.find { it.displayName == value } ?: DEFAULT_BLUE
        }
    }
}

// Change from:
// class AppColorTheme(val value: String) { ... }

// To:
enum class AppColorTheme(val value: String) {
    LIGHT("Light"),
    DARK("Dark"),
    SYSTEM("System");

    companion object {
        fun fromString(value: String): AppColorTheme {
            return entries.find { it.value == value } ?: SYSTEM
        }
    }
}

