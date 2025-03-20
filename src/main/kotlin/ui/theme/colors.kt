package ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.lightColors
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import java.io.BufferedReader
import java.io.InputStreamReader
import java.awt.Color as AwtColor
import javax.swing.UIManager

//🌿 Emerald Court
val EmeraldCourtLightColors = lightColorScheme(
    primary = Color(0xFF4CAF50),       // Lush emerald green
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFC8E6C9), // Mint green
    onPrimaryContainer = Color(0xFF1B5E20),

    secondary = Color(0xFF388E3C),     // Deep forest green
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFA5D6A7),
    onSecondaryContainer = Color(0xFF2E7D32),

    tertiary = Color(0xFF81C784),      // Soft moss
    onTertiary = Color(0xFF1B5E20),
    tertiaryContainer = Color(0xFFDCEDC8),
    onTertiaryContainer = Color(0xFF388E3C),

    background = Color(0xFFF1F8E9),    // Gentle spring
    onBackground = Color(0xFF2E7D32),

    surface = Color(0xFFF9FBE7),       // Morning dew
    onSurface = Color(0xFF1B5E20),

    surfaceVariant = Color(0xFFC5E1A5), // Meadow grass
    onSurfaceVariant = Color(0xFF33691E),

    error = Color(0xFFB71C1C),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFCDD2),
    onErrorContainer = Color(0xFFB71C1C),

    outline = Color(0xFF66BB6A)        // Vibrant leaf
)

val EmeraldCourtDarkColors = darkColorScheme(
    primary = Color(0xFF81C784),       // Muted emerald glow
    onPrimary = Color(0xFF1B5E20),
    primaryContainer = Color(0xFF388E3C), // Forest shade
    onPrimaryContainer = Color(0xFFA5D6A7),

    secondary = Color(0xFF66BB6A),     // Twilight leaf
    onSecondary = Color(0xFF004D40),
    secondaryContainer = Color(0xFF1B5E20),
    onSecondaryContainer = Color(0xFFA5D6A7),

    tertiary = Color(0xFF2E7D32),      // Deep jungle
    onTertiary = Color(0xFFDCEDC8),
    tertiaryContainer = Color(0xFF4CAF50),
    onTertiaryContainer = Color(0xFFC8E6C9),

    background = Color(0xFF1B1F1A),    // Midnight grove
    onBackground = Color(0xFFD7E8D4),

    surface = Color(0xFF263238),       // Ancient moss
    onSurface = Color(0xFFC8E6C9),

    surfaceVariant = Color(0xFF37474F), // Verdant dusk
    onSurfaceVariant = Color(0xFFA5D6A7),

    error = Color(0xFFFF6F61),
    onError = Color(0xFFB71C1C),
    errorContainer = Color(0xFFD32F2F),
    onErrorContainer = Color(0xFFFFCDD2),

    outline = Color(0xFF66BB6A)        // Soft meadow edge
)

//🌸 Rose Blossom
val RoseBlossomLightColors = lightColorScheme(
    primary = Color(0xFFD08181),       // Soft Rosewood
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFF4C2C2), // Light Blush
    onPrimaryContainer = Color(0xFF4D2A2A),

    secondary = Color(0xFFB9808D),     // Muted Dusty Rose
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE8B6BF),
    onSecondaryContainer = Color(0xFF3D1E24),

    tertiary = Color(0xFFC896A6),      // Warm Mauve
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFF3D3DA),
    onTertiaryContainer = Color(0xFF40282D),

    background = Color(0xFFFAF0F3),    // Soft Petal White
    onBackground = Color(0xFF312426),

    surface = Color(0xFFFFFBFC),       // Gentle Ivory
    onSurface = Color(0xFF312426),

    surfaceVariant = Color(0xFFF0D9DE), // Soft Velvet Pink
    onSurfaceVariant = Color(0xFF5C4346),

    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    outline = Color(0xFF96737A)        // Aged Rosewood
)

val RoseBlossomDarkColors = darkColorScheme(
    primary = Color(0xFFE2A5A5),       // Soft Dusty Pink
    onPrimary = Color(0xFF4A2727),
    primaryContainer = Color(0xFF7A4848), // Deep Mauve
    onPrimaryContainer = Color(0xFFFFD8D8),

    secondary = Color(0xFFC7919D),     // Faded Blush
    onSecondary = Color(0xFF3A2226),
    secondaryContainer = Color(0xFF5D3C42),
    onSecondaryContainer = Color(0xFFE6BFC5),

    tertiary = Color(0xFFD3A7B5),      // Warm Rosé
    onTertiary = Color(0xFF472C32),
    tertiaryContainer = Color(0xFF6B454E),
    onTertiaryContainer = Color(0xFFFFD9E0),

    background = Color(0xFF1C1819),    // Deep Rosewood
    onBackground = Color(0xFFE8D8DB),

    surface = Color(0xFF252021),       // Faint Shadow Mauve
    onSurface = Color(0xFFE8D8DB),

    surfaceVariant = Color(0xFF5C4346), // Dark Blush Tint
    onSurfaceVariant = Color(0xFFD7B8BE),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    outline = Color(0xFFA88A90)        // Muted Velvet Rose
)

//🌊 Ocean Mist
val OceanMistLightColors = lightColorScheme(
    primary = Color(0xFF1976D2),       // Deep ocean blue
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFBBDEFB), // Soft sky
    onPrimaryContainer = Color(0xFF0D47A1),

    secondary = Color(0xFF0288D1),     // Cool waves
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF81D4FA),
    onSecondaryContainer = Color(0xFF01579B),

    tertiary = Color(0xFF26C6DA),      // Crystal lagoon
    onTertiary = Color(0xFF004D40),
    tertiaryContainer = Color(0xFFE0F7FA),
    onTertiaryContainer = Color(0xFF00ACC1),

    background = Color(0xFFE3F2FD),    // Misty shore
    onBackground = Color(0xFF0D47A1),

    surface = Color(0xFFE1F5FE),       // Morning tide
    onSurface = Color(0xFF1976D2),

    surfaceVariant = Color(0xFFB3E5FC), // Sky reflection
    onSurfaceVariant = Color(0xFF01579B),

    error = Color(0xFFD32F2F),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFCDD2),
    onErrorContainer = Color(0xFFC62828),

    outline = Color(0xFF42A5F5)        // Ocean mist glow
)

val OceanMistDarkColors = darkColorScheme(
    primary = Color(0xFF42A5F5),       // Moonlit tide
    onPrimary = Color(0xFF0D47A1),
    primaryContainer = Color(0xFF1565C0), // Deep sea current
    onPrimaryContainer = Color(0xFFBBDEFB),

    secondary = Color(0xFF039BE5),     // Starlit wave
    onSecondary = Color(0xFFE3F2FD),
    secondaryContainer = Color(0xFF0277BD),
    onSecondaryContainer = Color(0xFF81D4FA),

    tertiary = Color(0xFF00ACC1),      // Frosty lagoon
    onTertiary = Color(0xFFE0F7FA),
    tertiaryContainer = Color(0xFF00838F),
    onTertiaryContainer = Color(0xFFB3E5FC),

    background = Color(0xFF121C27),    // Midnight ocean
    onBackground = Color(0xFFBBDEFB),

    surface = Color(0xFF0A1929),       // Abyss
    onSurface = Color(0xFFE3F2FD),

    surfaceVariant = Color(0xFF1E3A5F), // Tidal foam
    onSurfaceVariant = Color(0xFFBBDEFB),

    error = Color(0xFFD32F2F),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFCDD2),
    onErrorContainer = Color(0xFFC62828),

    outline = Color(0xFF42A5F5)        // Soft wave crest
)

//🍂 Autumn Ember
val AutumnEmberLightColors = lightColorScheme(
    primary = Color(0xFFB86F36),       // Muted Pumpkin Spice
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFF2C09F), // Toasted Maple
    onPrimaryContainer = Color(0xFF4A250F),

    secondary = Color(0xFF986C50),     // Autumn Chestnut
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFD7B4A0),
    onSecondaryContainer = Color(0xFF3E2419),

    tertiary = Color(0xFFC18450),      // Burnt Sienna
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFF3D1B2),
    onTertiaryContainer = Color(0xFF4A2F1B),

    background = Color(0xFFFAF5EE),    // Warm Vanilla
    onBackground = Color(0xFF2E1E16),

    surface = Color(0xFFFFFBF6),       // Aged Ivory
    onSurface = Color(0xFF2E1E16),

    surfaceVariant = Color(0xFFF0D7C0), // Faded Parchment
    onSurfaceVariant = Color(0xFF5D4432),

    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    outline = Color(0xFF8B6650)        // Antique Bronze
)

val AutumnEmberDarkColors = darkColorScheme(
    primary = Color(0xFFD39B6A),       // Golden Amber Glow
    onPrimary = Color(0xFF4D2B13),
    primaryContainer = Color(0xFF77492A), // Rich Burnt Orange
    onPrimaryContainer = Color(0xFFF4C9A3),

    secondary = Color(0xFFC29175),     // Dusty Harvest
    onSecondary = Color(0xFF3F2A1E),
    secondaryContainer = Color(0xFF5D3C2B),
    onSecondaryContainer = Color(0xFFE4BFA6),

    tertiary = Color(0xFFD9A678),      // Deep Autumn Oak
    onTertiary = Color(0xFF492D1B),
    tertiaryContainer = Color(0xFF6F4A2D),
    onTertiaryContainer = Color(0xFFF3D2B5),

    background = Color(0xFF1E1611),    // Smoky Ember
    onBackground = Color(0xFFE9DBCF),

    surface = Color(0xFF272018),       // Dim Candlelight
    onSurface = Color(0xFFE9DBCF),

    surfaceVariant = Color(0xFF5D4432), // Dark Sandstone
    onSurfaceVariant = Color(0xFFD7BDA9),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    outline = Color(0xFFA3846B)        // Aged Maple Wood
)

// Light Theme Colors (MD3 tonal palette)
val LightThemeColors = lightColorScheme(
    primary = Color(0xFF6750A4),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFEADDFF),
    onPrimaryContainer = Color(0xFF21005E),

    secondary = Color(0xFF625B71),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE8DEF8),
    onSecondaryContainer = Color(0xFF1D192B),

    tertiary = Color(0xFF7D5260),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFD8E4),
    onTertiaryContainer = Color(0xFF31111D),

    background = Color(0xFFFEF7FF),
    onBackground = Color(0xFF1D1B20),

    surface = Color(0xFFFEF7FF),
    onSurface = Color(0xFF1D1B20),

    surfaceVariant = Color(0xFFE7E0EC),
    onSurfaceVariant = Color(0xFF49454F),

    error = Color(0xFFB3261E),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),

    outline = Color(0xFF79747E)
)

// Dark Theme Colors
val DarkThemeColors = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    onPrimary = Color(0xFF381E72),
    primaryContainer = Color(0xFF4F378B),
    onPrimaryContainer = Color(0xFFEADDFF),

    secondary = Color(0xFFCCC2DC),
    onSecondary = Color(0xFF332D41),
    secondaryContainer = Color(0xFF4A4458),
    onSecondaryContainer = Color(0xFFE8DEF8),

    tertiary = Color(0xFFEFB8C8),
    onTertiary = Color(0xFF492532),
    tertiaryContainer = Color(0xFF633B48),
    onTertiaryContainer = Color(0xFFFFD8E4),

    background = Color(0xFF141218),
    onBackground = Color(0xFFE6E0E9),

    surface = Color(0xFF141218),
    onSurface = Color(0xFFE6E0E9),

    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0),

    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DEDC),

    outline = Color(0xFF938F99)
)

// Add to your Theme.kt file
val EarthlyLightColors = lightColorScheme(
    primary = Color(0xFF6D4C41),       // Earthy umber (commoner life)
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD7CCC8), // Pale clay
    onPrimaryContainer = Color(0xFF2A180F),

    secondary = Color(0xFF455A64),     // Slate blue (northern skies)
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFCFD8DC),
    onSecondaryContainer = Color(0xFF1E282D),

    tertiary = Color(0xFF8D6E63),      // Weathered terracotta
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFD8C9),
    onTertiaryContainer = Color(0xFF3A2924),

    background = Color(0xFFF5F0ED),    // Parchment paper
    onBackground = Color(0xFF1E1B18),

    surface = Color(0xFFFFFBF8),       // Aged ivory
    onSurface = Color(0xFF1E1B18),

    surfaceVariant = Color(0xFFEDE0D4), // Antique linen
    onSurfaceVariant = Color(0xFF52443C),

    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    outline = Color(0xFF85736C)        // Weathered wood
)

val EarthlyDarkColors = darkColorScheme(
    primary = Color(0xFFBCAAA4),       // Faded royal silver
    onPrimary = Color(0xFF442C22),
    primaryContainer = Color(0xFF5D4037), // Ancient stone
    onPrimaryContainer = Color(0xFFFFD8C9),

    secondary = Color(0xFFB2CBD5),     // Moonlit frost
    onSecondary = Color(0xFF1D333B),
    secondaryContainer = Color(0xFF344955),
    onSecondaryContainer = Color(0xFFCDE6F1),

    tertiary = Color(0xFFD4B5A8),      // Demon king's ember
    onTertiary = Color(0xFF4A3129),
    tertiaryContainer = Color(0xFF63473E),
    onTertiaryContainer = Color(0xFFFFD8C9),

    background = Color(0xFF161210),    // Castle stone
    onBackground = Color(0xFFE8E1DD),

    surface = Color(0xFF1E1B18),       // Royal archives
    onSurface = Color(0xFFE8E1DD),

    surfaceVariant = Color(0xFF52443C), // Sealed tomb
    onSurfaceVariant = Color(0xFFD7C3B8),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    outline = Color(0xFF9F8D85)        // Ancient scroll ink
)

//🖤 Black & White Color Scheme
val BlackWhiteLightColors = lightColorScheme(
    primary = Color.Black,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFBBBBBB), // Light gray
    onPrimaryContainer = Color.Black,

    secondary = Color.DarkGray,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD3D3D3), // Lighter gray
    onSecondaryContainer = Color.Black,

    tertiary = Color.Gray,
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFFE0E0E0), // Soft gray
    onTertiaryContainer = Color.Black,

    background = Color.White,
    onBackground = Color.Black,

    surface = Color.White,
    onSurface = Color.Black,

    surfaceVariant = Color(0xFFF0F0F0), // Slightly off-white
    onSurfaceVariant = Color.Black,

    error = Color.Red,
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color.Black,

    outline = Color.Gray
)

val BlackWhiteDarkColors = darkColorScheme(
    primary = Color.White,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF444444), // Dark gray
    onPrimaryContainer = Color.White,

    secondary = Color.LightGray,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF222222), // Very dark gray
    onSecondaryContainer = Color.White,

    tertiary = Color.Gray,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF333333), // Darker gray
    onTertiaryContainer = Color.White,

    background = Color.Black,
    onBackground = Color.White,

    surface = Color.Black,
    onSurface = Color.White,

    surfaceVariant = Color(0xFF121212), // Very dark gray
    onSurfaceVariant = Color.White,

    error = Color.Red,
    onError = Color.Black,
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color.White,

    outline = Color.LightGray
)