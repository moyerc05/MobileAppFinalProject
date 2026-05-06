package edu.moravian.csci395.flashfocus
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class ExtendedColorScheme(
    val customColor1: ColorFamily,
)

private val lightScheme =
    lightColorScheme(
        primary = primaryLight,
        onPrimary = onPrimaryLight,
        primaryContainer = primaryContainerLight,
        onPrimaryContainer = onPrimaryContainerLight,
        secondary = secondaryLight,
        onSecondary = onSecondaryLight,
        secondaryContainer = secondaryContainerLight,
        onSecondaryContainer = onSecondaryContainerLight,
        tertiary = tertiaryLight,
        onTertiary = onTertiaryLight,
        tertiaryContainer = tertiaryContainerLight,
        onTertiaryContainer = onTertiaryContainerLight,
        error = errorLight,
        onError = onErrorLight,
        errorContainer = errorContainerLight,
        onErrorContainer = onErrorContainerLight,
        background = backgroundLight,
        onBackground = onBackgroundLight,
        surface = surfaceLight,
        onSurface = onSurfaceLight,
        surfaceVariant = surfaceVariantLight,
        onSurfaceVariant = onSurfaceVariantLight,
        outline = outlineLight,
        outlineVariant = outlineVariantLight,
        scrim = scrimLight,
        inverseSurface = inverseSurfaceLight,
        inverseOnSurface = inverseOnSurfaceLight,
        inversePrimary = inversePrimaryLight,
        surfaceDim = surfaceDimLight,
        surfaceBright = surfaceBrightLight,
        surfaceContainerLowest = surfaceContainerLowestLight,
        surfaceContainerLow = surfaceContainerLowLight,
        surfaceContainer = surfaceContainerLight,
        surfaceContainerHigh = surfaceContainerHighLight,
        surfaceContainerHighest = surfaceContainerHighestLight,
    )

private val darkScheme =
    darkColorScheme(
        primary = primaryDark,
        onPrimary = onPrimaryDark,
        primaryContainer = primaryContainerDark,
        onPrimaryContainer = onPrimaryContainerDark,
        secondary = secondaryDark,
        onSecondary = onSecondaryDark,
        secondaryContainer = secondaryContainerDark,
        onSecondaryContainer = onSecondaryContainerDark,
        tertiary = tertiaryDark,
        onTertiary = onTertiaryDark,
        tertiaryContainer = tertiaryContainerDark,
        onTertiaryContainer = onTertiaryContainerDark,
        error = errorDark,
        onError = onErrorDark,
        errorContainer = errorContainerDark,
        onErrorContainer = onErrorContainerDark,
        background = backgroundDark,
        onBackground = onBackgroundDark,
        surface = surfaceDark,
        onSurface = onSurfaceDark,
        surfaceVariant = surfaceVariantDark,
        onSurfaceVariant = onSurfaceVariantDark,
        outline = outlineDark,
        outlineVariant = outlineVariantDark,
        scrim = scrimDark,
        inverseSurface = inverseSurfaceDark,
        inverseOnSurface = inverseOnSurfaceDark,
        inversePrimary = inversePrimaryDark,
        surfaceDim = surfaceDimDark,
        surfaceBright = surfaceBrightDark,
        surfaceContainerLowest = surfaceContainerLowestDark,
        surfaceContainerLow = surfaceContainerLowDark,
        surfaceContainer = surfaceContainerDark,
        surfaceContainerHigh = surfaceContainerHighDark,
        surfaceContainerHighest = surfaceContainerHighestDark,
    )

private val mediumContrastLightColorScheme =
    lightColorScheme(
        primary = primaryLightMediumContrast,
        onPrimary = onPrimaryLightMediumContrast,
        primaryContainer = primaryContainerLightMediumContrast,
        onPrimaryContainer = onPrimaryContainerLightMediumContrast,
        secondary = secondaryLightMediumContrast,
        onSecondary = onSecondaryLightMediumContrast,
        secondaryContainer = secondaryContainerLightMediumContrast,
        onSecondaryContainer = onSecondaryContainerLightMediumContrast,
        tertiary = tertiaryLightMediumContrast,
        onTertiary = onTertiaryLightMediumContrast,
        tertiaryContainer = tertiaryContainerLightMediumContrast,
        onTertiaryContainer = onTertiaryContainerLightMediumContrast,
        error = errorLightMediumContrast,
        onError = onErrorLightMediumContrast,
        errorContainer = errorContainerLightMediumContrast,
        onErrorContainer = onErrorContainerLightMediumContrast,
        background = backgroundLightMediumContrast,
        onBackground = onBackgroundLightMediumContrast,
        surface = surfaceLightMediumContrast,
        onSurface = onSurfaceLightMediumContrast,
        surfaceVariant = surfaceVariantLightMediumContrast,
        onSurfaceVariant = onSurfaceVariantLightMediumContrast,
        outline = outlineLightMediumContrast,
        outlineVariant = outlineVariantLightMediumContrast,
        scrim = scrimLightMediumContrast,
        inverseSurface = inverseSurfaceLightMediumContrast,
        inverseOnSurface = inverseOnSurfaceLightMediumContrast,
        inversePrimary = inversePrimaryLightMediumContrast,
        surfaceDim = surfaceDimLightMediumContrast,
        surfaceBright = surfaceBrightLightMediumContrast,
        surfaceContainerLowest = surfaceContainerLowestLightMediumContrast,
        surfaceContainerLow = surfaceContainerLowLightMediumContrast,
        surfaceContainer = surfaceContainerLightMediumContrast,
        surfaceContainerHigh = surfaceContainerHighLightMediumContrast,
        surfaceContainerHighest = surfaceContainerHighestLightMediumContrast,
    )

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color,
)

@Suppress("ktlint:standard:function-naming")
@Composable
fun FlashFocusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) darkScheme else lightScheme

    MaterialTheme(
        colorScheme = colorScheme,
        // typography = AppTypography, // Add this if you have defined typography
        content = content,
    )
}
