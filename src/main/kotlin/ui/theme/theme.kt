package ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val LocalDimens = staticCompositionLocalOf { DefaultDimens }

@Composable
fun AppTheme(
    uiTheme: AppColorTheme,
    compactLayout: Boolean = false, // Add this parameter
    colorScheme: AppColorScheme,
    content: @Composable () -> Unit
) {
    val isDarkTheme: Boolean = isSystemInDarkTheme()
    val darkTheme = uiTheme == AppColorTheme.DARK || (uiTheme == AppColorTheme.SYSTEM && isDarkTheme)
    val colors = colorScheme.getColors(darkTheme)
    val typography = if (compactLayout) CompactTypography else Typography()
    val dimensions = if (compactLayout) CompactDimens else DefaultDimens
    val shapes = if (compactLayout) CompactShapes else DefaultShapes

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        shapes = shapes
    ) {
        CompositionLocalProvider(
            LocalDimens provides dimensions,
            content = content
        )
    }
}

// In your Theme.kt file

// 1. Define the Dimens data class
data class Dimens(
    // Spacing
    val noPadding: Dp,
    val smallPadding: Dp,
    val mediumPadding: Dp,
    val largePadding: Dp,
    val extraLargePadding: Dp,

    // Component Sizes
    val textFieldHeight: Dp,
    val buttonHeight: Dp,
    val iconSize: Dp,
    val borderWidth: Dp,
    val dividerThickness: Dp,

    // Sidebar
    val sideBarWidth: Dp,
    val sideBarItemHeight: Dp,
    val sidebarHeaderHeight: Dp,
    val sidebarFooterHeight: Dp,

    // Avatars & Images
    val avatarSizeSmall: Dp,
    val avatarSizeMedium: Dp,
    val avatarSizeLarge: Dp,

    // Shapes
    val cornerRadiusSmall: Dp,
    val cornerRadiusMedium: Dp,
    val cornerRadiusLarge: Dp,

    // Elevation
    val elevationLow: Dp,
    val elevationMedium: Dp,
    val elevationHigh: Dp,

    // Responsive Breakpoints
    val breakpointMobile: Dp,
    val breakpointTablet: Dp,
    val breakpointDesktop: Dp
)

val CompactDimens = Dimens(
    noPadding = 0.dp,
    smallPadding = 4.dp,
    mediumPadding = 8.dp,
    largePadding = 12.dp,
    extraLargePadding = 16.dp,

    textFieldHeight = 40.dp,
    buttonHeight = 40.dp,
    iconSize = 24.dp,
    borderWidth = 1.dp,
    dividerThickness = 0.5.dp,

    sideBarWidth = 200.dp,
    sideBarItemHeight = 48.dp,
    sidebarHeaderHeight = 48.dp,
    sidebarFooterHeight = 56.dp,

    avatarSizeSmall = 32.dp,
    avatarSizeMedium = 40.dp,
    avatarSizeLarge = 56.dp,

    cornerRadiusSmall = 4.dp,
    cornerRadiusMedium = 8.dp,
    cornerRadiusLarge = 12.dp,

    elevationLow = 2.dp,
    elevationMedium = 4.dp,
    elevationHigh = 8.dp,

    breakpointMobile = 600.dp,
    breakpointTablet = 900.dp,
    breakpointDesktop = 1200.dp
)

val DefaultDimens = Dimens(
    noPadding = 0.dp,
    smallPadding = 8.dp,
    mediumPadding = 16.dp,
    largePadding = 24.dp,
    extraLargePadding = 32.dp,

    textFieldHeight = 60.dp,
    buttonHeight = 56.dp,
    iconSize = 40.dp,
    borderWidth = 2.dp,
    dividerThickness = 1.dp,

    sideBarWidth = 240.dp,
    sideBarItemHeight = 64.dp,
    sidebarHeaderHeight = 64.dp,
    sidebarFooterHeight = 72.dp,

    avatarSizeSmall = 40.dp,
    avatarSizeMedium = 56.dp,
    avatarSizeLarge = 72.dp,

    cornerRadiusSmall = 8.dp,
    cornerRadiusMedium = 16.dp,
    cornerRadiusLarge = 24.dp,

    elevationLow = 4.dp,
    elevationMedium = 8.dp,
    elevationHigh = 16.dp,

    breakpointMobile = 768.dp,
    breakpointTablet = 1024.dp,
    breakpointDesktop = 1440.dp
)

val CompactTypography = Typography(
    displayLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 48.sp,
        lineHeight = 52.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 40.sp,
        lineHeight = 44.sp
    ),
    displaySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 36.sp
    ),

    headlineLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 32.sp
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 28.sp
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 24.sp
    ),

    titleLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 22.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp
    ),
    titleSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 18.sp
    ),

    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 18.sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    bodySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 14.sp
    ),

    labelLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 14.sp
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 12.sp
    )
)

val CompactShapes = Shapes(
    extraSmall = RoundedCornerShape(CompactDimens.cornerRadiusSmall),
    small = RoundedCornerShape(CompactDimens.cornerRadiusMedium),
    medium = RoundedCornerShape(CompactDimens.cornerRadiusMedium),
    large = RoundedCornerShape(CompactDimens.cornerRadiusLarge),
    extraLarge = RoundedCornerShape(CompactDimens.cornerRadiusLarge)
)

val DefaultShapes = Shapes(
    extraSmall = RoundedCornerShape(DefaultDimens.cornerRadiusSmall),
    small = RoundedCornerShape(DefaultDimens.cornerRadiusMedium),
    medium = RoundedCornerShape(DefaultDimens.cornerRadiusMedium),
    large = RoundedCornerShape(DefaultDimens.cornerRadiusLarge),
    extraLarge = RoundedCornerShape(DefaultDimens.cornerRadiusLarge)
)

// Optional: Add component-specific shape extensions
val Shapes.textField: Shape
    get() = RoundedCornerShape(CompactDimens.cornerRadiusSmall)

val Shapes.button: Shape
    get() = RoundedCornerShape(CompactDimens.cornerRadiusMedium)

val Shapes.card: Shape
    get() = RoundedCornerShape(CompactDimens.cornerRadiusLarge)

val Shapes.avatar: Shape
    get() = CircleShape