package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalConfiguration
import com.example.data.*
import com.example.ui.*
import com.example.ui.theme.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import android.media.AudioFormat
import android.media.AudioManager
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.StrokeCap
import android.media.AudioTrack
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState

// ==========================================
// SELF-CONTAINED POP SYNTHESIZER
// ==========================================
object PopSoundPlayer {
    var selectedTheme: CustomBgTheme = CustomBgTheme.DEFAULT

    fun play() {
        play(selectedTheme)
    }

    fun play(theme: CustomBgTheme) {
        Thread {
            try {
                val sampleRate = 44100
                var durationMs = 60
                val sample: DoubleArray
                
                when (theme) {
                    CustomBgTheme.DEFAULT -> {
                        // High-tech gentle soft tick
                        durationMs = 40
                        val numSamples = durationMs * sampleRate / 1000
                        sample = DoubleArray(numSamples)
                        for (i in 0 until numSamples) {
                            val t = i.toDouble() / sampleRate
                            val p = i.toDouble() / numSamples
                            val freq = 800.0 - (400.0 * p)
                            val envelope = Math.exp(-22.0 * p)
                            sample[i] = Math.sin(2.0 * Math.PI * freq * t) * envelope * 0.45
                        }
                    }
                    CustomBgTheme.PUTIH -> {
                        // Mechanical clean click
                        durationMs = 30
                        val numSamples = durationMs * sampleRate / 1000
                        sample = DoubleArray(numSamples)
                        for (i in 0 until numSamples) {
                            val t = i.toDouble() / sampleRate
                            val p = i.toDouble() / numSamples
                            val freq = 1200.0 - (800.0 * p)
                            val envelope = Math.exp(-30.0 * p)
                            sample[i] = (Math.sin(2.0 * Math.PI * freq * t) + (Math.random() - 0.5) * 0.1) * envelope * 0.4
                        }
                    }
                    CustomBgTheme.CREAM -> {
                        // Woodblock
                        durationMs = 80
                        val numSamples = durationMs * sampleRate / 1000
                        sample = DoubleArray(numSamples)
                        for (i in 0 until numSamples) {
                            val t = i.toDouble() / sampleRate
                            val p = i.toDouble() / numSamples
                            val freq = 600.0
                            val envelope = Math.exp(-14.0 * p)
                            sample[i] = Math.sin(2.0 * Math.PI * freq * t) * envelope * 0.5
                        }
                    }
                    CustomBgTheme.BIRU_AWAN -> {
                        // Airy water droplet
                        durationMs = 100
                        val numSamples = durationMs * sampleRate / 1000
                        sample = DoubleArray(numSamples)
                        for (i in 0 until numSamples) {
                            val t = i.toDouble() / sampleRate
                            val p = i.toDouble() / numSamples
                            val freq = 400.0 + (600.0 * p * p)
                            val envelope = Math.exp(-12.0 * p)
                            sample[i] = Math.sin(2.0 * Math.PI * freq * t) * envelope * 0.5
                        }
                    }
                    CustomBgTheme.PINK -> {
                        // Pink squishy bubble
                        durationMs = 70
                        val numSamples = durationMs * sampleRate / 1000
                        sample = DoubleArray(numSamples)
                        for (i in 0 until numSamples) {
                            val t = i.toDouble() / sampleRate
                            val p = i.toDouble() / numSamples
                            val freq = 300.0 + (900.0 * p)
                            val envelope = Math.exp(-11.0 * p)
                            sample[i] = Math.sin(2.0 * Math.PI * freq * t) * envelope * 0.45
                        }
                    }
                    CustomBgTheme.UNGU_SENJA -> {
                        // Purple retro arcade chime
                        durationMs = 90
                        val numSamples = durationMs * sampleRate / 1000
                        sample = DoubleArray(numSamples)
                        for (i in 0 until numSamples) {
                            val t = i.toDouble() / sampleRate
                            val p = i.toDouble() / numSamples
                            val freq1 = 523.25 // C5
                            val freq2 = 659.25 // E5
                            val envelope = Math.exp(-10.0 * p)
                            sample[i] = (Math.sin(2.0 * Math.PI * freq1 * t) + Math.sin(2.0 * Math.PI * freq2 * t) * 0.5) * envelope * 0.35
                        }
                    }
                    CustomBgTheme.HIJAU_MINT -> {
                        // Mint shaker tick
                        durationMs = 50
                        val numSamples = durationMs * sampleRate / 1000
                        sample = DoubleArray(numSamples)
                        for (i in 0 until numSamples) {
                            val p = i.toDouble() / numSamples
                            val envelope = Math.exp(-18.0 * p)
                            sample[i] = (Math.random() - 0.5) * envelope * 0.35
                        }
                    }
                    CustomBgTheme.MATAHARI -> {
                        // Bell-like resonance
                        durationMs = 120
                        val numSamples = durationMs * sampleRate / 1000
                        sample = DoubleArray(numSamples)
                        for (i in 0 until numSamples) {
                            val t = i.toDouble() / sampleRate
                            val p = i.toDouble() / numSamples
                            val freq = 880.0
                            val envelope = Math.exp(-8.0 * p)
                            sample[i] = Math.sin(2.0 * Math.PI * freq * t) * envelope * 0.35
                        }
                    }
                    CustomBgTheme.CYBERPUNK -> {
                        // Tech click
                        durationMs = 70
                        val numSamples = durationMs * sampleRate / 1000
                        sample = DoubleArray(numSamples)
                        for (i in 0 until numSamples) {
                            val t = i.toDouble() / sampleRate
                            val p = i.toDouble() / numSamples
                            val freq = 120.0 + (900.0 * p)
                            val envelope = Math.exp(-16.0 * p)
                            sample[i] = (Math.sin(2.0 * Math.PI * freq * t) + (Math.random() - 0.5) * 0.05) * envelope * 0.4
                        }
                    }
                    CustomBgTheme.AQUA_DEEP -> {
                        // Deep sonar bubble
                        durationMs = 110
                        val numSamples = durationMs * sampleRate / 1000
                        sample = DoubleArray(numSamples)
                        for (i in 0 until numSamples) {
                            val t = i.toDouble() / sampleRate
                            val p = i.toDouble() / numSamples
                            val freq = 440.0
                            val envelope = Math.exp(-7.0 * p)
                            sample[i] = Math.sin(2.0 * Math.PI * freq * t) * envelope * 0.4
                        }
                    }
                    CustomBgTheme.SAKURA -> {
                        // Sakura bell-like blip
                        durationMs = 100
                        val numSamples = durationMs * sampleRate / 1000
                        sample = DoubleArray(numSamples)
                        for (i in 0 until numSamples) {
                            val t = i.toDouble() / sampleRate
                            val p = i.toDouble() / numSamples
                            val freq = 987.77 // B5
                            val envelope = Math.exp(-9.0 * p)
                            sample[i] = Math.sin(2.0 * Math.PI * freq * t) * envelope * 0.35
                        }
                    }
                    CustomBgTheme.MIDNIGHT_FOREST -> {
                        // Wood block tick
                        durationMs = 60
                        val numSamples = durationMs * sampleRate / 1000
                        sample = DoubleArray(numSamples)
                        for (i in 0 until numSamples) {
                            val t = i.toDouble() / sampleRate
                            val p = i.toDouble() / numSamples
                            val freq = 480.0
                            val envelope = Math.exp(-15.0 * p)
                            sample[i] = Math.sin(2.0 * Math.PI * freq * t) * envelope * 0.45
                        }
                    }
                    CustomBgTheme.NORDIC_SLATE -> {
                        // Cold slate crisp click
                        durationMs = 45
                        val numSamples = durationMs * sampleRate / 1000
                        sample = DoubleArray(numSamples)
                        for (i in 0 until numSamples) {
                            val t = i.toDouble() / sampleRate
                            val p = i.toDouble() / numSamples
                            val freq = 1100.0 - (500.0 * p)
                            val envelope = Math.exp(-20.0 * p)
                            sample[i] = Math.sin(2.0 * Math.PI * freq * t) * envelope * 0.35
                        }
                    }
                    CustomBgTheme.RETRO_ARCADE -> {
                        // Retro arcade blip
                        durationMs = 120
                        val numSamples = durationMs * sampleRate / 1000
                        sample = DoubleArray(numSamples)
                        for (i in 0 until numSamples) {
                            val t = i.toDouble() / sampleRate
                            val p = i.toDouble() / numSamples
                            val freq = if (p < 0.4) 600.0 else 1200.0
                            val envelope = Math.exp(-10.0 * p)
                            sample[i] = Math.sin(2.0 * Math.PI * freq * t) * envelope * 0.3
                        }
                    }
                    CustomBgTheme.COSMIC_LAVENDER -> {
                        // Cosmic stellar warp click
                        durationMs = 110
                        val numSamples = durationMs * sampleRate / 1000
                        sample = DoubleArray(numSamples)
                        for (i in 0 until numSamples) {
                            val t = i.toDouble() / sampleRate
                            val p = i.toDouble() / numSamples
                            val freq = 800.0 - (400.0 * Math.sin(p * Math.PI))
                            val envelope = Math.exp(-8.0 * p)
                            sample[i] = Math.sin(2.0 * Math.PI * freq * t) * envelope * 0.4
                        }
                    }
                    CustomBgTheme.FUNKY_NEON -> {
                        // Weird squishy synth pop
                        durationMs = 80
                        val numSamples = durationMs * sampleRate / 1000
                        sample = DoubleArray(numSamples)
                        for (i in 0 until numSamples) {
                            val t = i.toDouble() / sampleRate
                            val p = i.toDouble() / numSamples
                            val freq = 150.0 + (1200.0 * p * p)
                            val envelope = Math.exp(-10.0 * p)
                            sample[i] = Math.sin(2.0 * Math.PI * freq * t) * envelope * 0.45
                        }
                    }
                    CustomBgTheme.LAVA_VOLCANO -> {
                        // Warm volcanic low thud
                        durationMs = 70
                        val numSamples = durationMs * sampleRate / 1000
                        sample = DoubleArray(numSamples)
                        for (i in 0 until numSamples) {
                            val t = i.toDouble() / sampleRate
                            val p = i.toDouble() / numSamples
                            val freq = 120.0 + (80.0 * (1.0 - p))
                            val envelope = Math.exp(-12.0 * p)
                            sample[i] = Math.sin(2.0 * Math.PI * freq * t) * envelope * 0.6
                        }
                    }
                    CustomBgTheme.GLITCH_MATRIX -> {
                        // Digital green bitcrunch click
                        durationMs = 50
                        val numSamples = durationMs * sampleRate / 1000
                        sample = DoubleArray(numSamples)
                        for (i in 0 until numSamples) {
                            val t = i.toDouble() / sampleRate
                            val p = i.toDouble() / numSamples
                            val freq = 3000.0 * (1.0 - p * p)
                            val envelope = Math.exp(-18.0 * p)
                            val step = (Math.sin(2.0 * Math.PI * freq * t) * 10).toInt() / 10.0
                            sample[i] = step * envelope * 0.25
                        }
                    }
                    CustomBgTheme.CANDY_LAND -> {
                        // Sweet cartoon bubble blip
                        durationMs = 80
                        val numSamples = durationMs * sampleRate / 1000
                        sample = DoubleArray(numSamples)
                        for (i in 0 until numSamples) {
                            val t = i.toDouble() / sampleRate
                            val p = i.toDouble() / numSamples
                            val freq = 800.0 + (600.0 * Math.sin(p * Math.PI / 2))
                            val envelope = Math.sin(p * Math.PI) * Math.exp(-5.0 * p)
                            sample[i] = Math.sin(2.0 * Math.PI * freq * t) * envelope * 0.45
                        }
                    }
                    CustomBgTheme.GOOGLE_CLASSIC -> {
                        // Google classic minimal blip
                        durationMs = 60
                        val numSamples = durationMs * sampleRate / 1000
                        sample = DoubleArray(numSamples)
                        for (i in 0 until numSamples) {
                            val t = i.toDouble() / sampleRate
                            val p = i.toDouble() / numSamples
                            val freq = 600.0
                            val envelope = Math.exp(-15.0 * p)
                            sample[i] = Math.sin(2.0 * Math.PI * freq * t) * envelope * 0.4
                        }
                    }
                    CustomBgTheme.GOOGLE_DARK -> {
                        // Google dark clean blip
                        durationMs = 60
                        val numSamples = durationMs * sampleRate / 1000
                        sample = DoubleArray(numSamples)
                        for (i in 0 until numSamples) {
                            val t = i.toDouble() / sampleRate
                            val p = i.toDouble() / numSamples
                            val freq = 500.0
                            val envelope = Math.exp(-15.0 * p)
                            sample[i] = Math.sin(2.0 * Math.PI * freq * t) * envelope * 0.4
                        }
                    }
                    CustomBgTheme.GOOGLE_PIXEL -> {
                        // Pixel digital tick
                        durationMs = 35
                        val numSamples = durationMs * sampleRate / 1000
                        sample = DoubleArray(numSamples)
                        for (i in 0 until numSamples) {
                            val t = i.toDouble() / sampleRate
                            val p = i.toDouble() / numSamples
                            val freq = 1500.0 - (500.0 * p)
                            val envelope = Math.exp(-25.0 * p)
                            sample[i] = Math.sin(2.0 * Math.PI * freq * t) * envelope * 0.35
                        }
                    }
                    CustomBgTheme.GOOGLE_WORK -> {
                        // Google gentle work pop
                        durationMs = 50
                        val numSamples = durationMs * sampleRate / 1000
                        sample = DoubleArray(numSamples)
                        for (i in 0 until numSamples) {
                            val t = i.toDouble() / sampleRate
                            val p = i.toDouble() / numSamples
                            val freq = 700.0 - (200.0 * p)
                            val envelope = Math.exp(-18.0 * p)
                            sample[i] = Math.sin(2.0 * Math.PI * freq * t) * envelope * 0.4
                        }
                    }
                }
                
                val generateSine = ByteArray(2 * sample.size)
                var idx = 0
                for (doubleVal in sample) {
                    val valShort = (doubleVal * 32767).toInt().coerceIn(-32768, 32767).toShort()
                    generateSine[idx++] = (valShort.toInt() and 0x00ff).toByte()
                    generateSine[idx++] = ((valShort.toInt() and 0xff00) ushr 8).toByte()
                }
                
                val audioTrack = AudioTrack(
                    AudioManager.STREAM_MUSIC,
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    generateSine.size,
                    AudioTrack.MODE_STATIC
                )
                audioTrack.write(generateSine, 0, generateSine.size)
                audioTrack.play()
                Thread.sleep(durationMs.toLong() + 20)
                audioTrack.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}

// ==========================================
// SELF-CONTAINED MEOW SYNTHESIZER
// ==========================================
object CatMeowSoundPlayer {
    fun play(type: Int = 1) {
        Thread {
            try {
                val sampleRate = 44100
                val durationMs = when (type) {
                    2 -> 350   // Short cute happy squeak
                    3 -> 550   // Retro coin style
                    4 -> 600   // Longer growling meow
                    5 -> 400   // Cute warm double meow
                    else -> 450 // Standard meow
                }
                val numSamples = durationMs * sampleRate / 1000
                val sample = DoubleArray(numSamples)
                val generateBuffer = ShortArray(numSamples)
                
                val baseFreq = when (type) {
                    2 -> 620.0   // High happy pitch
                    3 -> 550.0   // Chiptune coin pitch
                    4 -> 280.0   // Low angry growl
                    5 -> 580.0   // Soft purry pitch
                    else -> 480.0 // Standard meow
                }
                
                val endFreq = when (type) {
                    2 -> 1100.0
                    3 -> 900.0
                    4 -> 340.0
                    5 -> 950.0
                    else -> 920.0
                }

                val fallFreq = when (type) {
                    2 -> 800.0
                    3 -> 750.0
                    4 -> 240.0
                    5 -> 780.0
                    else -> 650.0
                }
                
                for (i in 0 until numSamples) {
                    val t = i.toDouble() / sampleRate
                    val progress = i.toDouble() / numSamples
                    
                    // Frequency glide curves customized by meow type
                    val freq = if (progress < 0.25) {
                        baseFreq + ((endFreq - baseFreq) * (progress / 0.25))
                    } else {
                        endFreq - ((endFreq - fallFreq) * ((progress - 0.25) / 0.75))
                    }
                    
                    // Smooth envelope with humanized nasal cat vocal curves
                    val envelope = if (progress < 0.12) {
                        progress / 0.12
                    } else {
                        Math.exp(-3.2 * (progress - 0.12))
                    }
                    
                    // Frequency modulation for real custom vibrato
                    val vibratoSpeed = when (type) {
                        3 -> 24.0 // rapid chiptune vibrato
                        4 -> 8.0  // low heavy angry growling vibrato
                        5 -> 18.0 // sweet purring vibrato
                        else -> 14.0
                    }
                    val vibratoDepth = when (type) {
                        4 -> 0.10 // deeper growling modulation
                        else -> 0.05
                    }
                    val vibrato = 1.0 + vibratoDepth * Math.sin(2.0 * Math.PI * vibratoSpeed * t)
                    val baseSine = Math.sin(2.0 * Math.PI * freq * vibrato * t)
                    
                    // Add harmonics for rich high-quality acoustic tone
                    val secondHarmonic = Math.sin(2.0 * Math.PI * (freq * 1.45) * vibrato * t) * 0.45
                    val thirdHarmonic = Math.sin(2.0 * Math.PI * (freq * 2.1) * vibrato * t) * 0.25
                    val noiseComponent = ((Math.random() - 0.5) * 0.04) * envelope
                    
                    val mixed = (baseSine + secondHarmonic + thirdHarmonic) / 1.7
                    sample[i] = (mixed + noiseComponent) * envelope * 0.75
                }
                
                for (i in sample.indices) {
                    generateBuffer[i] = (sample[i] * 32767).toInt().coerceIn(-32768, 32767).toShort()
                }
                
                val track = AudioTrack(
                    AudioManager.STREAM_MUSIC,
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    generateBuffer.size * 2,
                    AudioTrack.MODE_STATIC
                )
                track.write(generateBuffer, 0, generateBuffer.size)
                track.play()
                
                Thread.sleep(durationMs.toLong() + 30L)
                track.stop()
                track.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}

// ==========================================
// ==========================================
// HETEROCHROMIA 3D CAT VECTOR BUTTON COMPOSABLE
// ==========================================
@Composable
fun HeterochromiaCatButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    var animState by remember { mutableStateOf(0) } // 0=IDLE, 1..24 = Distinct emotional/prop states!
    val coroutineScope = rememberCoroutineScope()
    var resetJob by remember { mutableStateOf<Job?>(null) }

    fun selectRandomAnimation(current: Int): Int {
        val choices = (1..24).filter { it != current }
        return choices.random()
    }

    // Bouncy spring transition progress
    val transitionProgress by animateFloatAsState(
        targetValue = if (animState != 0) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "CatTransition"
    )

    Box(
        modifier = modifier
            .size(38.dp)
            .clip(CircleShape)
            .clickable {
                resetJob?.cancel()
                val nextState = selectRandomAnimation(animState)
                animState = nextState
                
                // Play standard pop sound + custom localized meow sound!
                PopSoundPlayer.play()
                CatMeowSoundPlayer.play(if (nextState in 1..5) nextState else (1..5).random())
                
                onClick()
                
                resetJob = coroutineScope.launch {
                    delay(3500) // Keep the custom emotion visible on screen longer for user joy!
                    animState = 0
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            val activeAngry = if (animState == 4 || animState == 12) transitionProgress else 0f
            
            // Helper function to draw a shiny red heart shape (♥)
            fun DrawScope.drawHeart(cx: Float, cy: Float, sizeVal: Float) {
                val path = Path().apply {
                    val hs = sizeVal
                    moveTo(cx, cy + hs * 0.25f)
                    cubicTo(
                        cx - hs * 0.55f, cy - hs * 0.45f,
                        cx - hs * 0.85f, cy + hs * 0.35f,
                        cx, cy + hs * 0.9f
                    )
                    cubicTo(
                        cx + hs * 0.85f, cy + hs * 0.35f,
                        cx + hs * 0.55f, cy - hs * 0.45f,
                        cx, cy + hs * 0.25f
                    )
                    close()
                }
                drawPath(path = path, color = Color(0xFFF43F5E))
            }

            // Helper function to draw happy squinting curve eyes
            fun DrawScope.drawSmileEye(cx: Float, cy: Float, sizeW: Float, sizeH: Float, isLeft: Boolean) {
                val path = Path().apply {
                    moveTo(cx - sizeW * 0.8f, cy + sizeH * 0.1f)
                    quadraticTo(cx, cy - sizeH * 0.5f, cx + sizeW * 0.8f, cy + sizeH * 0.1f)
                }
                drawPath(
                    path = path, 
                    color = if (isLeft) Color(0xFF00B0FF) else Color(0xFFFFD54F), 
                    style = Stroke(width = sizeW * 0.3f, cap = StrokeCap.Round)
                )
            }

            // Helper function to draw coin-style eyes with "$" symbols
            fun DrawScope.drawMoneyEye(cx: Float, cy: Float, sizeW: Float, sizeH: Float) {
                // Shiny Green Coin Base
                drawCircle(
                    color = Color(0xFF10B981),
                    radius = sizeH * 0.55f,
                    center = Offset(cx, cy)
                )
                // Shiny Yellow Border
                drawCircle(
                    color = Color(0xFFFBBF24),
                    radius = sizeH * 0.55f,
                    center = Offset(cx, cy),
                    style = Stroke(width = sizeW * 0.15f)
                )
                // Vertical strip of $ sign
                drawLine(
                    color = Color(0xFFFEF08A),
                    start = Offset(cx, cy - sizeH * 0.45f),
                    end = Offset(cx, cy + sizeH * 0.45f),
                    strokeWidth = sizeW * 0.12f,
                    cap = StrokeCap.Round
                )
                // Curve parts of S shape
                val sPath = Path().apply {
                    moveTo(cx + sizeW * 0.22f, cy - sizeH * 0.25f)
                    quadraticTo(cx, cy - sizeH * 0.25f, cx - sizeW * 0.15f, cy - sizeH * 0.1f)
                    quadraticTo(cx, cy, cx + sizeW * 0.2f, cy + sizeH * 0.1f)
                    quadraticTo(cx, cy + sizeH * 0.25f, cx - sizeW * 0.22f, cy + sizeH * 0.25f)
                }
                drawPath(path = sPath, color = Color(0xFFFEF08A), style = Stroke(width = sizeW * 0.12f, cap = StrokeCap.Round))
            }

            // Helper function for procedural stars
            fun DrawScope.drawYellowStar(cx: Float, cy: Float, r: Float) {
                val star = Path().apply {
                    moveTo(cx, cy - r)
                    quadraticTo(cx, cy, cx + r, cy)
                    quadraticTo(cx, cy, cx, cy + r)
                    quadraticTo(cx, cy, cx - r, cy)
                    quadraticTo(cx, cy, cx, cy - r)
                }
                drawPath(path = star, color = Color(0xFFFBBF24))
            }

            // Draw Ears (Geometric triangles)
            // Left ear (Cat's right) - squashes downwards if angry
            val leftEarTipX = w * (0.16f - 0.08f * activeAngry)
            val leftEarTipY = h * (0.12f + 0.20f * activeAngry)
            val leftEar = Path().apply {
                moveTo(w * 0.22f, h * 0.38f)
                lineTo(leftEarTipX, leftEarTipY)
                lineTo(w * 0.46f, h * 0.32f)
                close()
            }
            drawPath(path = leftEar, color = Color(0xFFF1F5F9))
            drawPath(path = leftEar, color = Color(0xFF111827), style = Stroke(width = w * 0.035f, cap = StrokeCap.Round))
            
            val leftEarInnerTipX = w * (0.20f - 0.08f * activeAngry)
            val leftEarInnerTipY = h * (0.18f + 0.16f * activeAngry)
            val leftEarInner = Path().apply {
                moveTo(w * 0.25f, h * 0.34f)
                lineTo(leftEarInnerTipX, leftEarInnerTipY)
                lineTo(w * 0.40f, h * 0.30f)
                close()
            }
            drawPath(path = leftEarInner, color = Color(0xFFFECDD3))

            // Right ear (Cat's left) - squashes downwards if angry
            val rightEarTipX = w * (0.84f + 0.08f * activeAngry)
            val rightEarTipY = h * (0.12f + 0.20f * activeAngry)
            val rightEar = Path().apply {
                moveTo(w * 0.78f, h * 0.38f)
                lineTo(rightEarTipX, rightEarTipY)
                lineTo(w * 0.54f, h * 0.32f)
                close()
            }
            drawPath(path = rightEar, color = Color(0xFFF1F5F9))
            drawPath(path = rightEar, color = Color(0xFF111827), style = Stroke(width = w * 0.035f, cap = StrokeCap.Round))
            
            val rightEarInnerTipX = w * (0.80f + 0.08f * activeAngry)
            val rightEarInnerTipY = h * (0.18f + 0.16f * activeAngry)
            val rightEarInner = Path().apply {
                moveTo(w * 0.75f, h * 0.34f)
                lineTo(rightEarInnerTipX, rightEarInnerTipY)
                lineTo(w * 0.60f, h * 0.30f)
                close()
            }
            drawPath(path = rightEarInner, color = Color(0xFFFECDD3))

            // Main Head Body (Glossy 3D circle with a radial light gradient, flushing reddish if angry)
            val headCenterColor = Color(
                red = (1f - 0.05f * activeAngry),
                green = (1f - 0.15f * activeAngry),
                blue = (1f - 0.12f * activeAngry)
            )
            val headOuterColor = Color(
                red = (0.88f + 0.1f * activeAngry),
                green = (0.91f - 0.18f * activeAngry),
                blue = (0.94f - 0.15f * activeAngry)
            )
            
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(headCenterColor, headOuterColor),
                    center = Offset(w * 0.48f, h * 0.48f),
                    radius = w * 0.36f
                ),
                radius = w * 0.36f,
                center = Offset(w * 0.5f, h * 0.55f)
            )
            drawCircle(
                color = Color(0xFF111827),
                radius = w * 0.36f,
                center = Offset(w * 0.5f, h * 0.55f),
                style = Stroke(width = w * 0.035f)
            )

            // Cat Eyes with intense 3D Depth Heterochromia/Anim transitions:
            val eyeY = h * 0.52f
            val eyeW = w * 0.08f
            val eyeH = w * 0.13f
            
            // 1. LEFT EYE (Viewer's left)
            when (animState) {
                5 -> { // LOVE
                    drawHeart(w * 0.30f, eyeY - eyeH * 0.1f, eyeH * (0.9f + 0.1f * transitionProgress))
                }
                2, 22 -> { // SMILE, HAPPY_HAPPY
                    drawSmileEye(w * 0.30f, eyeY, eyeW, eyeH, true)
                }
                3 -> { // MONEY
                    drawMoneyEye(w * 0.30f, eyeY, eyeW, eyeH)
                }
                6 -> { // SLEEPY (closed arcs pointing down)
                    val p = Path().apply {
                        moveTo(w * 0.30f - eyeW, eyeY)
                        quadraticTo(w * 0.30f, eyeY + eyeH * 0.3f, w * 0.30f + eyeW, eyeY)
                    }
                    drawPath(p, color = Color(0xFF00B0FF), style = Stroke(width = eyeW * 0.3f, cap = StrokeCap.Round))
                }
                7 -> { // WINK (left eye closed)
                    val p = Path().apply {
                        moveTo(w * 0.30f - eyeW, eyeY)
                        quadraticTo(w * 0.30f, eyeY + eyeH * 0.3f, w * 0.30f + eyeW, eyeY)
                    }
                    drawPath(p, color = Color(0xFF00B0FF), style = Stroke(width = eyeW * 0.3f, cap = StrokeCap.Round))
                }
                15 -> { // STAR_EYES
                    drawYellowStar(w * 0.30f, eyeY, eyeH * 0.5f)
                }
                8 -> { // SHOCKED
                    drawCircle(color = Color(0xFF00B0FF), radius = eyeH * 0.5f, center = Offset(w * 0.30f, eyeY))
                    drawCircle(color = Color(0xFF001530), radius = eyeH * 0.18f, center = Offset(w * 0.30f, eyeY))
                }
                21 -> { // SUSPICIOUS
                    drawRect(color = Color(0xFF00B0FF), topLeft = Offset(w * 0.30f - eyeW, eyeY - eyeW * 0.2f), size = Size(eyeW * 2f, eyeW * 0.4f))
                }
                11 -> { // CRYING
                    drawOval(color = Color(0xFF00B0FF), topLeft = Offset(w * 0.30f - eyeW / 2, eyeY - eyeH / 2), size = Size(eyeW, eyeH))
                    drawCircle(color = Color(0xFF001530), radius = eyeW * 0.2f, center = Offset(w * 0.30f, eyeY))
                    val tearPath = Path().apply {
                        moveTo(w * 0.30f, eyeY + eyeH * 0.2f)
                        lineTo(w * 0.33f, eyeY + eyeH * 0.7f)
                        quadraticTo(w * 0.30f, eyeY + eyeH * 0.9f, w * 0.27f, eyeY + eyeH * 0.7f)
                        close()
                    }
                    drawPath(tearPath, color = Color(0xFF38BDF8))
                }
                else -> { // Normal or standard shapes
                    val squeezeY = if (animState == 1) 0.75f else if (animState == 4) 0.65f else 1f
                    val finalEyeH = eyeH * squeezeY
                    drawOval(
                        color = Color(0xFF00B0FF),
                        topLeft = Offset(w * 0.30f - eyeW / 2, eyeY - finalEyeH / 2),
                        size = Size(eyeW, finalEyeH)
                    )
                    drawOval(
                        color = Color(0xFF001530),
                        topLeft = Offset(w * 0.30f - (eyeW * 0.3f) / 2, eyeY - (finalEyeH * 0.7f) / 2),
                        size = Size(eyeW * 0.3f, finalEyeH * 0.7f)
                    )
                    drawCircle(
                        color = Color.White,
                        radius = w * 0.018f,
                        center = Offset(w * 0.28f, eyeY - finalEyeH * 0.22f)
                    )
                }
            }

            // 2. RIGHT EYE (Viewer's right)
            when (animState) {
                5 -> { // LOVE
                    drawHeart(w * 0.70f, eyeY - eyeH * 0.1f, eyeH * (0.9f + 0.1f * transitionProgress))
                }
                2, 22 -> { // SMILE, HAPPY_HAPPY
                    drawSmileEye(w * 0.70f, eyeY, eyeW, eyeH, false)
                }
                3 -> { // MONEY
                    drawMoneyEye(w * 0.70f, eyeY, eyeW, eyeH)
                }
                6 -> { // SLEEPY
                    val p = Path().apply {
                        moveTo(w * 0.70f - eyeW, eyeY)
                        quadraticTo(w * 0.70f, eyeY + eyeH * 0.3f, w * 0.70f + eyeW, eyeY)
                    }
                    drawPath(p, color = Color(0xFFFFD54F), style = Stroke(width = eyeW * 0.3f, cap = StrokeCap.Round))
                }
                15 -> { // STAR_EYES
                    drawYellowStar(w * 0.70f, eyeY, eyeH * 0.5f)
                }
                8 -> { // SHOCKED
                    drawCircle(color = Color(0xFFFFD54F), radius = eyeH * 0.5f, center = Offset(w * 0.70f, eyeY))
                    drawCircle(color = Color(0xFF422100), radius = eyeH * 0.18f, center = Offset(w * 0.70f, eyeY))
                }
                21 -> { // SUSPICIOUS
                    drawRect(color = Color(0xFFFFD54F), topLeft = Offset(w * 0.70f - eyeW, eyeY - eyeW * 0.2f), size = Size(eyeW * 2f, eyeW * 0.4f))
                }
                11 -> { // CRYING
                    drawOval(color = Color(0xFFFFD54F), topLeft = Offset(w * 0.70f - eyeW / 2, eyeY - eyeH / 2), size = Size(eyeW, eyeH))
                    drawCircle(color = Color(0xFF422100), radius = eyeW * 0.2f, center = Offset(w * 0.70f, eyeY))
                    val tearPath = Path().apply {
                        moveTo(w * 0.70f, eyeY + eyeH * 0.2f)
                        lineTo(w * 0.73f, eyeY + eyeH * 0.7f)
                        quadraticTo(w * 0.70f, eyeY + eyeH * 0.9f, w * 0.67f, eyeY + eyeH * 0.7f)
                        close()
                    }
                    drawPath(tearPath, color = Color(0xFF38BDF8))
                }
                else -> { // Standard/idle/others are normal on right side
                    val squeezeY = if (animState == 1) 0.75f else if (animState == 4) 0.65f else 1f
                    val finalEyeH = eyeH * squeezeY
                    drawOval(
                        color = Color(0xFFFFD54F),
                        topLeft = Offset(w * 0.70f - eyeW / 2, eyeY - finalEyeH / 2),
                        size = Size(eyeW, finalEyeH)
                    )
                    drawOval(
                        color = Color(0xFF422100),
                        topLeft = Offset(w * 0.70f - (eyeW * 0.3f) / 2, eyeY - (finalEyeH * 0.7f) / 2),
                        size = Size(eyeW * 0.3f, finalEyeH * 0.7f)
                    )
                    drawCircle(
                        color = Color.White,
                        radius = w * 0.018f,
                        center = Offset(w * 0.68f, eyeY - finalEyeH * 0.22f)
                    )
                }
            }

            // Slanted eyebrows
            if (animState == 4 || animState == 12) { // ANGRY or DEVILISH
                drawLine(
                    color = Color(0xFF1E293B),
                    start = Offset(w * 0.21f, eyeY - eyeH * 0.65f),
                    end = Offset(w * 0.39f, eyeY - eyeH * 0.3f),
                    strokeWidth = w * 0.035f,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = Color(0xFF1E293B),
                    start = Offset(w * 0.79f, eyeY - eyeH * 0.65f),
                    end = Offset(w * 0.61f, eyeY - eyeH * 0.3f),
                    strokeWidth = w * 0.035f,
                    cap = StrokeCap.Round
                )
            }

            // Snout / Nose (Cute soft pink inverted rounded triangle)
            if (animState != 19) { // Normal snout unless CLOWN
                val nosePath = Path().apply {
                    moveTo(w * 0.47f, h * 0.64f)
                    lineTo(w * 0.53f, h * 0.64f)
                    lineTo(w * 0.50f, h * 0.68f)
                    close()
                }
                drawPath(path = nosePath, color = Color(0xFFFB7185))
            } else { // CLOWN big red nose
                drawCircle(color = Color(0xFFEF4444), radius = w * 0.06f, center = Offset(w * 0.5f, h * 0.66f))
            }

            // Mouth draw states
            when (animState) {
                1, 8, 18 -> { // OPEN / MEOW / SHOCKED
                    drawCircle(
                        color = Color(0xFF1E293B),
                        radius = w * 0.08f,
                        center = Offset(w * 0.50f, h * 0.71f)
                    )
                    drawCircle(
                        color = Color(0xFFFDA4AF),
                        radius = w * 0.045f,
                        center = Offset(w * 0.50f, h * 0.73f)
                    )
                }
                13 -> { // DERP tongue out
                    val mouthLeft = Path().apply {
                        moveTo(w * 0.50f, h * 0.68f)
                        quadraticTo(w * 0.46f, h * 0.72f, w * 0.43f, h * 0.69f)
                    }
                    val mouthRight = Path().apply {
                        moveTo(w * 0.50f, h * 0.68f)
                        quadraticTo(w * 0.54f, h * 0.72f, w * 0.57f, h * 0.69f)
                    }
                    drawPath(path = mouthLeft, color = Color(0xFF64748B), style = Stroke(width = w * 0.025f, cap = StrokeCap.Round))
                    drawPath(path = mouthRight, color = Color(0xFF64748B), style = Stroke(width = w * 0.025f, cap = StrokeCap.Round))
                    // Draw pink tongue hanging out to the side
                    drawRoundRect(
                        color = Color(0xFFFB7185),
                        topLeft = Offset(w * 0.47f, h * 0.70f),
                        size = Size(w * 0.07f, w * 0.07f),
                        cornerRadius = CornerRadius(10f, 10f)
                    )
                }
                20 -> { // CONFUSED squiggly mouth
                    val squigPath = Path().apply {
                        moveTo(w * 0.42f, h * 0.70f)
                        quadraticTo(w * 0.46f, h * 0.67f, w * 0.50f, h * 0.71f)
                        quadraticTo(w * 0.54f, h * 0.73f, w * 0.58f, h * 0.69f)
                    }
                    drawPath(path = squigPath, color = Color(0xFF1E293B), style = Stroke(width = w * 0.025f, cap = StrokeCap.Round))
                }
                23 -> { // NINJA mask - draw a sleek shadow wrap around nose area hiding mouth
                    drawRect(
                        color = Color(0xFF1E293B),
                        topLeft = Offset(w * 0.20f, h * 0.62f),
                        size = Size(w * 0.60f, h * 0.18f),
                        alpha = 0.95f
                    )
                }
                else -> { // Normal grin / smile mouth
                    val mouthLeft = Path().apply {
                        moveTo(w * 0.50f, h * 0.68f)
                        if (animState == 4 || animState == 21) {
                            quadraticTo(w * 0.46f, h * 0.64f, w * 0.43f, h * 0.66f)
                        } else {
                            quadraticTo(w * 0.46f, h * 0.72f, w * 0.43f, h * 0.69f)
                        }
                    }
                    val mouthRight = Path().apply {
                        moveTo(w * 0.50f, h * 0.68f)
                        if (animState == 4 || animState == 21) {
                            quadraticTo(w * 0.54f, h * 0.64f, w * 0.57f, h * 0.66f)
                        } else {
                            quadraticTo(w * 0.54f, h * 0.72f, w * 0.57f, h * 0.69f)
                        }
                    }
                    drawPath(path = mouthLeft, color = Color(0xFF64748B), style = Stroke(width = w * 0.025f, cap = StrokeCap.Round))
                    drawPath(path = mouthRight, color = Color(0xFF64748B), style = Stroke(width = w * 0.025f, cap = StrokeCap.Round))
                }
            }

            // Rosy Cheek Blushes which add lovely chibi character
            val blushColor = if (animState == 3) {
                Color(0xFF34D399).copy(alpha = 0.4f)
            } else if (animState == 5) {
                Color(0xFFFB7185).copy(alpha = 0.65f)
            } else {
                Color(0xFFFDA4AF).copy(alpha = 0.45f)
            }
            
            val blushScale = if (animState == 5) 1.35f else if (animState == 2) 1.25f else 1.0f
            val blushRadius = w * 0.05f * blushScale

            drawCircle(
                color = blushColor,
                radius = blushRadius,
                center = Offset(w * 0.24f, h * 0.65f)
            )
            drawCircle(
                color = blushColor,
                radius = blushRadius,
                center = Offset(w * 0.76f, h * 0.65f)
            )

            // ACCESSORIES overlays
            when (animState) {
                14 -> { // PIRATE (eyepatch over left eye)
                    drawCircle(color = Color(0xFF1E293B), radius = eyeH * 0.6f, center = Offset(w * 0.3f, eyeY))
                    drawLine(color = Color(0xFF1E293B), start = Offset(w * 0.14f, eyeY - eyeH * 0.5f), end = Offset(w * 0.5f, eyeY - eyeH * 0.8f), strokeWidth = w * 0.02f)
                }
                9 -> { // INTELLECTUAL GLASSES
                    drawCircle(color = Color(0xFF1E293B), radius = eyeH * 0.65f, center = Offset(w * 0.3f, eyeY), style = Stroke(width = w * 0.025f))
                    drawCircle(color = Color(0xFF1E293B), radius = eyeH * 0.65f, center = Offset(w * 0.70f, eyeY), style = Stroke(width = w * 0.025f))
                    drawLine(color = Color(0xFF1E293B), start = Offset(w * 0.3f + eyeH * 0.65f, eyeY), end = Offset(w * 0.70f - eyeH * 0.65f, eyeY), strokeWidth = w * 0.025f)
                }
                10 -> { // CYBER GLASSES (Visor)
                    drawRoundRect(
                        color = Color(0xFFEC4899),
                        topLeft = Offset(w * 0.22f, eyeY - eyeW * 0.4f),
                        size = Size(w * 0.56f, eyeW * 0.8f),
                        cornerRadius = CornerRadius(8f, 8f),
                        style = Stroke(width = w * 0.03f)
                    )
                    drawRoundRect(
                        color = Color(0xFF00FFCC),
                        topLeft = Offset(w * 0.24f, eyeY - eyeW * 0.3f),
                        size = Size(w * 0.52f, eyeW * 0.6f),
                        cornerRadius = CornerRadius(6f, 6f),
                        alpha = 0.8f
                    )
                }
                16 -> { // GENTLEMAN MOUSTACHE
                    val mustachePath = Path().apply {
                        moveTo(w * 0.5f, h * 0.70f)
                        cubicTo(w * 0.44f, h * 0.68f, w * 0.38f, h * 0.69f, w * 0.34f, h * 0.73f)
                        cubicTo(w * 0.38f, h * 0.74f, w * 0.44f, h * 0.72f, w * 0.5f, h * 0.71f)
                        cubicTo(w * 0.56f, h * 0.72f, w * 0.62f, h * 0.74f, w * 0.66f, h * 0.73f)
                        cubicTo(w * 0.62f, h * 0.69f, w * 0.56f, h * 0.68f, w * 0.5f, h * 0.70f)
                    }
                    drawPath(mustachePath, color = Color(0xFF334155))
                }
                18 -> { // ALIEN
                    drawLine(color = Color(0xFF22C55E), start = Offset(w * 0.5f, h * 0.23f), end = Offset(w * 0.5f, h * 0.12f), strokeWidth = w * 0.03f, cap = StrokeCap.Round)
                    drawCircle(color = Color(0xFF34D399), radius = w * 0.04f, center = Offset(w * 0.5f, h * 0.10f))
                }
                12 -> { // DEVIL HORNS
                    val leftHorn = Path().apply {
                        moveTo(w * 0.32f, h * 0.26f)
                        quadraticTo(w * 0.22f, h * 0.18f, w * 0.25f, h * 0.12f)
                        quadraticTo(w * 0.34f, h * 0.18f, w * 0.38f, h * 0.26f)
                    }
                    drawPath(leftHorn, color = Color(0xFFEF4444))
                    val rightHorn = Path().apply {
                        moveTo(w * 0.68f, h * 0.26f)
                        quadraticTo(w * 0.78f, h * 0.18f, w * 0.75f, h * 0.12f)
                        quadraticTo(w * 0.66f, h * 0.18f, w * 0.62f, h * 0.26f)
                    }
                    drawPath(rightHorn, color = Color(0xFFEF4444))
                }
                24 -> { // PARTY HAT CONE
                    val partyPath = Path().apply {
                        moveTo(w * 0.5f, h * 0.05f)
                        lineTo(w * 0.36f, h * 0.24f)
                        lineTo(w * 0.64f, h * 0.24f)
                        close()
                    }
                    drawPath(partyPath, color = Color(0xFFFFD54F))
                    drawCircle(color = Color(0xFFEC4899), radius = w * 0.03f, center = Offset(w * 0.5f, h * 0.04f))
                    drawLine(color = Color(0xFF3B82F6), start = Offset(w * 0.43f, h * 0.15f), end = Offset(w * 0.57f, h * 0.15f), strokeWidth = w * 0.02f)
                }
            }

            // Whiskers (Wiggle slightly on meow or touch)
            val wiggle = if (animState == 1) (5f * (Math.sin(System.currentTimeMillis() / 40.0)).toFloat()) else 0f
            
            // Left Whiskers
            drawLine(
                color = Color(0xFF94A3B8), 
                start = Offset(w * 0.25f, h * 0.64f), 
                end = Offset(w * 0.08f, h * 0.61f + wiggle), 
                strokeWidth = w * 0.015f
            )
            drawLine(
                color = Color(0xFF94A3B8), 
                start = Offset(w * 0.25f, h * 0.68f), 
                end = Offset(w * 0.07f, h * 0.69f - wiggle), 
                strokeWidth = w * 0.015f
            )
            // Right Whiskers
            drawLine(
                color = Color(0xFF94A3B8), 
                start = Offset(w * 0.75f, h * 0.64f), 
                end = Offset(w * 0.92f, h * 0.61f - wiggle), 
                strokeWidth = w * 0.015f
            )
            drawLine(
                color = Color(0xFF94A3B8), 
                start = Offset(w * 0.75f, h * 0.68f), 
                end = Offset(w * 0.93f, h * 0.69f + wiggle), 
                strokeWidth = w * 0.015f
            )
        }
    }
}

// ==========================================
// RECAP AGGREGATION MODEL
// ==========================================
data class RecapItem(val label: String, val income: Double, val expense: Double)

fun getStartOfDay(time: Long): Long {
    val cal = Calendar.getInstance()
    cal.timeInMillis = time
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}

// ==========================================
// BACKGROUND VISUAL CUSTOMIZER CHOICES
// ==========================================
enum class CustomBgTheme(
    val displayName: String, 
    val bgColor: Color, 
    val isDark: Boolean,
    val primaryColor: Color,
    val expenseColor: Color,
    val positiveCardStart: Color,
    val positiveCardEnd: Color,
    val labelCapsColor: Color
) {
    DEFAULT(
        "Default", 
        Color(0xFF080C14), 
        true,
        Color(0xFF00BEB2),          // Toska
        Color(0xFFE53935),          // Red for Bokek
        Color(0xFF003F3C),          // Rich Dark Teal
        Color(0xFF02171E),          // Deep Abyssal Blue
        Color(0xFF94A3B8)           // Slate labels
    ),
    PUTIH(
        "Putih", 
        Color(0xFFF8FAFC), 
        false,
        Color(0xFF1D4ED8),          // Deep Royal Blue
        Color(0xFFDC2626),          // Clean Crimson
        Color(0xFFEFF6FF),          // Soft Cozy Blue
        Color(0xFFDBEAFE),          // Elegant Sky Blue
        Color(0xFF475569)           // Dark slate
    ),
    CREAM(
        "Cream", 
        Color(0xFFFAF7EE), 
        false,
        Color(0xFF0F766E),          // Deep Forest Teal
        Color(0xFFB91C1C),          // Warm Burgundy Red
        Color(0xFFF0FDF4),          // Light Mint Green
        Color(0xFFDCFCE7),          // Soft Sage
        Color(0xFF78350F)           // Warm Brown Accent
    ),
    BIRU_AWAN(
        "Biru Awan", 
        Color(0xFFE0F2FE), 
        false,
        Color(0xFF0284C7),          // Sky Ocean Blue
        Color(0xFFEA580C),          // Deep Coral Orange-Red
        Color(0xFFF0F9FF),          // Soft Cloud
        Color(0xFFE0F2FE),          // Clear Air Blue
        Color(0xFF0F172A)           // Deep Navy Slate
    ),
    PINK(
        "Pink", 
        Color(0xFFFCE7F3), 
        false,
        Color(0xFFBE185D),          // Rich Sweety Rose
        Color(0xFF9F1239),          // Crimson Plum Red
        Color(0xFFFDF2F8),          // Delicate Pink Bubble
        Color(0xFFFCE7F3),          // Warm Pink Peach
        Color(0xFF9D174D)           // Rosewood Wine labels
    ),
    UNGU_SENJA(
        "Ungu Senja",
        Color(0xFF13113C),
        true,
        Color(0xFFA78BFA),          // Purple Glow
        Color(0xFFF43F5E),          // Bright Rose
        Color(0xFF2E1065),          // Velvet Dark Purple
        Color(0xFF170D30),          // Shadow Purple
        Color(0xFFC084FC)           // Light lavender
    ),
    HIJAU_MINT(
        "Hijau Mint",
        Color(0xFF021E17),
        true,
        Color(0xFF34D399),          // Mint Green
        Color(0xFFF87171),          // Light Red Coral
        Color(0xFF064E3B),          // Dark Forest
        Color(0xFF022C22),          // Deep Abyssal Green
        Color(0xFFA7F3D0)           // Minty Sage labels
    ),
    MATAHARI(
        "Matahari",
        Color(0xFFFFF7ED),
        false,
        Color(0xFFD97706),          // Sunset Amber Gold
        Color(0xFFEF4444),          // Bright Red Flame
        Color(0xFFFEF3C7),          // Sun Light Amber
        Color(0xFFFFEDD5),          // Sweet Papaya Peach
        Color(0xFF9A3412)           // Rich Terracotta labels
    ),
    CYBERPUNK(
        "Cyberpunk",
        Color(0xFF030712),
        true,
        Color(0xFFF0ABFC),          // Neon Orchid Fuchsia
        Color(0xFF22D3EE),          // Neon Cyber Cyan
        Color(0xFF1E1B4B),          // Retro Synth Dark Purple
        Color(0xFF0A0F24),          // Cyber Abyssal Blue
        Color(0xFFE9D5FF)           // Glowing Violet labels
    ),
    AQUA_DEEP(
        "Aqua Deep",
        Color(0xFF030E1C),
        true,
        Color(0xFF38BDF8),          // Ocean Sky Blue
        Color(0xFFFB7185),          // Warm Coral Coral
        Color(0xFF0F2C59),          // Submarine Blue
        Color(0xFF04152D),          // Trench Blue
        Color(0xFF7DD3FC)           // Oceanic Teal labels
    ),
    SAKURA(
        "Sakura Dream",
        Color(0xFFFFF1F2),
        false,
        Color(0xFFEC4899),          // Bubblegum Pink
        Color(0xFFBE123C),          // Dark Rose
        Color(0xFFFFE4E6),          // Sweet Cherry
        Color(0xFFFECDD3),          // Soft Coral
        Color(0xFF9F1239)           // Rich Pink labels
    ),
    MIDNIGHT_FOREST(
        "Midnight Forest",
        Color(0xFF022C22),
        true,
        Color(0xFF10B981),          // Emerald Gold Glow
        Color(0xFFFBBF24),          // Bright Amber Gold
        Color(0xFF064E3B),          // Velvet Deep Moss
        Color(0xFF022C22),          // Midnight Fern Green
        Color(0xFFA7F3D0)           // Mossy Sage labels
    ),
    NORDIC_SLATE(
        "Nordic Slate",
        Color(0xFFF1F5F9),
        false,
        Color(0xFF475569),          // Slate Cool Steel
        Color(0xFF0F172A),          // Deep Obsidian
        Color(0xFFE2E8F0),          // Ash Frost
        Color(0xFFCBD5E1),          // Arctic Silver
        Color(0xFF334155)           // Steel labels
    ),
    RETRO_ARCADE(
        "Retro Arcade",
        Color(0xFF0C0A09),
        true,
        Color(0xFFF97316),          // Radio orange
        Color(0xFF22C55E),          // Laser Green
        Color(0xFF292524),          // Dark coal card
        Color(0xFF1C1917),          // Warm dark pit
        Color(0xFFFFEDD5)           // Laser label pastel
    ),
    COSMIC_LAVENDER(
        "Cosmic Lavender",
        Color(0xFFFAF5FF),
        false,
        Color(0xFF7C3AED),          // Magic Purple
        Color(0xFFDB2777),          // Galactic Rose
        Color(0xFFF3E8FF),          // Lavender Card
        Color(0xFFE9D5FF),          // Soft Violet Star
        Color(0xFF5B21B6)           // Cosmos Purple label
    ),
    // 4 NYENTRIK (ECCENTRIC) THEMES
    FUNKY_NEON(
        "Funky Neon",
        Color(0xFF25002C),
        true,
        Color(0xFFADFF2F),          // Green/Lime
        Color(0xFFFF1493),          // Deep Pink
        Color(0xFF4A0E4E),          // Violet neon
        Color(0xFF1F0024),          // Ultra Dark Violet
        Color(0xFFADFF2F)
    ),
    LAVA_VOLCANO(
        "Lava Volcano",
        Color(0xFF0F0300),
        true,
        Color(0xFFFF4500),          // OrangeRed
        Color(0xFFFFD700),          // Gold
        Color(0xFF330A00),          // Burning charcoal start
        Color(0xFF110300),          // Ash end
        Color(0xFFFFA07A)
    ),
    GLITCH_MATRIX(
        "Glitch Matrix",
        Color(0xFF000F05),
        true,
        Color(0xFF00FF41),          // Matrix green
        Color(0xFF39FF14),          // Neon green
        Color(0xFF00220A),          // Deep terminal green
        Color(0xFF000803),          // Shell screen
        Color(0xFF00FF41)
    ),
    CANDY_LAND(
        "Candy Land",
        Color(0xFFFFF0F5),          // LavenderBlush
        false,
        Color(0xFF00F5FF),          // Deep Turquoise
        Color(0xFFFF69B4),          // HotPink
        Color(0xFFE0FFFF),          // LightCyan
        Color(0xFFFFD1DC),          // BabyPink
        Color(0xFFFF1493)
    ),
    // 4 GOOGLE-STYLE THEMES
    GOOGLE_CLASSIC(
        "Google Classic",
        Color(0xFFFFFFFF),
        false,
        Color(0xFF1A73E8),          // Google Product Blue
        Color(0xFF34A853),          // Google Green
        Color(0xFFE8F0FE),          // Soft Blue Card
        Color(0xFFF1F3F4),          // Cool Grey
        Color(0xFF5F6368)
    ),
    GOOGLE_DARK(
        "Google Dark",
        Color(0xFF202124),
        true,
        Color(0xFF8AB4F8),          // Google Light Blue
        Color(0xFF81C995),          // Google Light Green
        Color(0xFF303134),          // Charcoal grey card
        Color(0xFF202124),          // Dark background
        Color(0xFFE8EAED)
    ),
    GOOGLE_PIXEL(
        "Google Pixel",
        Color(0xFFF8F9FA),
        false,
        Color(0xFF1967D2),          // Rich Pixel Blue
        Color(0xFFEA4335),          // Google Red
        Color(0xFFE8F0FE),          // Slate blue-hued card
        Color(0xFFF8F9FA),          // Light gray
        Color(0xFF202124)
    ),
    GOOGLE_WORK(
        "Google Work",
        Color(0xFFE8F0FE),
        false,
        Color(0xFF00796B),          // Material Teal
        Color(0xFFE040FB),          // Fuchsia accent
        Color(0xFFE0F2F1),          // Minty blue card start
        Color(0xFFE0F7FA),          // Soft teal card end
        Color(0xFF004D40)
    )
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("liquid_finance_scaffold"),
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPadding ->
                    LiquidFinanceDashboard(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}

// ==========================================
// THOUSANDS DOT FORMATTERS & PARSERS
// ==========================================
fun formatCurrency(amount: Double): String {
    val formatter = DecimalFormat("#,###")
    val symbols = formatter.decimalFormatSymbols
    symbols.groupingSeparator = '.'
    symbols.monetaryDecimalSeparator = ','
    formatter.decimalFormatSymbols = symbols
    return "Rp ${formatter.format(amount)}"
}

fun formatShortCurrency(amount: Double): String {
    val formatter = DecimalFormat("#,###")
    val symbols = formatter.decimalFormatSymbols
    symbols.groupingSeparator = '.'
    symbols.monetaryDecimalSeparator = ','
    formatter.decimalFormatSymbols = symbols
    return "Rp ${formatter.format(amount)}"
}

fun formatInputString(input: String): String {
    val clean = input.replace(Regex("[^\\d]"), "")
    if (clean.isEmpty()) return ""
    return try {
        val parsed = clean.toLong()
        val formatter = DecimalFormat("#,###")
        val symbols = formatter.decimalFormatSymbols
        symbols.groupingSeparator = '.'
        formatter.decimalFormatSymbols = symbols
        formatter.format(parsed)
    } catch (e: Exception) {
        clean
    }
}

fun parseInputToDouble(formattedInput: String): Double {
    val clean = formattedInput.replace(".", "")
    return clean.toDoubleOrNull() ?: 0.0
}

fun getCategoryIcon(category: String): ImageVector {
    return when (category.lowercase()) {
        "gaji" -> Icons.Filled.AttachMoney
        "investasi" -> Icons.Filled.TrendingUp
        "rumah" -> Icons.Filled.Home
        "belanja" -> Icons.Filled.ShoppingCart
        "makanan" -> Icons.Filled.Fastfood
        "freelance" -> Icons.Filled.Stars
        "transport" -> Icons.Filled.DirectionsCar
        "hiburan" -> Icons.Filled.Celebration
        "lainnya" -> Icons.Filled.Category
        else -> Icons.Filled.Category
    }
}

// ==========================================
// ANIMATED MONEY TEXT COUNTER
// ==========================================
@Composable
fun AnimatedMoneyText(
    amount: Double,
    style: TextStyle,
    color: Color,
    fontWeight: FontWeight = FontWeight.Bold,
    modifier: Modifier = Modifier
) {
    val animatedValue by animateFloatAsState(
        targetValue = amount.toFloat(),
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "MoneyCounterAnimator"
    )
    Text(
        text = formatCurrency(animatedValue.toDouble()),
        style = style,
        color = color,
        fontWeight = fontWeight,
        modifier = modifier,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

// ==========================================
// CORE GLASS COMPOSABLE COMPONENTS
// ==========================================
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    containerColor: Color = GlassBgLight,
    borderColor: Color = GlassBorderLight,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    var combinedModifier = modifier
        .clip(RoundedCornerShape(24.dp))
        .background(containerColor)
        
    if (onClick != null) {
        combinedModifier = combinedModifier.clickable(
            onClick = onClick,
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple()
        )
    }

    Box(
        modifier = combinedModifier
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        borderColor.copy(alpha = 0.5f),
                        borderColor.copy(alpha = 0.05f)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(24.dp)
    ) {
        Column {
            content()
        }
    }
}

// ==========================================
// FINANCIAL STATE ENGINE
// ==========================================
data class FinancialCondition(
    val text: String,
    val emoji: String,
    val color: Color,
    val headerBackground: Brush
)

@Composable
fun calculateFinancialCondition(totalIncome: Double, totalExpense: Double, totalBalance: Double, theme: CustomBgTheme): FinancialCondition {
    return if (totalBalance < 0.0) {
        // 1. Negative Balance ("Woiii kok minessss" -> Red gradient, white/red text)
        FinancialCondition(
            text = "woiii kok minessss",
            emoji = "😡",
            color = ColorNegativeRed,
            headerBackground = Brush.verticalGradient(
                colors = listOf(
                    GlossyHeaderRed,
                    GlossyHeaderRed.copy(alpha = 0.85f)
                )
            )
        )
    } else {
        // 2. Zero or Positive Balance -> Suitable colors from selected theme!
        FinancialCondition(
            text = if (totalIncome == 0.0 && totalExpense == 0.0) "Mulai Catat Transaksi!" else "Kondisi Finansial Sehat",
            emoji = if (totalIncome == 0.0 && totalExpense == 0.0) "🌱" else "😊",
            color = theme.primaryColor,
            headerBackground = Brush.verticalGradient(
                colors = listOf(
                    theme.positiveCardStart,
                    theme.positiveCardEnd
                )
            )
        )
    }
}

// ==========================================
// MAIN DASHBOARD LAYOUT
// ==========================================
@Composable
fun LiquidFinanceDashboard(
    modifier: Modifier = Modifier,
    viewModel: LiquidFinanceViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    
    // Dynamic Preselection default type
    var currentAddType by remember { mutableStateOf("EXPENSE") }

    // Dynamic period filter range
    var selectedTimeFilter by remember { mutableStateOf("Harian") }

    // Custom background customizer state
    var selectedBgTheme by remember { mutableStateOf(CustomBgTheme.DEFAULT) }
    var showBgCustomizer by remember { mutableStateOf(false) }
    var selectedFont by remember { mutableStateOf(AppFont.DEFAULT) }
    var catcoyClickCount by remember { mutableStateOf(0) }
    var showPianoScreen by remember { mutableStateOf(false) }

    val animBgColor by animateColorAsState(
        targetValue = selectedBgTheme.bgColor,
        animationSpec = tween(600, easing = LinearOutSlowInEasing),
        label = "BgColorAnimation"
    )

    val animPrimaryColor by animateColorAsState(
        targetValue = selectedBgTheme.primaryColor,
        animationSpec = tween(600, easing = LinearOutSlowInEasing),
        label = "PrimaryColorAnimation"
    )

    val animExpenseColor by animateColorAsState(
        targetValue = selectedBgTheme.expenseColor,
        animationSpec = tween(600, easing = LinearOutSlowInEasing),
        label = "ExpenseColorAnimation"
    )

    val animIncomeCardStart by animateColorAsState(
        targetValue = if (selectedBgTheme.isDark) Color(0xFF00302D) else selectedBgTheme.primaryColor.copy(alpha = 0.08f),
        animationSpec = tween(600),
        label = "IncomeCardStartAnimation"
    )
    val animIncomeCardEnd by animateColorAsState(
        targetValue = if (selectedBgTheme.isDark) Color(0xFF000F0E) else selectedBgTheme.primaryColor.copy(alpha = 0.03f),
        animationSpec = tween(600),
        label = "IncomeCardEndAnimation"
    )
    val animExpenseCardStart by animateColorAsState(
        targetValue = if (selectedBgTheme.isDark) Color(0xFF2E0914) else selectedBgTheme.expenseColor.copy(alpha = 0.08f),
        animationSpec = tween(600),
        label = "ExpenseCardStartAnimation"
    )
    val animExpenseCardEnd by animateColorAsState(
        targetValue = if (selectedBgTheme.isDark) Color(0xFF0F0206) else selectedBgTheme.expenseColor.copy(alpha = 0.03f),
        animationSpec = tween(600),
        label = "ExpenseCardEndAnimation"
    )

    // Dynamic typography colors based on selected background theme
    val dynamicTextColor = if (selectedBgTheme.isDark) Color.White else Color(0xFF1B2430)
    val dynamicSubTextColor = if (selectedBgTheme.isDark) Color.White.copy(alpha = 0.5f) else Color(0xFF525F71)
    val dynamicSubTextColor60 = if (selectedBgTheme.isDark) Color.White.copy(alpha = 0.6f) else Color(0xFF525F71).copy(alpha = 0.8f)

    val aggregator = remember(uiState.transactions, selectedTimeFilter) {
        val now = System.currentTimeMillis()
        val oneDay = 86400000L
        
        when (selectedTimeFilter) {
            "Harian" -> {
                // Get all transactions in the last 24 hours
                val twentyFourHoursAgo = now - 24 * 3600000L
                val last24hTransactions = uiState.transactions
                    .filter { it.timestamp >= twentyFourHoursAgo }
                
                if (last24hTransactions.isEmpty()) {
                    listOf(RecapItem("No Data", 0.0, 0.0))
                } else {
                    // Group by Hour formatted as HH:00 or HH.00
                    val sdfLabel = SimpleDateFormat("HH:00", Locale.getDefault())
                    val grouped = last24hTransactions.groupBy { 
                        sdfLabel.format(Date(it.timestamp))
                    }
                    
                    grouped.map { (hourLabel, txs) ->
                        // Sort by chronological timestamp of first transaction in the group
                        val firstTimestamp = txs.minOfOrNull { it.timestamp } ?: 0L
                        val inc = txs.filter { it.type == "INCOME" }.sumOf { it.amount }
                        val exp = txs.filter { it.type == "EXPENSE" }.sumOf { it.amount }
                        Triple(firstTimestamp, hourLabel, Pair(inc, exp))
                    }
                    .sortedBy { it.first }
                    .map { (_, hourLabel, values) ->
                        RecapItem(label = hourLabel, income = values.first, expense = values.second)
                    }
                }
            }
            "Mingguan" -> {
                // Mingguan: Catatan pengeluaran & pemasukan 7 hari terakhir
                val sdf = SimpleDateFormat("EE", Locale.getDefault())
                (0..6).reversed().map { d ->
                    val start = now - (d * oneDay)
                    val dayLabel = sdf.format(Date(start))
                    
                    val startOfDay = getStartOfDay(start)
                    val endOfDay = startOfDay + oneDay
                    val txs = uiState.transactions.filter { it.timestamp in startOfDay until endOfDay }
                    
                    val inc = txs.filter { it.type == "INCOME" }.sumOf { it.amount }
                    val exp = txs.filter { it.type == "EXPENSE" }.sumOf { it.amount }
                    RecapItem(label = dayLabel, income = inc, expense = exp)
                }
            }
            else -> {
                // Bulanan: Catatan pengeluaran selama 12 bulan ini
                val sdf = SimpleDateFormat("MMM", Locale.getDefault())
                val cal = Calendar.getInstance()
                (0..11).reversed().map { m ->
                    cal.timeInMillis = now
                    cal.add(Calendar.MONTH, -m)
                    val monthLabel = sdf.format(cal.time)
                    
                    val startOfMonth = cal.apply {
                        set(Calendar.DAY_OF_MONTH, 1)
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }.timeInMillis
                    
                    cal.add(Calendar.MONTH, 1)
                    val endOfMonth = cal.timeInMillis
                    
                    val txs = uiState.transactions.filter { it.timestamp in startOfMonth until endOfMonth }
                    val inc = txs.filter { it.type == "INCOME" }.sumOf { it.amount }
                    val exp = txs.filter { it.type == "EXPENSE" }.sumOf { it.amount }
                    RecapItem(label = monthLabel, income = inc, expense = exp)
                }
            }
        }
    }

    val pagerState = rememberPagerState(initialPage = 0) { 2 }
    val scope = rememberCoroutineScope()
    var showCalculator by remember { mutableStateOf(false) }

    CompositionLocalProvider(LocalAppFont provides selectedFont) {
        // 1. Premium Dark Fluid Glass Ambient Background Layer with smooth transition
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(animBgColor) // Dynamic smooth anim
                .drawBehind {
                val alphaMultiplier = if (selectedBgTheme.isDark) 1f else 0.45f
                
                when (selectedBgTheme) {
                    CustomBgTheme.CYBERPUNK, CustomBgTheme.RETRO_ARCADE, CustomBgTheme.GLITCH_MATRIX, CustomBgTheme.FUNKY_NEON -> {
                        // Cyber grid / glitch shapes
                        val beamColor = animPrimaryColor.copy(alpha = 0.15f * alphaMultiplier)
                        val accentColor = animExpenseColor.copy(alpha = 0.12f * alphaMultiplier)
                        
                        // Cyber grid lines
                        val numGridLines = 15
                        for (i in 0..numGridLines) {
                            val hz = size.height * (i.toFloat() / numGridLines)
                            drawLine(
                                color = beamColor.copy(alpha = beamColor.alpha * 0.4f),
                                start = Offset(0f, hz),
                                end = Offset(size.width, hz),
                                strokeWidth = 1.5f
                            )
                            val vt = size.width * (i.toFloat() / numGridLines)
                            drawLine(
                                color = beamColor.copy(alpha = beamColor.alpha * 0.4f),
                                start = Offset(vt, 0f),
                                end = Offset(vt, size.height),
                                strokeWidth = 1.5f
                            )
                        }
                        
                        // Overlapping neon glowing rectangles
                        drawRect(
                            color = beamColor,
                            topLeft = Offset(size.width * 0.62f, size.height * 0.08f),
                            size = Size(size.width * 0.35f, size.width * 0.35f)
                        )
                        drawRect(
                            color = accentColor,
                            topLeft = Offset(size.width * 0.05f, size.height * 0.75f),
                            size = Size(size.width * 0.45f, size.width * 0.45f)
                        )
                    }
                    CustomBgTheme.SAKURA, CustomBgTheme.PINK, CustomBgTheme.CANDY_LAND -> {
                        // Soft sweet flowery, hearts, and candy bubbles
                        val petalColor1 = animPrimaryColor.copy(alpha = 0.2f * alphaMultiplier)
                        val petalColor2 = animExpenseColor.copy(alpha = 0.15f * alphaMultiplier)
                        
                        // Draw overlapping large flower petal outlines or organic blobs
                        val petalPath1 = Path().apply {
                            moveTo(size.width * 0.5f, size.height * 0.1f)
                            cubicTo(size.width * 0.9f, 0f, size.width, size.height * 0.3f, size.width * 0.7f, size.height * 0.4f)
                            cubicTo(size.width * 0.4f, size.height * 0.5f, size.width * 0.2f, size.height * 0.2f, size.width * 0.5f, size.height * 0.1f)
                        }
                        drawPath(path = petalPath1, color = petalColor1)
                        
                        val petalPath2 = Path().apply {
                            moveTo(size.width * 0.5f, size.height * 0.9f)
                            cubicTo(0f, size.height * 0.9f, 0f, size.height * 0.6f, size.width * 0.3f, size.height * 0.5f)
                            cubicTo(size.width * 0.6f, size.height * 0.4f, size.width * 0.8f, size.height * 0.8f, size.width * 0.5f, size.height * 0.9f)
                        }
                        drawPath(path = petalPath2, color = petalColor2)
                    }
                    CustomBgTheme.MIDNIGHT_FOREST, CustomBgTheme.HIJAU_MINT, CustomBgTheme.CREAM -> {
                        // Leafy/organic wave curves
                        val colorBio1 = animPrimaryColor.copy(alpha = 0.18f * alphaMultiplier)
                        val colorBio2 = animExpenseColor.copy(alpha = 0.12f * alphaMultiplier)
                        
                        // Wave 1 at top
                        val pathWave1 = Path().apply {
                            moveTo(0f, 0f)
                            lineTo(size.width, 0f)
                            lineTo(size.width, size.height * 0.32f)
                            quadraticTo(size.width * 0.5f, size.height * 0.18f, 0f, size.height * 0.28f)
                            close()
                        }
                        drawPath(path = pathWave1, color = colorBio1)
                        
                        // Wave 2 at bottom
                        val pathWave2 = Path().apply {
                            moveTo(0f, size.height)
                            lineTo(size.width, size.height)
                            lineTo(size.width, size.height * 0.75f)
                            quadraticTo(size.width * 0.65f, size.height * 0.88f, 0f, size.height * 0.78f)
                            close()
                        }
                        drawPath(path = pathWave2, color = colorBio2)
                    }
                    CustomBgTheme.UNGU_SENJA, CustomBgTheme.COSMIC_LAVENDER -> {
                        // Stars and starry sparkles, sweeping nebula paths
                        val starColor = animPrimaryColor.copy(alpha = 0.25f * alphaMultiplier)
                        val glowColor = animExpenseColor.copy(alpha = 0.15f * alphaMultiplier)
                        
                        // Sweeping nebula trail
                        val trailPath = Path().apply {
                            moveTo(0f, size.height * 0.2f)
                            cubicTo(size.width * 0.4f, size.height * 0.1f, size.width * 0.6f, size.height * 0.5f, size.width, size.height * 0.35f)
                            lineTo(size.width, size.height * 0.45f)
                            cubicTo(size.width * 0.6f, size.height * 0.6f, size.width * 0.4f, size.height * 0.2f, 0f, size.height * 0.3f)
                            close()
                        }
                        drawPath(path = trailPath, brush = Brush.linearGradient(listOf(starColor, Color.Transparent)))
                        
                        // Procedural custom 4-pointed stars
                        fun DrawScope.drawFourPointStar(cx: Float, cy: Float, r: Float, col: Color) {
                            val star = Path().apply {
                                moveTo(cx, cy - r)
                                quadraticTo(cx, cy, cx + r, cy)
                                quadraticTo(cx, cy, cx, cy + r)
                                quadraticTo(cx, cy, cx - r, cy)
                                quadraticTo(cx, cy, cx, cy - r)
                            }
                            drawPath(path = star, color = col)
                        }
                        
                        drawFourPointStar(size.width * 0.82f, size.height * 0.12f, size.width * 0.08f, starColor)
                        drawFourPointStar(size.width * 0.2f, size.height * 0.78f, size.width * 0.06f, glowColor)
                        drawFourPointStar(size.width * 0.15f, size.height * 0.25f, size.width * 0.04f, starColor.copy(alpha = 0.15f))
                        drawFourPointStar(size.width * 0.88f, size.height * 0.68f, size.width * 0.05f, glowColor.copy(alpha = 0.15f))
                    }
                    CustomBgTheme.LAVA_VOLCANO -> {
                        // Volcanic cracks and jagged plates
                        val plateColor = animPrimaryColor.copy(alpha = 0.16f * alphaMultiplier)
                        val magmaGlow = animExpenseColor.copy(alpha = 0.22f * alphaMultiplier)
                        
                        // Jagged plate at top-right
                        val pathPlate1 = Path().apply {
                            moveTo(size.width, 0f)
                            lineTo(size.width * 0.45f, 0f)
                            lineTo(size.width * 0.65f, size.height * 0.18f)
                            lineTo(size.width, size.height * 0.28f)
                            close()
                        }
                        drawPath(path = pathPlate1, color = plateColor)
                        
                        // Jagged magma crack across the screen
                        val pathCrack = Path().apply {
                            moveTo(0f, size.height * 0.7f)
                            lineTo(size.width * 0.35f, size.height * 0.65f)
                            lineTo(size.width * 0.55f, size.height * 0.82f)
                            lineTo(size.width, size.height * 0.78f)
                            lineTo(size.width, size.height * 0.85f)
                            lineTo(size.width * 0.52f, size.height * 0.88f)
                            lineTo(size.width * 0.32f, size.height * 0.72f)
                            lineTo(0f, size.height * 0.78f)
                            close()
                        }
                        drawPath(path = pathCrack, color = magmaGlow)
                    }
                    else -> {
                        // DEFAULT, PUTIH, GOOGLE_CLASSIC, GOOGLE_DARK, GOOGLE_PIXEL, GOOGLE_WORK
                        // Elegant overlapping rounded rectangles & curved planes for modern material feel
                        val cardColor1 = animPrimaryColor.copy(alpha = 0.18f * alphaMultiplier)
                        val cardColor2 = animExpenseColor.copy(alpha = 0.12f * alphaMultiplier)
                        
                        // Overlapping diagonal soft pill shapes
                        val pathPill1 = Path().apply {
                            moveTo(size.width * 0.6f, -50f)
                            lineTo(size.width * 1.2f, size.height * 0.25f)
                            lineTo(size.width * 0.9f, size.height * 0.35f)
                            lineTo(size.width * 0.3f, 50f)
                            close()
                        }
                        drawPath(path = pathPill1, color = cardColor1)
                        
                        val pathPill2 = Path().apply {
                            moveTo(-50f, size.height * 0.7f)
                            lineTo(size.width * 0.6f, size.height * 0.55f)
                            lineTo(size.width * 0.8f, size.height * 0.85f)
                            lineTo(size.width * 0.2f, size.height + 50f)
                            close()
                        }
                        drawPath(path = pathPill2, color = cardColor2)
                    }
                }
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(28.dp))

            // ==========================================
            // HEADER BAR: clickable "10" for background customization
            // ==========================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Day block bubble "10" (customizer trigger)
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (selectedBgTheme.isDark) Color.White.copy(alpha = 0.08f) else Color.Black.copy(alpha = 0.08f))
                        .border(
                            1.dp, 
                            if (selectedBgTheme.isDark) Color.White.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.15f), 
                            RoundedCornerShape(14.dp)
                        )
                        .clickable {
                            PopSoundPlayer.play()
                            showBgCustomizer = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "10",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = dynamicTextColor
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Text(
                    text = "catatcoy",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp),
                    fontWeight = FontWeight.Bold,
                    color = dynamicTextColor,
                    modifier = Modifier.clickable {
                        catcoyClickCount++
                        if (catcoyClickCount >= 10) {
                            catcoyClickCount = 0
                            showPianoScreen = true
                            PopSoundPlayer.play()
                        } else {
                            PopSoundPlayer.play()
                        }
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                // Interactive Heterochromia 3D Cat Button (clicks play pop sound + meow synthesizer)
                HeterochromiaCatButton {
                    // Sounds are played internally dynamically mirroring the active facial expression
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ==========================================
            // BEAUTIFUL SWITCHER TABS FOR SWIPING
            // ==========================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (selectedBgTheme.isDark) Color.White.copy(alpha = 0.05f) else Color.Black.copy(alpha = 0.05f))
                    .border(
                        1.dp, 
                        if (selectedBgTheme.isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.08f), 
                        RoundedCornerShape(14.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // UTAMA tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (pagerState.currentPage == 0) animPrimaryColor.copy(alpha = 0.35f) else Color.Transparent)
                        .clickable {
                            PopSoundPlayer.play()
                            scope.launch { pagerState.animateScrollToPage(0) }
                        }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "BUKU UTAMA",
                        style = LiquidLabelCaps.copy(fontSize = 11.sp, fontWeight = FontWeight.Bold),
                        color = dynamicTextColor
                    )
                }
                
                // NOTED tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (pagerState.currentPage == 1) animPrimaryColor.copy(alpha = 0.35f) else Color.Transparent)
                        .clickable {
                            PopSoundPlayer.play()
                            scope.launch { pagerState.animateScrollToPage(1) }
                        }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "NOTED",
                        style = LiquidLabelCaps.copy(fontSize = 11.sp, fontWeight = FontWeight.Bold),
                        color = dynamicTextColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                if (page == 0) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(horizontal = 24.dp)
                    ) {
                        Spacer(modifier = Modifier.height(12.dp))

            // ==========================================
            // PRINCIPAL RESPONSIVE HEADER CARD
            // ==========================================
            val condition = calculateFinancialCondition(
                totalIncome = uiState.totalIncome,
                totalExpense = uiState.totalExpense,
                totalBalance = uiState.totalBalance,
                theme = selectedBgTheme
            )
            
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(condition.headerBackground, shape = RoundedCornerShape(24.dp))
                    .testTag("balance_card"),
                containerColor = Color.Transparent, // transparent as we draw background brush
                borderColor = Color.White.copy(alpha = 0.15f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "SALDO SEKARANG (NET)",
                            style = LiquidLabelCaps.copy(fontSize = 11.sp),
                            color = Color.White.copy(alpha = 0.6f),
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Animated Balance with appropriate color!
                        AnimatedMoneyText(
                            amount = uiState.totalBalance,
                            style = LiquidDisplayLarge.copy(fontSize = 32.sp),
                            color = condition.color,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(condition.color)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                  text = condition.text,
                                  style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                  color = condition.color
                            )
                        }
                    }
                    
                    // Adaptive emoji container
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color.White.copy(alpha = 0.12f))
                            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(18.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = condition.emoji,
                            fontSize = 32.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ==========================================
            // COMPARISON SIDE-BY-SIDE CARDS
            // ==========================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // LEFT CARD: Pengeluaran (with beautiful soft red vertical gradient)
                GlassCard(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("expense_summary_card")
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(animExpenseCardStart, animExpenseCardEnd)
                            ),
                            shape = RoundedCornerShape(24.dp)
                        ),
                    containerColor = Color.Transparent,
                    borderColor = animExpenseColor.copy(alpha = 0.25f)
                ) {
                    Column(modifier = Modifier.padding(2.dp)) {
                        Text(
                            text = "Pengeluaran",
                            style = MaterialTheme.typography.bodyMedium,
                            color = animExpenseColor.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        AnimatedMoneyText(
                            amount = uiState.totalExpense,
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                            color = animExpenseColor,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        val expenseProgress = if (uiState.totalIncome > 0.0) {
                            (uiState.totalExpense / uiState.totalIncome).toFloat().coerceIn(0f, 1f)
                        } else if (uiState.totalExpense > 0.0) 1f else 0f
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.1f))
                        ) {
                            if (expenseProgress > 0f) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(expenseProgress)
                                        .clip(CircleShape)
                                        .background(animExpenseColor)
                                )
                            }
                        }
                    }
                }

                // RIGHT CARD: Pemasukan (with beautiful soft green-teal / toska gradient and toska text)
                GlassCard(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("income_summary_card")
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(animIncomeCardStart, animIncomeCardEnd)
                            ),
                            shape = RoundedCornerShape(24.dp)
                        ),
                    containerColor = Color.Transparent,
                    borderColor = animPrimaryColor.copy(alpha = 0.25f)
                ) {
                    Column(modifier = Modifier.padding(2.dp)) {
                        Text(
                            text = "Pemasukan",
                            style = MaterialTheme.typography.bodyMedium,
                            color = animPrimaryColor.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        AnimatedMoneyText(
                            amount = uiState.totalIncome,
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                            color = animPrimaryColor,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        val incomeProgress = if (uiState.totalIncome > 0.0) 0.85f else 0.0f
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.1f))
                        ) {
                            if (incomeProgress > 0f) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(incomeProgress)
                                        .clip(CircleShape)
                                        .background(animPrimaryColor)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ==========================================
            // CAPSULE CHIP ROW & SCALED PDF ACTION (plus removed)
            // ==========================================
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                containerColor = if (selectedBgTheme.isDark) Color.White.copy(alpha = 0.05f) else Color.Black.copy(alpha = 0.05f),
                borderColor = if (selectedBgTheme.isDark) Color.White.copy(alpha = 0.12f) else Color.Black.copy(alpha = 0.12f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf("Harian", "Mingguan", "Bulanan").forEach { label ->
                            val isSelected = selectedTimeFilter == label
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (isSelected) animPrimaryColor.copy(alpha = 0.15f) else Color.Transparent)
                                    .clickable { 
                                        PopSoundPlayer.play()
                                        selectedTimeFilter = label 
                                    }
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                  Text(
                                      text = label,
                                      style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                                      color = if (isSelected) animPrimaryColor else dynamicSubTextColor,
                                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                  )
                            }
                        }
                    }

                    // Enlarge PDF export icon for perfect visual balance
                    Box(
                        modifier = Modifier
                            .height(38.dp)
                            .width(82.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(animPrimaryColor.copy(alpha = 0.15f))
                            .border(1.dp, animPrimaryColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .clickable { 
                                PopSoundPlayer.play()
                                exportAndShareFinancialPdf(
                                    context = context,
                                    timeFilter = selectedTimeFilter,
                                    transactions = uiState.transactions,
                                    recapItems = aggregator
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "PDF Report",
                                tint = animPrimaryColor,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                  text = "PDF",
                                  fontSize = 11.sp,
                                  fontWeight = FontWeight.Bold,
                                  color = animPrimaryColor
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ==========================================
            // QUICK ACTION PRESELECTION DOUBLE BUTTONS
            // ==========================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // + Catat Pemasukan
                Button(
                    onClick = {
                        PopSoundPlayer.play()
                        currentAddType = "INCOME"
                        viewModel.setTransactionDialogVisible(true)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .border(1.dp, animPrimaryColor.copy(alpha = 0.25f), RoundedCornerShape(14.dp)),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = animPrimaryColor.copy(alpha = 0.15f),
                        contentColor = animPrimaryColor
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        text = "+ Catat Pemasukan",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                // + Catat Pengeluaran
                Button(
                    onClick = {
                        PopSoundPlayer.play()
                        currentAddType = "EXPENSE"
                        viewModel.setTransactionDialogVisible(true)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .border(1.dp, animExpenseColor.copy(alpha = 0.25f), RoundedCornerShape(14.dp)),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = animExpenseColor.copy(alpha = 0.15f),
                        contentColor = animExpenseColor
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        text = "+ Catat Pengeluaran",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ==========================================
            // GRAPH: RECAP OF FINANCIALS (DYNAMIC DATA)
            // ==========================================
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                containerColor = if (selectedBgTheme.isDark) Color.White.copy(alpha = 0.06f) else Color.Black.copy(alpha = 0.04f),
                borderColor = if (selectedBgTheme.isDark) Color.White.copy(alpha = 0.12f) else Color.Black.copy(alpha = 0.12f)
            ) {
                Column(modifier = Modifier.padding(2.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = when (selectedTimeFilter) {
                                "Harian" -> "Recap 24 Jam Terakhir"
                                "Mingguan" -> "Recap 7 Hari Terakhir"
                                else -> "Recap 12 Bulan Terakhir"
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = dynamicTextColor
                        )
                        
                        Text(
                            text = when (selectedTimeFilter) {
                                "Harian" -> "24 Jam"
                                "Mingguan" -> "7 Hari"
                                else -> "12 Bulan"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = dynamicSubTextColor
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = when (selectedTimeFilter) {
                            "Harian" -> "Rekap pengeluaran dan pemasukan 24 jam terakhir."
                            "Mingguan" -> "Analisis perputaran kas 7 hari terakhir."
                            else -> "Arus pengeluaran & pemasukan 12 bulan terakhir."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = dynamicSubTextColor
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Bar Chart - Fully Dynamic Heights based on aggregated transactions!
                    val maxVal = maxOf(aggregator.maxOfOrNull { maxOf(it.income, it.expense) } ?: 0.0, 1.0)
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        aggregator.forEach { item ->
                            val incPct = (item.income / maxVal).toFloat().coerceIn(0f, 1f)
                            val expPct = (item.expense / maxVal).toFloat().coerceIn(0f, 1f)
                            
                            val incHeight = (incPct * 100).coerceAtLeast(4f).dp
                            val expHeight = (expPct * 100).coerceAtLeast(4f).dp

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.Bottom,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .width(6.dp)
                                            .height(incHeight)
                                            .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
                                            .background(animPrimaryColor)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .width(6.dp)
                                            .height(expHeight)
                                            .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
                                            .background(animExpenseColor)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = item.label,
                                    fontSize = 11.sp,
                                    color = dynamicSubTextColor60,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(animPrimaryColor)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Pemasukan",
                            fontSize = 12.sp,
                            color = dynamicTextColor,
                            fontWeight = FontWeight.Medium
                        )
                        
                        Spacer(modifier = Modifier.width(20.dp))
                        
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(animExpenseColor)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Pengeluaran",
                            fontSize = 12.sp,
                            color = dynamicTextColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ==========================================
            // TRANSACTION HISTORIC LIST
            // ==========================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "REKAP CATATAN COY",
                    style = LiquidLabelCaps,
                    color = dynamicTextColor
                )

                // Small filter badge button
                Text(
                    text = "Klik Transaksi Untuk Edit",
                    style = MaterialTheme.typography.labelSmall,
                    color = animPrimaryColor,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (uiState.filteredTransactions.isEmpty()) {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp)
                ) {
                    Text(
                        text = "Tidak menemukan transaksi sesuai filter.",
                        style = LiquidBodyMedium,
                        color = dynamicSubTextColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp)
                    )
                }
            } else {
                uiState.filteredTransactions.forEachIndexed { index, tx ->
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .testTag("transaction_item_${index}"),
                        containerColor = if (selectedBgTheme.isDark) GlassBgLight else Color.Black.copy(alpha = 0.04f),
                        borderColor = if (selectedBgTheme.isDark) GlassBorderLight else Color.Black.copy(alpha = 0.08f),
                        onClick = { 
                            PopSoundPlayer.play()
                            viewModel.setEditingTransaction(tx) 
                        } // Open edit dialog on click!
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                             ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (tx.type == "INCOME") animPrimaryColor.copy(alpha = 0.12f)
                                            else animExpenseColor.copy(alpha = 0.12f)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = getCategoryIcon(tx.category),
                                        contentDescription = tx.category,
                                        tint = if (tx.type == "INCOME") animPrimaryColor else animExpenseColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = tx.title,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = dynamicTextColor,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${tx.category} • ${SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date(tx.timestamp))}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = dynamicSubTextColor
                                    )
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(
                                    text = if (tx.type == "INCOME") "+ ${formatCurrency(tx.amount)}" else "- ${formatCurrency(tx.amount)}",
                                    style = LiquidLabelCaps.copy(fontSize = 14.sp),
                                    color = if (tx.type == "INCOME") animPrimaryColor else animExpenseColor,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                // Edit Button Explicitly next to delete button
                                IconButton(
                                    onClick = { 
                                        PopSoundPlayer.play()
                                        viewModel.setEditingTransaction(tx) 
                                    },
                                    modifier = Modifier.size(34.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit Transaksi",
                                        tint = animPrimaryColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                // Delete Button
                                IconButton(
                                    onClick = { 
                                        PopSoundPlayer.play()
                                        viewModel.deleteTransaction(tx) 
                                    },
                                    modifier = Modifier.size(34.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Hapus Transaksi Buku",
                                        tint = animExpenseColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
                } else {
                    NotedDashboard(
                        selectedBgTheme = selectedBgTheme,
                        animPrimaryColor = animPrimaryColor,
                        animExpenseColor = animExpenseColor,
                        dynamicTextColor = dynamicTextColor,
                        dynamicSubTextColor = dynamicSubTextColor,
                        dynamicSubTextColor60 = dynamicSubTextColor60,
                        viewModel = viewModel,
                        uiState = uiState
                    )
                }
            }
        }

        // ==========================================
        // DIALOG: THEME / BACKGROUND CUSTOMIZER
        // ==========================================
        if (showBgCustomizer) {
            BackgroundCustomizerDialog(
                selectedTheme = selectedBgTheme,
                onThemeSelect = { selectedBgTheme = it },
                selectedFont = selectedFont,
                onFontSelect = { selectedFont = it },
                onDismiss = { showBgCustomizer = false }
            )
        }

        // ==========================================
        // DIALOG: ADD TRANSACTION SPEC
        // ==========================================
        if (uiState.showAddTransactionDialog) {
            AddTransactionDialog(
                defaultType = currentAddType,
                categoriesList = uiState.categories,
                onAddCategory = { name, type -> viewModel.addCategory(name, type) },
                onDeleteCategory = { cat -> viewModel.deleteCategory(cat) },
                onDismiss = { viewModel.setTransactionDialogVisible(false) },
                onSubmit = { id, title, amount, type, category, notes ->
                    viewModel.addTransaction(title, amount, type, category, notes)
                    viewModel.setTransactionDialogVisible(false)
                }
            )
        }

        // ==========================================
        // DIALOG: EDIT TRANSACTION SPEC (Dynamic update!)
        // ==========================================
        if (uiState.editingTransaction != null) {
            AddTransactionDialog(
                transaction = uiState.editingTransaction,
                categoriesList = uiState.categories,
                onAddCategory = { name, type -> viewModel.addCategory(name, type) },
                onDeleteCategory = { cat -> viewModel.deleteCategory(cat) },
                onDismiss = { viewModel.setEditingTransaction(null) },
                onSubmit = { id, title, amount, type, category, notes ->
                    val updated = uiState.editingTransaction!!.copy(
                        id = id,
                        title = title,
                        amount = amount,
                        type = type,
                        category = category,
                        notes = notes
                    )
                    viewModel.updateTransaction(updated)
                    viewModel.setEditingTransaction(null)
                }
            )
        }

        // ==========================================
        // DIALOG: ADD BUDGET LIMIT
        // ==========================================
        if (uiState.showAddBudgetDialog) {
            AddBudgetDialog(
                categoriesList = uiState.categories,
                onDismiss = { viewModel.setBudgetDialogVisible(false) },
                onSubmit = { category, limit ->
                    viewModel.addBudget(category, limit)
                    viewModel.setBudgetDialogVisible(false)
                }
            )
        }

        // ==========================================
        // DIALOG: ADD SAVINGS GOAL
        // ==========================================
        if (uiState.showAddSavingsGoalDialog) {
            AddSavingsGoalDialog(
                onDismiss = { viewModel.setSavingsGoalDialogVisible(false) },
                onSubmit = { title, target, current ->
                    viewModel.addSavingsGoal(title, target, current)
                    viewModel.setSavingsGoalDialogVisible(false)
                }
            )
        }

        // ==========================================
        // DIALOG: QUICK CONTRIBUTION (SAVINGS TOP-UP)
        // ==========================================
        if (uiState.showContributionDialog && uiState.targetGoalForContribution != null) {
            SavingsContributionDialog(
                goal = uiState.targetGoalForContribution!!,
                onDismiss = { viewModel.setContributionDialogVisible(false) },
                onSubmit = { addition ->
                    viewModel.updateSavingsGoalProgress(uiState.targetGoalForContribution!!, addition)
                    viewModel.setContributionDialogVisible(false)
                }
            )
        }

        // ==========================================
        // DIALOG: ADD NOTED ITEM
        // ==========================================
        if (uiState.showAddNotedDialog) {
            AddNotedDialog(
                selectedTheme = selectedBgTheme,
                onDismiss = { viewModel.setNotedDialogVisible(false) },
                onSubmit = { title, nominal, isDebt, notes, dueDate ->
                    viewModel.addNotedItem(title, nominal, isDebt, notes, dueDate)
                    viewModel.setNotedDialogVisible(false)
                }
            )
        }

        // ==========================================
        // DIALOG: CUSTOM CALCULATOR
        // ==========================================
        if (showCalculator) {
            CustomCalculatorDialog(
                selectedTheme = selectedBgTheme,
                onDismiss = { showCalculator = false }
            )
        }

        // ==========================================
        // DIALOG: PIANO EASTER EGG SIMULATION
        // ==========================================
        if (showPianoScreen) {
            PianoSimulationDialog(
                selectedTheme = selectedBgTheme,
                onDismiss = { showPianoScreen = false }
            )
        }

        // ==========================================
        // FAB: FLOATING CALCULATOR ACTION BUTTON
        // ==========================================
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 24.dp, bottom = 32.dp)
        ) {
            FloatingActionButton(
                onClick = {
                    PopSoundPlayer.play()
                    showCalculator = true
                },
                containerColor = animPrimaryColor.copy(alpha = 0.55f),
                contentColor = dynamicTextColor,
                shape = CircleShape,
                modifier = Modifier
                    .size(56.dp)
                    .testTag("calculator_fab")
            ) {
                Icon(
                    imageVector = Icons.Default.Calculate,
                    contentDescription = "Calculator",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
}

// ==========================================
// SUB-UI DIALOGS (Modal Sheets)
// ==========================================

@Composable
fun AddTransactionDialog(
    transaction: TransactionEntity? = null,
    defaultType: String = "EXPENSE",
    categoriesList: List<CategoryEntity> = emptyList(),
    onAddCategory: (String, String) -> Unit = { _, _ -> },
    onDeleteCategory: (CategoryEntity) -> Unit = {},
    onDismiss: () -> Unit,
    onSubmit: (id: Int, title: String, amount: Double, type: String, category: String, notes: String) -> Unit
) {
    var title by remember { mutableStateOf(transaction?.title ?: "") }
    var amountStr by remember { mutableStateOf(transaction?.let { formatInputString(it.amount.toLong().toString()) } ?: "") }
    var type by remember { mutableStateOf(transaction?.type ?: defaultType) }
    var category by remember { mutableStateOf(transaction?.category ?: "") }
    var notes by remember { mutableStateOf(transaction?.notes ?: "") }

    var showCustomCategoryInput by remember { mutableStateOf(false) }
    var newCategoryName by remember { mutableStateOf("") }

    val fallbackIncome = listOf("Gaji", "Investasi", "Freelance", "Lainnya")
    val fallbackExpense = listOf("Makanan", "Belanja", "Rumah", "Transport", "Hiburan", "Lainnya")
    
    val baseList = categoriesList.ifEmpty {
        fallbackIncome.map { CategoryEntity(name = it, type = "INCOME", isCustom = false) } + fallbackExpense.map { CategoryEntity(name = it, type = "EXPENSE", isCustom = false) }
    }

    val activeCategories = baseList.filter { it.type == type }

    // Set default selected category if none
    LaunchedEffect(type, activeCategories) {
        if (category.isEmpty() || activeCategories.none { it.name.lowercase() == category.lowercase() }) {
            category = activeCategories.firstOrNull()?.name ?: "Lainnya"
        }
    }

    // Outlining focused Colors to ensure black typing color always visible
    val inputColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color(0xFF1B2430),
        unfocusedTextColor = Color(0xFF1B2430),
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedBorderColor = ColorToska,
        unfocusedBorderColor = Color(0xFF525F71).copy(alpha = 0.5f),
        focusedLabelColor = ColorToska,
        unfocusedLabelColor = Color(0xFF525F71)
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = if (transaction != null) "EDIT TRANSAKSI" else "TRANSAKSI BARU",
                    style = LiquidLabelCaps,
                    color = ColorToska,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Tipe Transaksi",
                    style = LiquidLabelCaps,
                    color = Color(0xFF1B2430),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val selectedBorderColor = ColorToska
                    // INCOME
                    Button(
                        onClick = {
                            PopSoundPlayer.play()
                            type = "INCOME"
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .border(
                                width = 2.dp,
                                color = if (type == "INCOME") selectedBorderColor else Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (type == "INCOME") ColorToska.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.03f),
                            contentColor = if (type == "INCOME") ColorToska else Color(0xFF525F71)
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    ) {
                        Text("Pemasukan", fontWeight = FontWeight.Bold, style = LiquidLabelCaps)
                    }
                    
                    // EXPENSE
                    Button(
                        onClick = {
                            PopSoundPlayer.play()
                            type = "EXPENSE"
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .border(
                                width = 2.dp,
                                color = if (type == "EXPENSE") selectedBorderColor else Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (type == "EXPENSE") ColorToska.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.03f),
                            contentColor = if (type == "EXPENSE") ColorToska else Color(0xFF525F71)
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    ) {
                        Text("Pengeluaran", fontWeight = FontWeight.Bold, style = LiquidLabelCaps)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Amount Input - Formatted dynamically (Top position)
                OutlinedTextField(
                    value = amountStr,
                    onValueChange = { amountStr = formatInputString(it) },
                    label = { Text("Nominal (Rupiah)", style = LiquidLabelCaps, color = Color(0xFF1B2430)) },
                    singleLine = true,
                    colors = inputColors,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("amount_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Title Input (Bottom position)
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Deskripsi Transaksi", style = LiquidLabelCaps, color = Color(0xFF1B2430)) },
                    singleLine = true,
                    colors = inputColors,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Kategori Penugasan",
                    style = LiquidLabelCaps,
                    color = Color(0xFF1B2430),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    activeCategories.forEach { cat ->
                        val isSelected = category.lowercase() == cat.name.lowercase()
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) ColorToska.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.03f))
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) ColorToska else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { 
                                    PopSoundPlayer.play()
                                    category = cat.name 
                                }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = getCategoryIcon(cat.name),
                                    contentDescription = cat.name,
                                    tint = if (isSelected) ColorToska else Color(0xFF525F71),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = cat.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isSelected) ColorToska else Color(0xFF525F71),
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                                
                                if (cat.isCustom) {
                                    IconButton(
                                        onClick = {
                                            PopSoundPlayer.play()
                                            onDeleteCategory(cat)
                                        },
                                        modifier = Modifier.size(16.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Hapus",
                                            tint = Color.Red.copy(alpha = 0.7f),
                                            modifier = Modifier.size(12.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(ColorToska.copy(alpha = 0.07f))
                            .border(1.dp, ColorToska.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .clickable {
                                PopSoundPlayer.play()
                                showCustomCategoryInput = !showCustomCategoryInput
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Tambah Kategori Baru",
                                tint = ColorToska,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Tambah",
                                style = MaterialTheme.typography.bodyMedium,
                                color = ColorToska,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                if (showCustomCategoryInput) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = newCategoryName,
                            onValueChange = { newCategoryName = it },
                            placeholder = { Text("Nama Kategori Baru", style = MaterialTheme.typography.bodyMedium) },
                            singleLine = true,
                            colors = inputColors,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        Button(
                            onClick = {
                                if (newCategoryName.isNotBlank()) {
                                    PopSoundPlayer.play()
                                    onAddCategory(newCategoryName.trim(), type)
                                    category = newCategoryName.trim()
                                    newCategoryName = ""
                                    showCustomCategoryInput = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ColorToska),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Simpan", style = LiquidLabelCaps)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Notes Input
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Catatan Tambahan (Opsional)", style = LiquidLabelCaps, color = Color(0xFF1B2430)) },
                    singleLine = true,
                    colors = inputColors,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Batal", style = LiquidLabelCaps, color = Color(0xFF525F71))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val amt = parseInputToDouble(amountStr)
                            onSubmit(transaction?.id ?: 0, title, amt, type, category, notes)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorToska),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("submit_transaction_button")
                    ) {
                        Text("Simpan", style = LiquidLabelCaps, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun AddBudgetDialog(
    categoriesList: List<CategoryEntity> = emptyList(),
    onDismiss: () -> Unit,
    onSubmit: (String, Double) -> Unit
) {
    var category by remember { mutableStateOf("") }
    var limitStr by remember { mutableStateOf("") }

    val fallbackExpense = listOf("Makanan", "Belanja", "Rumah", "Transport", "Hiburan", "Lainnya")
    val budgetsAndCats = categoriesList.filter { it.type == "EXPENSE" }.ifEmpty {
        fallbackExpense.map { CategoryEntity(name = it, type = "EXPENSE", isCustom = false) }
    }

    LaunchedEffect(budgetsAndCats) {
        if (category.isEmpty() || budgetsAndCats.none { it.name.lowercase() == category.lowercase() }) {
            category = budgetsAndCats.firstOrNull()?.name ?: "Makanan"
        }
    }

    val inputColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color(0xFF1B2430),
        unfocusedTextColor = Color(0xFF1B2430),
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedBorderColor = ColorToska,
        unfocusedBorderColor = Color(0xFF525F71).copy(alpha = 0.5f),
        focusedLabelColor = ColorToska,
        unfocusedLabelColor = Color(0xFF525F71)
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "SET BUDGET ALOKASI",
                    style = LiquidLabelCaps,
                    color = ColorToska,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Kategori Pengeluaran", style = LiquidLabelCaps, color = Color(0xFF1B2430))
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    budgetsAndCats.forEach { cat ->
                        val isSelected = category.lowercase() == cat.name.lowercase()
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) ColorToska.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.03f))
                                .border(1.dp, if (isSelected) ColorToska else Color.Transparent, RoundedCornerShape(12.dp))
                                .clickable { category = cat.name }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = cat.name,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isSelected) ColorToska else Color(0xFF525F71),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Limit Input - Formatted beautifully as typing
                OutlinedTextField(
                    value = limitStr,
                    onValueChange = { limitStr = formatInputString(it) },
                    label = { Text("Batas Maksimal Bulanan (Rp)", style = LiquidLabelCaps, color = Color(0xFF1B2430)) },
                    singleLine = true,
                    colors = inputColors,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Batal", style = LiquidLabelCaps, color = Color(0xFF525F71))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val limitVal = parseInputToDouble(limitStr)
                            onSubmit(category, limitVal)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorToska),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Terapkan", style = LiquidLabelCaps, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun AddSavingsGoalDialog(
    onDismiss: () -> Unit,
    onSubmit: (String, Double, Double) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var targetStr by remember { mutableStateOf("") }
    var currentStr by remember { mutableStateOf("") }

    val inputColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color(0xFF1B2430),
        unfocusedTextColor = Color(0xFF1B2430),
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedBorderColor = ColorToska,
        unfocusedBorderColor = Color(0xFF525F71).copy(alpha = 0.5f),
        focusedLabelColor = ColorToska,
        unfocusedLabelColor = Color(0xFF525F71)
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "TARGET TABUNGAN BARU",
                    style = LiquidLabelCaps,
                    color = ColorToska,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Nama Target (cth: Liburan Bali)", style = LiquidLabelCaps, color = Color(0xFF1B2430)) },
                    singleLine = true,
                    colors = inputColors,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = targetStr,
                    onValueChange = { targetStr = formatInputString(it) },
                    label = { Text("Jumlah Target (Rp)", style = LiquidLabelCaps, color = Color(0xFF1B2430)) },
                    singleLine = true,
                    colors = inputColors,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Tabungan awal
                OutlinedTextField(
                    value = currentStr,
                    onValueChange = { currentStr = formatInputString(it) },
                    label = { Text("Tabungan Awal Saat Ini (Rp)", style = LiquidLabelCaps, color = Color(0xFF1B2430)) },
                    singleLine = true,
                    colors = inputColors,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Batal", style = LiquidLabelCaps, color = Color(0xFF525F71))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val trg = parseInputToDouble(targetStr)
                            val cur = parseInputToDouble(currentStr)
                            onSubmit(title, trg, cur)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorToska),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Simpan", style = LiquidLabelCaps, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun SavingsContributionDialog(
    goal: SavingsGoalEntity,
    onDismiss: () -> Unit,
    onSubmit: (Double) -> Unit
) {
    var additionStr by remember { mutableStateOf("") }

    val inputColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color(0xFF1B2430),
        unfocusedTextColor = Color(0xFF1B2430),
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedBorderColor = ColorToska,
        unfocusedBorderColor = Color(0xFF525F71).copy(alpha = 0.5f),
        focusedLabelColor = ColorToska,
        unfocusedLabelColor = Color(0xFF525F71)
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "SETOR KE TARGET",
                    style = LiquidLabelCaps,
                    color = ColorToska,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = goal.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B2430)
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = additionStr,
                    onValueChange = { additionStr = formatInputString(it) },
                    label = { Text("Jumlah Setoran Tabungan (Rp)", style = LiquidLabelCaps, color = Color(0xFF1B2430)) },
                    singleLine = true,
                    colors = inputColors,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Batal", style = LiquidLabelCaps, color = Color(0xFF525F71))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val addVal = parseInputToDouble(additionStr)
                            onSubmit(addVal)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorToska),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Setor Sekarang", style = LiquidLabelCaps, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun BackgroundCustomizerDialog(
    selectedTheme: CustomBgTheme,
    onThemeSelect: (CustomBgTheme) -> Unit,
    selectedFont: AppFont,
    onFontSelect: (AppFont) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(2.dp, selectedTheme.primaryColor.copy(alpha = 0.5f), RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(
                containerColor = if (selectedTheme.isDark) Color(0xD00B0F19) else Color(0xD0FFFFFF)
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "GANTI WARNA BEKGRON",
                    style = LiquidLabelCaps,
                    color = selectedTheme.primaryColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Start)
                )
                
                Spacer(modifier = Modifier.height(18.dp))
                
                // Wrap in Box with max weight to enable scrolling without overflow
                Box(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .fillMaxWidth()
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        CustomBgTheme.values().forEach { theme ->
                            val isSelected = theme == selectedTheme
                            
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        if (isSelected) theme.primaryColor.copy(alpha = 0.18f) 
                                        else if (selectedTheme.isDark) Color.White.copy(alpha = 0.05f) 
                                        else Color.Black.copy(alpha = 0.03f)
                                    )
                                    .border(1.dp, if (isSelected) theme.primaryColor else Color.Transparent, RoundedCornerShape(14.dp))
                                    .clickable {
                                        PopSoundPlayer.play(theme)
                                        onThemeSelect(theme)
                                    }
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Mini Color preview circle
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(theme.bgColor)
                                        .border(
                                            1.dp, 
                                            if (selectedTheme.isDark) Color.White.copy(alpha = 0.3f) else Color.Gray.copy(alpha = 0.3f), 
                                            CircleShape
                                        )
                                )
                                
                                Spacer(modifier = Modifier.width(12.dp))
                                
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = when (theme) {
                                            CustomBgTheme.DEFAULT -> "Default (Glossy Onyx)"
                                            CustomBgTheme.PUTIH -> "Putih (White Aesthetic)"
                                            CustomBgTheme.CREAM -> "Cream Classy Cozy"
                                            CustomBgTheme.BIRU_AWAN -> "Biru Awan (Sky Blue)"
                                            CustomBgTheme.PINK -> "Soft Sweet Pink"
                                            CustomBgTheme.UNGU_SENJA -> "Ungu Senja (Sunset Indigo)"
                                            CustomBgTheme.HIJAU_MINT -> "Hijau Mint (Forest Emerald)"
                                            CustomBgTheme.MATAHARI -> "Matahari (Sunset Orange)"
                                            CustomBgTheme.CYBERPUNK -> "Cyberpunk (Neon Midnight)"
                                            CustomBgTheme.AQUA_DEEP -> "Aqua Deep (Ocean Trench)"
                                            CustomBgTheme.SAKURA -> "Sakura Dream (Cherry Blossom)"
                                            CustomBgTheme.MIDNIGHT_FOREST -> "Midnight Forest (Fern Gold)"
                                            CustomBgTheme.NORDIC_SLATE -> "Nordic Slate (Silver Frost)"
                                            CustomBgTheme.RETRO_ARCADE -> "Retro Arcade (Laser Fire)"
                                            CustomBgTheme.COSMIC_LAVENDER -> "Cosmic Lavender (Magic Violet)"
                                            CustomBgTheme.FUNKY_NEON -> "Funky Neon (Vibrant Lime Pink)"
                                            CustomBgTheme.LAVA_VOLCANO -> "Lava Volcano (Fierce Magma)"
                                            CustomBgTheme.GLITCH_MATRIX -> "Glitch Matrix (Hacker Green)"
                                            CustomBgTheme.CANDY_LAND -> "Candy Land (Bubblegum Sweet)"
                                            CustomBgTheme.GOOGLE_CLASSIC -> "Google Classic (White G-Core)"
                                            CustomBgTheme.GOOGLE_DARK -> "Google Dark (Material Dark)"
                                            CustomBgTheme.GOOGLE_PIXEL -> "Google Pixel (Aesthetic Minimal)"
                                            CustomBgTheme.GOOGLE_WORK -> "Google Work (Teal Corporate)"
                                        },
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (selectedTheme.isDark) Color.White else Color(0xFF1B2430),
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        // Primary dot
                                        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(theme.primaryColor))
                                        Text(
                                            text = "Masuk", 
                                            style = MaterialTheme.typography.labelSmall, 
                                            color = if (selectedTheme.isDark) Color.White.copy(alpha = 0.6f) else Color(0xFF525F71)
                                        )
                                        // Expense dot
                                        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(theme.expenseColor))
                                        Text(
                                            text = "Keluar", 
                                            style = MaterialTheme.typography.labelSmall, 
                                            color = if (selectedTheme.isDark) Color.White.copy(alpha = 0.6f) else Color(0xFF525F71)
                                        )
                                    }
                                }
                                
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = theme.primaryColor,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Clean Divider
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(if (selectedTheme.isDark) Color.White.copy(alpha = 0.12f) else Color.Black.copy(alpha = 0.1f))
                        )
                        
                        Spacer(modifier = Modifier.height(18.dp))
                        
                        Text(
                            text = "PILIH GAYA FONT SISTEM (15 OPSI)",
                            style = LiquidLabelCaps,
                            color = selectedTheme.primaryColor,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        
                        AppFont.values().forEach { font ->
                            val isFontSelected = font == selectedFont
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isFontSelected) selectedTheme.primaryColor.copy(alpha = 0.15f)
                                        else if (selectedTheme.isDark) Color.White.copy(alpha = 0.04f)
                                        else Color.Black.copy(alpha = 0.02f)
                                    )
                                    .border(
                                        1.dp,
                                        if (isFontSelected) selectedTheme.primaryColor else Color.Transparent,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable {
                                        PopSoundPlayer.play()
                                        onFontSelect(font)
                                    }
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = font.displayName,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontFamily = font.fontFamily,
                                                fontWeight = if (isFontSelected) FontWeight.Bold else FontWeight.Medium
                                            ),
                                            color = if (selectedTheme.isDark) Color.White else Color(0xFF1B2430)
                                        )
                                        if (font.isPixel) {
                                             Spacer(modifier = Modifier.width(8.dp))
                                             Box(
                                                 modifier = Modifier
                                                     .clip(RoundedCornerShape(4.dp))
                                                     .background(selectedTheme.primaryColor)
                                                     .padding(horizontal = 6.dp, vertical = 2.dp)
                                             ) {
                                                 Text(
                                                     text = "NEW PIXEL",
                                                     style = TextStyle(
                                                         fontFamily = font.fontFamily,
                                                         fontSize = 8.sp,
                                                         fontWeight = FontWeight.Bold,
                                                         color = Color.Black
                                                     )
                                                 )
                                             }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Contoh: Rp 150.000 (Pemasukan)",
                                        style = TextStyle(
                                            fontFamily = font.fontFamily,
                                            fontSize = 11.sp,
                                            color = (if (selectedTheme.isDark) Color.White else Color(0xFF1B2430)).copy(alpha = 0.5f)
                                        )
                                    )
                                }
                                
                                if (isFontSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = selectedTheme.primaryColor,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = selectedTheme.primaryColor),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Selesai", style = LiquidLabelCaps, color = Color.White)
                }
            }
        }
    }
}

// ==========================================
// NOTED (UTANG-PIUTANG) COMPOSABLES
// ==========================================

@Composable
fun NotedDashboard(
    selectedBgTheme: CustomBgTheme,
    animPrimaryColor: Color,
    animExpenseColor: Color,
    dynamicTextColor: Color,
    dynamicSubTextColor: Color,
    dynamicSubTextColor60: Color,
    viewModel: LiquidFinanceViewModel,
    uiState: LiquidFinanceUiState
) {
    val scrollState = rememberScrollState()
    
    val totalUtang = remember(uiState.notedItems) {
        uiState.notedItems.filter { it.isDebt && !it.isCompleted }.sumOf { it.nominal }
    }
    val totalPiutang = remember(uiState.notedItems) {
        uiState.notedItems.filter { !it.isDebt && !it.isCompleted }.sumOf { it.nominal }
    }
    val selisihNoted = totalPiutang - totalUtang

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // SUMMARY SCORECARD FOR DEBTS/CREDITS
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = if (selectedBgTheme.isDark) {
                            listOf(Color(0xFF1E1B4B), Color(0xFF0F0E30))
                        } else {
                            listOf(Color(0xFFEEF2FF), Color(0xFFE0E7FF))
                        }
                    ),
                    shape = RoundedCornerShape(24.dp)
                ),
            containerColor = Color.Transparent,
            borderColor = animPrimaryColor.copy(alpha = 0.2f)
        ) {
            Text(
                text = "RINGKASAN NOTED (UTANG-PIUTANG)",
                style = LiquidLabelCaps.copy(fontSize = 11.sp, fontWeight = FontWeight.Bold),
                color = if (selectedBgTheme.isDark) Color.White.copy(alpha = 0.6f) else Color(0xFF475569)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = formatCurrency(selisihNoted),
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 32.sp),
                fontWeight = FontWeight.Black,
                color = when {
                    selisihNoted > 0 -> animPrimaryColor
                    selisihNoted < 0 -> animExpenseColor
                    else -> dynamicTextColor
                }
            )
            
            Text(
                text = if (selisihNoted >= 0) "Surplus Piutang (Bersih)" else "Defisit Utang (Bersih)",
                style = MaterialTheme.typography.bodySmall,
                color = dynamicSubTextColor
            )
            
            Spacer(modifier = Modifier.height(18.dp))
            
            Row(modifier = Modifier.fillMaxWidth()) {
                // PIUTANG Column
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "TOTAL PIUTANG",
                        style = LiquidLabelCaps.copy(fontSize = 10.sp),
                        color = animPrimaryColor
                    )
                    Text(
                        text = formatCurrency(totalPiutang),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = dynamicTextColor
                    )
                }
                
                // Divider line
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(34.dp)
                        .background(dynamicSubTextColor.copy(alpha = 0.2f))
                        .align(Alignment.CenterVertically)
                )
                
                // UTANG Column
                Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                    Text(
                        text = "TOTAL UTANG",
                        style = LiquidLabelCaps.copy(fontSize = 10.sp),
                        color = animExpenseColor
                    )
                    Text(
                        text = formatCurrency(totalUtang),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = dynamicTextColor
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ADD BUTTON FOR NOTED
        Button(
            onClick = {
                PopSoundPlayer.play()
                viewModel.setNotedDialogVisible(true)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = animPrimaryColor)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Tambah Catatan",
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "TAMBAH CATATAN BARU",
                style = LiquidLabelCaps,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // LIST HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "DAFTAR CATATAN AKTIF",
                style = LiquidLabelCaps,
                fontWeight = FontWeight.Bold,
                color = dynamicTextColor
            )
            
            Text(
                text = "${uiState.notedItems.size} Catatan",
                style = MaterialTheme.typography.bodySmall,
                color = dynamicSubTextColor
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (uiState.notedItems.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.EventNote,
                    contentDescription = "Empty",
                    tint = dynamicSubTextColor.copy(alpha = 0.4f),
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Belum Ada Catatan Utang/Piutang",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = dynamicTextColor
                )
                Text(
                    text = "Klik tombol di atas untuk menambah catatan jatuh tempo.",
                    style = MaterialTheme.typography.bodySmall,
                    color = dynamicSubTextColor,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            uiState.notedItems.forEach { item ->
                val isCompleted = item.isCompleted
                
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    containerColor = if (isCompleted) {
                        if (selectedBgTheme.isDark) Color.White.copy(alpha = 0.02f) else Color.Black.copy(alpha = 0.02f)
                    } else {
                        if (selectedBgTheme.isDark) Color.White.copy(alpha = 0.05f) else Color.White.copy(alpha = 0.6f)
                    },
                    borderColor = if (isCompleted) Color.Transparent else {
                        if (item.isDebt) animExpenseColor.copy(alpha = 0.15f) else animPrimaryColor.copy(alpha = 0.15f)
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isCompleted,
                            onCheckedChange = { 
                                PopSoundPlayer.play()
                                viewModel.toggleNotedItemCompleted(item)
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = animPrimaryColor,
                                uncheckedColor = dynamicSubTextColor
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        textDecoration = if (isCompleted) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                                    ),
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCompleted) dynamicSubTextColor else dynamicTextColor
                                )
                                
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(
                                            if (item.isDebt) animExpenseColor.copy(alpha = 0.15f)
                                            else animPrimaryColor.copy(alpha = 0.15f)
                                        )
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (item.isDebt) "UTANG" else "PIUTANG",
                                        style = LiquidLabelCaps.copy(fontSize = 8.sp, fontWeight = FontWeight.Bold),
                                        color = if (item.isDebt) animExpenseColor else animPrimaryColor
                                    )
                                }
                            }

                            if (item.notes.isNotBlank()) {
                                Text(
                                    text = item.notes,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = dynamicSubTextColor
                                )
                            }

                            if (item.dueDate.isNotBlank()) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(top = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = "Jatuh Tempo",
                                        tint = animExpenseColor.copy(alpha = 0.7f),
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = item.dueDate,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = animExpenseColor.copy(alpha = 0.8f)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = formatCurrency(item.nominal),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isCompleted) {
                                    dynamicSubTextColor
                                } else if (item.isDebt) {
                                    animExpenseColor
                                } else {
                                    animPrimaryColor
                                }
                            )
                            
                            IconButton(
                                onClick = {
                                    PopSoundPlayer.play()
                                    viewModel.deleteNotedItem(item)
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Hapus Catatan",
                                    tint = animExpenseColor.copy(alpha = 0.8f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun AddNotedDialog(
    selectedTheme: CustomBgTheme,
    onDismiss: () -> Unit,
    onSubmit: (title: String, nominal: Double, isDebt: Boolean, notes: String, dueDate: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var nominalStr by remember { mutableStateOf("") }
    var isDebt by remember { mutableStateOf(true) }
    var notes by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }

    val inputColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color(0xFF1B2430),
        unfocusedTextColor = Color(0xFF1B2430),
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedBorderColor = selectedTheme.primaryColor,
        unfocusedBorderColor = Color(0xFF525F71).copy(alpha = 0.5f),
        focusedLabelColor = selectedTheme.primaryColor,
        unfocusedLabelColor = Color(0xFF525F71)
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(2.dp, selectedTheme.primaryColor.copy(alpha = 0.5f), RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "TAMBAH CATATAN UTANG-PIUTANG",
                    style = LiquidLabelCaps,
                    color = selectedTheme.primaryColor,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.04f))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isDebt) selectedTheme.expenseColor else Color.Transparent)
                            .clickable {
                                PopSoundPlayer.play()
                                isDebt = true
                            }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "UTANG SAYA",
                            style = LiquidLabelCaps.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                            color = if (isDebt) Color.White else Color(0xFF475569)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (!isDebt) selectedTheme.primaryColor else Color.Transparent)
                            .clickable {
                                PopSoundPlayer.play()
                                isDebt = false
                            }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "PIUTANG SAYA",
                            style = LiquidLabelCaps.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                            color = if (!isDebt) Color.White else Color(0xFF475569)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Nama/Deskripsi Catatan", style = LiquidLabelCaps, color = Color(0xFF1B2430)) },
                    singleLine = true,
                    colors = inputColors,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = nominalStr,
                    onValueChange = { nominalStr = formatInputString(it) },
                    label = { Text("Nominal Uang (Rp)", style = LiquidLabelCaps, color = Color(0xFF1B2430)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = inputColors,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = dueDate,
                    onValueChange = { dueDate = it },
                    label = { Text("Jatuh Tempo (Misal: 30 Juni 2026)", style = LiquidLabelCaps, color = Color(0xFF1B2430)) },
                    singleLine = true,
                    colors = inputColors,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Catatan/Keterangan tambahan", style = LiquidLabelCaps, color = Color(0xFF1B2430)) },
                    maxLines = 3,
                    colors = inputColors,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Batal", style = LiquidLabelCaps, color = Color(0xFF525F71))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val amt = parseInputToDouble(nominalStr)
                            onSubmit(title, amt, isDebt, notes, dueDate)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = selectedTheme.primaryColor),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Simpan", style = LiquidLabelCaps, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun CustomCalculatorDialog(
    selectedTheme: CustomBgTheme,
    onDismiss: () -> Unit
) {
    var displayStr by remember { mutableStateOf("") }
    var previewResult by remember { mutableStateOf("0") }

    fun addChar(char: String) {
        PopSoundPlayer.play()
        displayStr += char
        previewResult = evaluateExpression(displayStr)
    }

    fun addOperator(op: String) {
        PopSoundPlayer.play()
        val trimmed = displayStr.trim()
        if (trimmed.endsWith("+") || trimmed.endsWith("-") || trimmed.endsWith("*") || trimmed.endsWith("/")) {
            displayStr = trimmed.dropLast(1) + op
        } else if (trimmed.isNotEmpty()) {
            displayStr = "$trimmed $op "
        }
    }

    fun calculateFinal() {
        PopSoundPlayer.play()
        if (displayStr.isNotBlank()) {
            val finalRes = evaluateExpression(displayStr)
            displayStr = finalRes
            previewResult = finalRes
        }
    }

    fun backspace() {
        PopSoundPlayer.play()
        val trimmed = displayStr.trim()
        if (trimmed.isNotEmpty()) {
            if (trimmed.endsWith(" +") || trimmed.endsWith(" -") || trimmed.endsWith(" *") || trimmed.endsWith(" /")) {
                displayStr = trimmed.dropLast(2)
            } else if (displayStr.endsWith(" ")) {
                displayStr = displayStr.dropLast(3)
            } else {
                displayStr = displayStr.dropLast(1)
            }
            previewResult = evaluateExpression(displayStr)
        }
    }

    fun clearAll() {
        PopSoundPlayer.play()
        displayStr = ""
        previewResult = "0"
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F172A))
                .systemBarsPadding()
                .padding(24.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Calculate,
                            contentDescription = "Calc",
                            tint = selectedTheme.primaryColor,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "KALKULATOR COY",
                            style = LiquidLabelCaps.copy(fontSize = 12.sp),
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White.copy(alpha = 0.5f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black)
                        .border(1.dp, selectedTheme.primaryColor.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = if (displayStr.isEmpty()) "0" else displayStr,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 32.sp, fontWeight = FontWeight.Bold),
                        color = Color.White,
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "= $previewResult",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                        color = selectedTheme.primaryColor,
                        textAlign = TextAlign.End
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                val buttonModifier = Modifier
                    .weight(1f)
                    .height(72.dp)
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = { clearAll() }, colors = ButtonDefaults.buttonColors(containerColor = selectedTheme.expenseColor), shape = RoundedCornerShape(12.dp), modifier = buttonModifier) {
                        Text("C", style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color.White))
                    }
                    Button(onClick = { backspace() }, colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.15f)), shape = RoundedCornerShape(12.dp), modifier = buttonModifier) {
                        Icon(imageVector = Icons.Default.Backspace, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(24.dp))
                    }
                    Button(onClick = { addOperator("/") }, colors = ButtonDefaults.buttonColors(containerColor = selectedTheme.primaryColor.copy(alpha = 0.25f)), shape = RoundedCornerShape(12.dp), modifier = buttonModifier) {
                        Text("/", style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Black, color = selectedTheme.primaryColor))
                    }
                    Button(onClick = { addOperator("*") }, colors = ButtonDefaults.buttonColors(containerColor = selectedTheme.primaryColor.copy(alpha = 0.25f)), shape = RoundedCornerShape(12.dp), modifier = buttonModifier) {
                        Text("x", style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Black, color = selectedTheme.primaryColor))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = { addChar("7") }, colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.08f)), shape = RoundedCornerShape(12.dp), modifier = buttonModifier) {
                        Text("7", style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White))
                    }
                    Button(onClick = { addChar("8") }, colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.08f)), shape = RoundedCornerShape(12.dp), modifier = buttonModifier) {
                        Text("8", style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White))
                    }
                    Button(onClick = { addChar("9") }, colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.08f)), shape = RoundedCornerShape(12.dp), modifier = buttonModifier) {
                        Text("9", style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White))
                    }
                    Button(onClick = { addOperator("-") }, colors = ButtonDefaults.buttonColors(containerColor = selectedTheme.primaryColor.copy(alpha = 0.25f)), shape = RoundedCornerShape(12.dp), modifier = buttonModifier) {
                        Text("-", style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Black, color = selectedTheme.primaryColor))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = { addChar("4") }, colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.08f)), shape = RoundedCornerShape(12.dp), modifier = buttonModifier) {
                        Text("4", style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White))
                    }
                    Button(onClick = { addChar("5") }, colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.08f)), shape = RoundedCornerShape(12.dp), modifier = buttonModifier) {
                        Text("5", style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White))
                    }
                    Button(onClick = { addChar("6") }, colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.08f)), shape = RoundedCornerShape(12.dp), modifier = buttonModifier) {
                        Text("6", style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White))
                    }
                    Button(onClick = { addOperator("+") }, colors = ButtonDefaults.buttonColors(containerColor = selectedTheme.primaryColor.copy(alpha = 0.25f)), shape = RoundedCornerShape(12.dp), modifier = buttonModifier) {
                        Text("+", style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Black, color = selectedTheme.primaryColor))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = { addChar("1") }, colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.08f)), shape = RoundedCornerShape(12.dp), modifier = buttonModifier) {
                        Text("1", style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White))
                    }
                    Button(onClick = { addChar("2") }, colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.08f)), shape = RoundedCornerShape(12.dp), modifier = buttonModifier) {
                        Text("2", style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White))
                    }
                    Button(onClick = { addChar("3") }, colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.08f)), shape = RoundedCornerShape(12.dp), modifier = buttonModifier) {
                        Text("3", style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White))
                    }
                    Button(onClick = { calculateFinal() }, colors = ButtonDefaults.buttonColors(containerColor = selectedTheme.primaryColor), shape = RoundedCornerShape(12.dp), modifier = buttonModifier) {
                        Text("=", style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color.White))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { addChar("0") }, 
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.08f)), 
                        shape = RoundedCornerShape(12.dp), 
                        modifier = Modifier.weight(1.2f).height(72.dp)
                    ) {
                        Text("0", style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White))
                    }
                    
                    Button(
                        onClick = { addChar(".") }, 
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.08f)), 
                        shape = RoundedCornerShape(12.dp), 
                        modifier = Modifier.weight(0.8f).height(72.dp)
                    ) {
                        Text(".", style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White))
                    }
                }
            }
        }
    }
}

fun evaluateExpression(expr: String): String {
    val clean = expr.trim()
    val tokens = clean.split(" ")
    if (tokens.isEmpty() || tokens[0].isEmpty()) return "0"
    try {
        var result = tokens[0].toDoubleOrNull() ?: return "0"
        var i = 1
        while (i < tokens.size - 1) {
            val op = tokens[i]
            val nextVal = tokens[i+1].toDoubleOrNull() ?: 0.0
            result = when (op) {
                "+" -> result + nextVal
                "-" -> result - nextVal
                "*" -> result * nextVal
                "/" -> if (nextVal != 0.0) result / nextVal else 0.0
                else -> result
            }
            i += 2
        }
        val intResult = result.toLong()
        return if (result == intResult.toDouble()) intResult.toString() else String.format(Locale.US, "%.2f", result)
    } catch (e: Exception) {
        return "Error"
    }
}

fun exportAndShareFinancialPdf(
    context: android.content.Context,
    timeFilter: String,
    transactions: List<com.example.data.TransactionEntity>,
    recapItems: List<RecapItem>
) {
    try {
        val pdfDocument = android.graphics.pdf.PdfDocument()
        val pageInfo = android.graphics.pdf.PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        
        val paintTitle = android.graphics.Paint().apply {
            color = android.graphics.Color.BLACK
            textSize = 22f
            isFakeBoldText = true
            isAntiAlias = true
        }
        
        val paintSub = android.graphics.Paint().apply {
            color = android.graphics.Color.DKGRAY
            textSize = 12f
            isAntiAlias = true
        }
        
        val paintHeader = android.graphics.Paint().apply {
            color = android.graphics.Color.BLACK
            textSize = 14f
            isFakeBoldText = true
            isAntiAlias = true
        }

        val paintText = android.graphics.Paint().apply {
            color = android.graphics.Color.BLACK
            textSize = 11f
            isAntiAlias = true
        }

        val paintLine = android.graphics.Paint().apply {
            color = android.graphics.Color.LTGRAY
            strokeWidth = 1f
        }

        // 1. Draw Title Header Card
        canvas.drawText("LIQUID FINANCE RECAP", 40f, 60f, paintTitle)
        canvas.drawText("Laporan Ringkasan Keuangan - Periodik $timeFilter", 40f, 82f, paintSub)
        canvas.drawText("Dibuat pada: ${java.text.DateFormat.getDateTimeInstance().format(java.util.Date())}", 40f, 98f, paintSub)
        
        canvas.drawLine(40f, 115f, 555f, 115f, paintLine)

        // 2. Draw Calculations Summary
        val totalIncome = transactions.filter { it.type == "INCOME" }.sumOf { it.amount }
        val totalExpense = transactions.filter { it.type == "EXPENSE" }.sumOf { it.amount }
        val finalBalance = totalIncome - totalExpense

        canvas.drawText("RANGKUMAN KEUANGAN", 40f, 145f, paintHeader)
        canvas.drawText("Total Pemasukan: Rp ${String.format("%,.0f", totalIncome)}", 40f, 170f, paintText)
        canvas.drawText("Total Pengeluaran: Rp ${String.format("%,.0f", totalExpense)}", 40f, 190f, paintText)
        canvas.drawText("Saldo Akhir (Surplus/Defisit): Rp ${String.format("%,.0f", finalBalance)}", 40f, 210f, paintText)

        canvas.drawLine(40f, 235f, 555f, 235f, paintLine)

        // 3. Draw Aggregated Data Section
        canvas.drawText("REKAP DATA AGREGAT BERDASARKAN FILTER", 40f, 260f, paintHeader)
        
        var yPos = 290f
        canvas.drawText("Label / Periode", 40f, yPos, paintSub)
        canvas.drawText("Pemasukan", 220f, yPos, paintSub)
        canvas.drawText("Pengeluaran", 380f, yPos, paintSub)
        
        yPos += 14f
        canvas.drawLine(40f, yPos, 555f, yPos, paintLine)
        
        if (recapItems.isEmpty() || (recapItems.size == 1 && recapItems[0].label == "No Data")) {
            yPos += 24f
            canvas.drawText("Tidak ada transaksi untuk periode ini.", 40f, yPos, paintText)
        } else {
            recapItems.take(15).forEach { item ->
                yPos += 24f
                canvas.drawText(item.label, 40f, yPos, paintText)
                canvas.drawText("Rp ${String.format("%,.0f", item.income)}", 220f, yPos, paintText)
                canvas.drawText("Rp ${String.format("%,.0f", item.expense)}", 380f, yPos, paintText)
            }
        }

        // Draw basic footer
        canvas.drawText("Laporan digital ini sah dibuat otomatis oleh sistem Liquid Finance.", 40f, 800f, paintSub)

        pdfDocument.finishPage(page)

        // Write the PDF file to context.cacheDir
        val file = java.io.File(context.cacheDir, "LiquidFinance_Recap_${System.currentTimeMillis()}.pdf")
        val outputStream = java.io.FileOutputStream(file)
        pdfDocument.writeTo(outputStream)
        outputStream.close()
        pdfDocument.close()

        // 4. Share PDF File using FileProvider
        val uri = androidx.core.content.FileProvider.getUriForFile(
            context,
            "com.example.fileprovider",
            file
        )

        val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(android.content.Intent.EXTRA_STREAM, uri)
            putExtra(android.content.Intent.EXTRA_SUBJECT, "Laporan Keuangan Liquid Finance - $timeFilter")
            putExtra(android.content.Intent.EXTRA_TEXT, "Halo! Berikut laporan ringkasan keuangan Anda dari aplikasi Liquid Finance untuk periode $timeFilter.\n\nSaldo Akhir Anda saat ini adalah Rp ${String.format("%,.0f", finalBalance)}")
            addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        android.widget.Toast.makeText(context, "Mengekspor laporan PDF...", android.widget.Toast.LENGTH_SHORT).show()

        context.startActivity(android.content.Intent.createChooser(intent, "Bagikan Laporan PDF Keuangan").apply {
            addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        })

    } catch (e: Exception) {
        e.printStackTrace()
        android.widget.Toast.makeText(context, "Gagal mengekspor PDF: ${e.message}", android.widget.Toast.LENGTH_LONG).show()
    }
}

// ==========================================
// PIANO EASTER EGG SIMULATION & SOUND SYNTHESIZER
// ==========================================

object PianoSoundPlayer {
    fun playNote(freq: Double, isPianoMode: Boolean) {
        Thread {
            try {
                val sampleRate = 44100
                val durationMs = if (isPianoMode) 600 else 400
                val numSamples = durationMs * sampleRate / 1000
                val sample = DoubleArray(numSamples)
                val generateBuffer = ShortArray(numSamples)
                
                if (isPianoMode) {
                    // Soft digital acoustic piano sound with decays
                    for (i in 0 until numSamples) {
                        val t = i.toDouble() / sampleRate
                        val progress = i.toDouble() / numSamples
                        val envelope = if (progress < 0.03) {
                            progress / 0.03
                        } else {
                            Math.exp(-5.0 * (progress - 0.03))
                        }
                        
                        val baseSine = Math.sin(2.0 * Math.PI * freq * t)
                        val overtone1 = Math.sin(2.0 * Math.PI * freq * 2.0 * t) * 0.35
                        val overtone2 = Math.sin(2.0 * Math.PI * freq * 3.0 * t) * 0.18
                        val overtone3 = Math.sin(2.0 * Math.PI * freq * 4.0 * t) * 0.08
                        val noise = (Math.random() - 0.5) * 0.02 * Math.exp(-25.0 * progress)
                        
                        val mixed = (baseSine + overtone1 + overtone2 + overtone3) / 1.6
                        sample[i] = (mixed + noise) * envelope * 0.8
                    }
                } else {
                    // Cute meow synthesizer matching key pitch
                    val baseFreq = freq
                    val endFreq = freq * 1.83
                    val fallFreq = freq * 1.35
                    
                    for (i in 0 until numSamples) {
                        val t = i.toDouble() / sampleRate
                        val progress = i.toDouble() / numSamples
                        
                        val currentFreq = if (progress < 0.25) {
                            baseFreq + ((endFreq - baseFreq) * (progress / 0.25))
                        } else {
                            endFreq - ((endFreq - fallFreq) * ((progress - 0.25) / 0.75))
                        }
                        
                        val envelope = if (progress < 0.12) {
                            progress / 0.12
                        } else {
                            Math.exp(-3.5 * (progress - 0.12))
                        }
                        
                        val vibrato = 1.0 + 0.06 * Math.sin(2.0 * Math.PI * 14.0 * t)
                        val baseSine = Math.sin(2.0 * Math.PI * currentFreq * vibrato * t)
                        val secondHarmonic = Math.sin(2.0 * Math.PI * (currentFreq * 1.45) * vibrato * t) * 0.45
                        val thirdHarmonic = Math.sin(2.0 * Math.PI * (currentFreq * 2.1) * vibrato * t) * 0.2
                        val noiseComponent = ((Math.random() - 0.5) * 0.03) * envelope
                        
                        val mixed = (baseSine + secondHarmonic + thirdHarmonic) / 1.65
                        sample[i] = (mixed + noiseComponent) * envelope * 0.7
                    }
                }
                
                for (i in sample.indices) {
                    generateBuffer[i] = (sample[i] * 32767).toInt().coerceIn(-32768, 32767).toShort()
                }
                
                val track = AudioTrack(
                    android.media.AudioManager.STREAM_MUSIC,
                    sampleRate,
                    android.media.AudioFormat.CHANNEL_OUT_MONO,
                    android.media.AudioFormat.ENCODING_PCM_16BIT,
                    generateBuffer.size * 2,
                    AudioTrack.MODE_STATIC
                )
                track.write(generateBuffer, 0, generateBuffer.size)
                track.play()
                
                Thread.sleep(durationMs.toLong() + 30L)
                track.stop()
                track.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}

data class PianoKeyInfo(
    val name: String,
    val freq: Double,
    val solfege: String,
    val whiteIndexBefore: Int = -1
)

@Composable
fun PianoSimulationDialog(
    selectedTheme: CustomBgTheme,
    onDismiss: () -> Unit
) {
    var isMeowMode by remember { mutableStateOf(false) }
    var showNoteLabels by remember { mutableStateOf(true) }
    
    // 2 Octaves + 1 High Note
    // 15 White Keys
    val whiteKeys = listOf(
        PianoKeyInfo("C4", 261.63, "Do"),
        PianoKeyInfo("D4", 293.66, "Re"),
        PianoKeyInfo("E4", 329.63, "Mi"),
        PianoKeyInfo("F4", 349.23, "Fa"),
        PianoKeyInfo("G4", 392.00, "Sol"),
        PianoKeyInfo("A4", 440.00, "La"),
        PianoKeyInfo("B4", 493.88, "Si"),
        PianoKeyInfo("C5", 523.25, "Do"),
        PianoKeyInfo("D5", 587.33, "Re"),
        PianoKeyInfo("E5", 659.25, "Mi"),
        PianoKeyInfo("F5", 698.46, "Fa"),
        PianoKeyInfo("G5", 783.99, "Sol"),
        PianoKeyInfo("A5", 880.00, "La"),
        PianoKeyInfo("B5", 987.77, "Si"),
        PianoKeyInfo("C6", 1046.50, "Do")
    )
    
    // Black keys mapping (each positions under index k of the white keys)
    val blackKeys = listOf(
        PianoKeyInfo("C#4", 277.18, "Do#", whiteIndexBefore = 0),
        PianoKeyInfo("D#4", 311.13, "Re#", whiteIndexBefore = 1),
        PianoKeyInfo("F#4", 369.99, "Fa#", whiteIndexBefore = 3),
        PianoKeyInfo("G#4", 415.30, "Sol#", whiteIndexBefore = 4),
        PianoKeyInfo("A#4", 466.16, "La#", whiteIndexBefore = 5),
        PianoKeyInfo("C#5", 554.37, "Do#", whiteIndexBefore = 7),
        PianoKeyInfo("D#5", 622.25, "Re#", whiteIndexBefore = 8),
        PianoKeyInfo("F#5", 739.99, "Fa#", whiteIndexBefore = 10),
        PianoKeyInfo("G#5", 830.61, "Sol#", whiteIndexBefore = 11),
        PianoKeyInfo("A#5", 932.33, "La#", whiteIndexBefore = 12)
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        val config = LocalConfiguration.current
        val screenW = config.screenWidthDp.dp
        val screenH = config.screenHeightDp.dp

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F172A)),
            contentAlignment = Alignment.Center
        ) {
            // Apply 90 degrees rotation for landscape view simulation within vertical environment
            Box(
                modifier = Modifier
                    .size(width = screenH, height = screenW)
                    .rotate(90f)
                    .background(Color(0xFF020617))
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // HEADER ROW
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "🐾 PIANO CATATCOY UNYU (2 OKTAF)",
                                style = LiquidLabelCaps.copy(fontSize = 11.sp),
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            
                            // MODE TOGGLE
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White.copy(alpha = 0.08f))
                                    .clickable { 
                                         PopSoundPlayer.play()
                                         isMeowMode = !isMeowMode 
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(if (isMeowMode) Color(0xFF10B981) else Color(0xFFEF4444))
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isMeowMode) "MODE: NYAN MEOW 🐱" else "MODE: PIANO AKUSTIK 🎹",
                                    style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // LABEL TOGGLE
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White.copy(alpha = 0.08f))
                                    .clickable { 
                                         PopSoundPlayer.play()
                                         showNoteLabels = !showNoteLabels 
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (showNoteLabels) "NADA: SHOW ✅" else "NADA: HIDE ❌",
                                    style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                            }
                        }
                        
                        IconButton(
                            onClick = onDismiss, 
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.1f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Piano",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    
                    // PIANO KEYBOARD WRAPPER Box
                    BoxWithConstraints(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.Black)
                            .padding(4.dp)
                    ) {
                        val keyboardWidth = maxWidth
                        val numWhite = whiteKeys.size
                        val whiteKeyWidth = keyboardWidth / numWhite

                        // 1. WHITE KEYS LAYER
                        Row(modifier = Modifier.fillMaxSize()) {
                            whiteKeys.forEachIndexed { idx, key ->
                                var isPressed by remember { mutableStateOf(false) }
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .padding(horizontal = 1.5.dp)
                                        .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
                                        .background(if (isPressed) Color(0xFFE2E8F0) else Color.White)
                                        .pointerInput(Unit) {
                                            detectTapGestures(
                                                onPress = {
                                                    isPressed = true
                                                    PianoSoundPlayer.playNote(key.freq, isPianoMode = !isMeowMode)
                                                    tryAwaitRelease()
                                                    isPressed = false
                                                }
                                            )
                                        },
                                    contentAlignment = Alignment.BottomCenter
                                ) {
                                    if (showNoteLabels) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.padding(bottom = 12.dp)
                                        ) {
                                            Text(
                                                text = key.name,
                                                style = TextStyle(
                                                    fontSize = 11.sp, 
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF334155)
                                                )
                                            )
                                            Text(
                                                text = key.solfege,
                                                style = TextStyle(
                                                    fontSize = 9.sp, 
                                                    color = Color(0xFF64748B)
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // 2. BLACK KEYS LAYER
                        blackKeys.forEach { key ->
                            val leftOffset = (whiteKeyWidth.value * (key.whiteIndexBefore + 1) - whiteKeyWidth.value * 0.3f).dp
                            var isPressed by remember { mutableStateOf(false) }
                            
                            Box(
                                modifier = Modifier
                                    .offset(x = leftOffset)
                                    .width(whiteKeyWidth * 0.6f)
                                    .fillMaxHeight(0.6f)
                                    .clip(RoundedCornerShape(bottomStart = 6.dp, bottomEnd = 6.dp))
                                    .background(if (isPressed) Color(0xFF475569) else Color(0xFF1E293B))
                                    .border(1.5.dp, Color.Black, RoundedCornerShape(bottomStart = 6.dp, bottomEnd = 6.dp))
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onPress = {
                                                isPressed = true
                                                PianoSoundPlayer.playNote(key.freq, isPianoMode = !isMeowMode)
                                                tryAwaitRelease()
                                                isPressed = false
                                            }
                                        )
                                    },
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                if (showNoteLabels) {
                                    Text(
                                        text = key.name,
                                        style = TextStyle(
                                            fontSize = 7.sp, 
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White.copy(alpha = 0.9f)
                                        ),
                                        modifier = Modifier.padding(bottom = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

