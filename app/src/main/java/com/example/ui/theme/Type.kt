package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

// Define AppFont enum containing 15 distinct, safe, non-crashing system-backed fonts
enum class AppFont(
    val displayName: String,
    val fontFamily: FontFamily,
    val letterSpacingExtra: Float = 0f,
    val isPixel: Boolean = false
) {
    DEFAULT("Default Sans-Serif", FontFamily.SansSerif),
    SERIF("Classic Serif Elegant", FontFamily.Serif),
    MONOSPACE("Solid Tech Monospace", FontFamily.Monospace),
    CURSIVE("Sweet Heart Cursive", FontFamily.Cursive),
    CASUAL("Casual Playful Sans", FontFamily(android.graphics.Typeface.create("casual", android.graphics.Typeface.NORMAL))),
    CONDENSED("Sleek Condensed Tech", FontFamily(android.graphics.Typeface.create("sans-serif-condensed", android.graphics.Typeface.NORMAL))),
    SMALL_CAPS("Vintage Typewriter", FontFamily(android.graphics.Typeface.create("serif-monospace", android.graphics.Typeface.NORMAL)), 0.05f),
    PIXEL("8-Bit Retro Pixel (Pixel)", FontFamily.Monospace, 0.16f, true), // Monospace + bold + letterSpacing + force All Caps!
    GEOMETRIC("Future Geometric Heavy", FontFamily(android.graphics.Typeface.create("sans-serif", android.graphics.Typeface.BOLD))),
    ELEGANT_THIN("Luxury Minimal Thin", FontFamily(android.graphics.Typeface.create("sans-serif-thin", android.graphics.Typeface.NORMAL))),
    LIGHT_CLEAN("Clean Minimalist Light", FontFamily(android.graphics.Typeface.create("sans-serif-light", android.graphics.Typeface.NORMAL))),
    MEDIUM("Premium Medium Slate", FontFamily(android.graphics.Typeface.create("sans-serif-medium", android.graphics.Typeface.NORMAL))),
    BLACK("Titan Hacker Black", FontFamily(android.graphics.Typeface.create("sans-serif-black", android.graphics.Typeface.NORMAL))),
    SERIF_HEAVY("Majestic Roman Bold", FontFamily(android.graphics.Typeface.create("serif", android.graphics.Typeface.BOLD))),
    RETRO_ARCADE("Neon Laser Arcade", FontFamily(android.graphics.Typeface.create("monospace", android.graphics.Typeface.BOLD)), 0.12f)
}

// CompositionLocal to store the selected font globally
val LocalAppFont = staticCompositionLocalOf { AppFont.DEFAULT }

// Convert all typography token definitions into dynamic @Composable getters
@Composable
fun getDynamicTextStyle(
    sizeSp: Int,
    lineHeightSp: Int,
    weight: FontWeight,
    extraSpacingEm: Float = 0f
): TextStyle {
    val currentFont = LocalAppFont.current
    val isPixelAndUppercase = currentFont.isPixel
    val finalWeight = if (isPixelAndUppercase) FontWeight.ExtraBold else weight
    val finalSpacing = if (isPixelAndUppercase) {
        (0.14f + currentFont.letterSpacingExtra).em
    } else {
        extraSpacingEm.em
    }
    return TextStyle(
        fontFamily = currentFont.fontFamily,
        fontWeight = finalWeight,
        fontSize = sizeSp.sp,
        lineHeight = lineHeightSp.sp,
        letterSpacing = finalSpacing,
        fontFeatureSettings = if (isPixelAndUppercase) "smcp" else null
    )
}

val LiquidDisplayLarge: TextStyle
    @Composable
    get() = getDynamicTextStyle(48, 56, FontWeight.Bold, -0.02f)

val LiquidHeadlineLarge: TextStyle
    @Composable
    get() = getDynamicTextStyle(32, 40, FontWeight.SemiBold)

val LiquidHeadlineLargeMobile: TextStyle
    @Composable
    get() = getDynamicTextStyle(24, 32, FontWeight.SemiBold)

val LiquidBodyMedium: TextStyle
    @Composable
    get() = getDynamicTextStyle(16, 24, FontWeight.Normal)

val LiquidLabelCaps: TextStyle
    @Composable
    get() = getDynamicTextStyle(12, 16, FontWeight.Medium, 0.05f)

// Map central typography system to update on selected font
val Typography: Typography
    @Composable
    get() = Typography(
        displayLarge = LiquidDisplayLarge,
        displayMedium = getDynamicTextStyle(40, 48, FontWeight.Bold),
        displaySmall = getDynamicTextStyle(32, 40, FontWeight.Bold),
        
        headlineLarge = LiquidHeadlineLarge,
        headlineMedium = LiquidHeadlineLargeMobile,
        headlineSmall = getDynamicTextStyle(20, 26, FontWeight.SemiBold),
        
        titleLarge = getDynamicTextStyle(22, 28, FontWeight.Bold),
        titleMedium = getDynamicTextStyle(16, 22, FontWeight.Medium),
        titleSmall = getDynamicTextStyle(14, 18, FontWeight.Medium),
        
        bodyLarge = LiquidBodyMedium,
        bodyMedium = getDynamicTextStyle(14, 20, FontWeight.Normal),
        bodySmall = getDynamicTextStyle(12, 16, FontWeight.Normal),
        
        labelLarge = LiquidLabelCaps,
        labelMedium = getDynamicTextStyle(11, 16, FontWeight.Normal, 0.05f),
        labelSmall = getDynamicTextStyle(9, 13, FontWeight.Normal, 0.05f)
    )

