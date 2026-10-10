package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.FFBackground
import com.example.ui.theme.FFBorder
import com.example.ui.theme.FFCyanDiamond
import com.example.ui.theme.FFGreenSuccess
import com.example.ui.theme.FFOrange
import com.example.ui.theme.FFRedError
import com.example.ui.theme.FFSurface
import com.example.ui.theme.FFSurfaceCard
import com.example.ui.theme.FFSurfaceVariant
import com.example.ui.theme.FFTextMuted
import com.example.ui.theme.FFTextPrimary
import com.example.ui.theme.FFTextSecondary
import kotlin.random.Random

@Composable
fun CaptchaScreen(
    captchasSolvedToday: Int,
    onCaptchaSolved: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Text Code", "Image Grid", "Math Quiz", "Slider Lock")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(FFBackground)
            .testTag("captcha_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Captcha Header Status
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("captcha_status_header_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FFSurfaceCard),
                border = BorderStroke(1.dp, FFCyanDiamond.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = FFCyanDiamond,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "REAL CAPTCHA SOLVER",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = FFCyanDiamond,
                                letterSpacing = 1.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Earn 100 Points Per Captcha",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = FFTextPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Solved today: $captchasSolvedToday / 50 tasks",
                            fontSize = 12.sp,
                            color = FFTextSecondary
                        )
                    }

                    Surface(
                        color = FFCyanDiamond.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, FFCyanDiamond)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "+100",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = FFCyanDiamond
                            )
                            Text(
                                text = "PTS / TASK",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = FFTextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Tab Selector for Captcha Modes
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = FFSurface,
                contentColor = FFCyanDiamond,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = FFCyanDiamond
                    )
                },
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) FFCyanDiamond else FFTextSecondary
                            )
                        }
                    )
                }
            }
        }

        // Active Captcha Component
        item {
            when (selectedTab) {
                0 -> TextCodeCaptchaCard(onSolved = { onCaptchaSolved("Text Code") })
                1 -> ImageGridCaptchaCard(onSolved = { onCaptchaSolved("Image Grid") })
                2 -> MathQuizCaptchaCard(onSolved = { onCaptchaSolved("Math Quiz") })
                3 -> SliderCaptchaCard(onSolved = { onCaptchaSolved("Slider Lock") })
            }
        }

        // Help Info Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("captcha_info_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FFSurfaceVariant.copy(alpha = 0.4f)),
                border = BorderStroke(1.dp, FFBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Why Captchas in FF Diamonds?",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = FFTextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Real captchas prevent automated bots and secure server funds for genuine gamers. Each verified test instantly credits 100 points to your account balance.",
                        fontSize = 11.sp,
                        color = FFTextSecondary,
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }
}

// 1. Text Canvas Captcha
@Composable
fun TextCodeCaptchaCard(onSolved: () -> Unit) {
    fun generateRandomCaptcha(): String {
        val chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
        return (1..5).map { chars.random() }.joinToString("")
    }

    var captchaText by remember { mutableStateOf(generateRandomCaptcha()) }
    var userInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successNotice by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("text_captcha_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = FFSurface),
        border = BorderStroke(1.dp, FFBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Type the security characters shown below",
                fontSize = 13.sp,
                color = FFTextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Authentic Distorted Captcha Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(84.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0F172A))
                    .border(BorderStroke(1.dp, FFBorder), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Draw noise grid lines
                    val random = Random(captchaText.hashCode())
                    for (i in 0 until 12) {
                        drawLine(
                            color = Color(0xFF334155).copy(alpha = 0.6f),
                            start = Offset(random.nextFloat() * w, random.nextFloat() * h),
                            end = Offset(random.nextFloat() * w, random.nextFloat() * h),
                            strokeWidth = 2f
                        )
                    }

                    // Draw noise dots
                    for (i in 0 until 40) {
                        drawCircle(
                            color = Color(0xFF475569).copy(alpha = 0.5f),
                            radius = 2.5f,
                            center = Offset(random.nextFloat() * w, random.nextFloat() * h)
                        )
                    }

                    // Render distorted characters
                    val paint = android.graphics.Paint().apply {
                        isAntiAlias = true
                        textSize = 58f
                        typeface = android.graphics.Typeface.DEFAULT_BOLD
                        color = android.graphics.Color.argb(255, 0, 229, 255)
                    }

                    val charWidth = w / (captchaText.length + 1)
                    captchaText.forEachIndexed { idx, ch ->
                        val charX = charWidth * (idx + 0.8f)
                        val charY = h * 0.68f + (random.nextFloat() * 10 - 5)
                        val angle = (random.nextFloat() * 24 - 12)

                        drawContext.canvas.nativeCanvas.save()
                        drawContext.canvas.nativeCanvas.rotate(angle, charX, charY)
                        drawContext.canvas.nativeCanvas.drawText(ch.toString(), charX, charY, paint)
                        drawContext.canvas.nativeCanvas.restore()
                    }
                }

                // Refresh Button
                IconButton(
                    onClick = {
                        captchaText = generateRandomCaptcha()
                        userInput = ""
                        errorMessage = null
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .testTag("refresh_captcha_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh Code",
                        tint = FFTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = userInput,
                onValueChange = {
                    userInput = it.uppercase()
                    errorMessage = null
                },
                placeholder = { Text("Enter 5-character code", color = FFTextMuted) },
                singleLine = true,
                isError = errorMessage != null,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FFCyanDiamond,
                    unfocusedBorderColor = FFBorder,
                    focusedTextColor = FFTextPrimary,
                    unfocusedTextColor = FFTextPrimary
                ),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Characters,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (userInput.trim() == captchaText) {
                            captchaText = generateRandomCaptcha()
                            userInput = ""
                            errorMessage = null
                            onSolved()
                        } else {
                            errorMessage = "Incorrect code! Please try again."
                        }
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("captcha_text_input")
            )

            errorMessage?.let {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = it,
                    fontSize = 12.sp,
                    color = FFRedError,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = {
                    if (userInput.trim() == captchaText) {
                        captchaText = generateRandomCaptcha()
                        userInput = ""
                        errorMessage = null
                        onSolved()
                    } else {
                        errorMessage = "Incorrect code! Please try again."
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = FFCyanDiamond),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("verify_captcha_button")
            ) {
                Text(
                    text = "VERIFY & CLAIM 100 POINTS",
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp,
                    color = Color.Black
                )
            }
        }
    }
}

// 2. Real Image Grid Captcha (Select All Matching Objects)
@Composable
fun ImageGridCaptchaCard(onSolved: () -> Unit) {
    val items = remember {
        mutableStateListOf(
            "Medkit", "Airdrop", "Helmet",
            "Helmet", "Vest", "Gloo Wall",
            "Helmet", "Ammo", "Sniper"
        )
    }
    val targetType = "Helmet"
    val selectedIndices = remember { mutableStateListOf<Int>() }
    var resultError by remember { mutableStateOf<String?>(null) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("image_grid_captcha_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = FFSurface),
        border = BorderStroke(1.dp, FFBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // reCAPTCHA styled prompt header
            Surface(
                color = Color(0xFF1D4ED8),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Select all squares with",
                        fontSize = 12.sp,
                        color = Color.White
                    )
                    Text(
                        text = targetType.uppercase() + "S",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "Click verify once none are left.",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 3x3 Tile Grid
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                for (row in 0 until 3) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (col in 0 until 3) {
                            val index = row * 3 + col
                            val item = items[index]
                            val isSelected = selectedIndices.contains(index)

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) Color(0xFF1E3A8A) else FFSurfaceVariant)
                                    .border(
                                        BorderStroke(
                                            if (isSelected) 2.dp else 1.dp,
                                            if (isSelected) FFCyanDiamond else FFBorder
                                        ),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable {
                                        if (isSelected) selectedIndices.remove(index) else selectedIndices.add(index)
                                        resultError = null
                                    }
                                    .testTag("grid_captcha_tile_$index"),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = item,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) FFCyanDiamond else FFTextPrimary,
                                        textAlign = TextAlign.Center
                                    )
                                    if (isSelected) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = FFCyanDiamond,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            resultError?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    fontSize = 12.sp,
                    color = FFRedError,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = {
                    val correctIndices = items.mapIndexedNotNull { i, s -> if (s == targetType) i else null }
                    if (selectedIndices.toSet() == correctIndices.toSet()) {
                        selectedIndices.clear()
                        resultError = null
                        onSolved()
                    } else {
                        resultError = "Selection incorrect! Please find all ${targetType}s."
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = FFOrange),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("verify_grid_captcha_button")
            ) {
                Text(
                    text = "VERIFY SELECTION (+100 PTS)",
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp,
                    color = Color.White
                )
            }
        }
    }
}

// 3. Math Challenge Captcha
@Composable
fun MathQuizCaptchaCard(onSolved: () -> Unit) {
    var num1 by remember { mutableIntStateOf((10..40).random()) }
    var num2 by remember { mutableIntStateOf((5..25).random()) }
    var mathInput by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }

    val correctAnswer = num1 + num2

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("math_captcha_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = FFSurface),
        border = BorderStroke(1.dp, FFBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Anti-Bot Fast Mental Math",
                fontSize = 13.sp,
                color = FFTextSecondary
            )

            Spacer(modifier = Modifier.height(14.dp))

            Surface(
                color = FFSurfaceVariant,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, FFBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$num1 + $num2 = ?",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = FFCyanDiamond,
                        letterSpacing = 2.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = mathInput,
                onValueChange = {
                    mathInput = it
                    errorText = null
                },
                placeholder = { Text("Enter answer", color = FFTextMuted) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FFCyanDiamond,
                    unfocusedBorderColor = FFBorder,
                    focusedTextColor = FFTextPrimary,
                    unfocusedTextColor = FFTextPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("math_answer_input")
            )

            errorText?.let {
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = it, fontSize = 12.sp, color = FFRedError)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = {
                    if (mathInput.trim() == correctAnswer.toString()) {
                        num1 = (10..40).random()
                        num2 = (5..25).random()
                        mathInput = ""
                        errorText = null
                        onSolved()
                    } else {
                        errorText = "Wrong answer! Please calculate again."
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = FFOrange),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("verify_math_captcha_button")
            ) {
                Text(
                    text = "SUBMIT ANSWER (+100 PTS)",
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp,
                    color = Color.White
                )
            }
        }
    }
}

// 4. Slider Puzzle Captcha
@Composable
fun SliderCaptchaCard(onSolved: () -> Unit) {
    val targetPosition = 75f
    var sliderValue by remember { mutableFloatStateOf(0f) }
    var isVerified by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("slider_captcha_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = FFSurface),
        border = BorderStroke(1.dp, FFBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Drag the slider to align the security lock",
                fontSize = 13.sp,
                color = FFTextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0F172A))
                    .border(BorderStroke(1.dp, FFBorder), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.CenterStart
            ) {
                // Target slot indicator
                Box(
                    modifier = Modifier
                        .fillMaxWidth(targetPosition / 100f)
                        .padding(end = 10.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Surface(
                        color = Color(0xFF1E293B),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, FFCyanDiamond),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("🔒", fontSize = 16.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Slider(
                value = sliderValue,
                onValueChange = {
                    sliderValue = it
                    if (kotlin.math.abs(sliderValue - targetPosition) <= 4f) {
                        isVerified = true
                    }
                },
                valueRange = 0f..100f,
                colors = SliderDefaults.colors(
                    thumbColor = if (isVerified) FFGreenSuccess else FFCyanDiamond,
                    activeTrackColor = FFCyanDiamond,
                    inactiveTrackColor = FFBorder
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("puzzle_slider")
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = {
                    if (isVerified) {
                        sliderValue = 0f
                        isVerified = false
                        onSolved()
                    }
                },
                enabled = isVerified,
                colors = ButtonDefaults.buttonColors(
                    containerColor = FFGreenSuccess,
                    disabledContainerColor = FFSurfaceVariant
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("verify_slider_captcha_button")
            ) {
                Text(
                    text = if (isVerified) "LOCK ALIGNED! CLAIM 100 PTS" else "DRAG SLIDER TO TARGET",
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp,
                    color = if (isVerified) Color.Black else FFTextMuted
                )
            }
        }
    }
}
