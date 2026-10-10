package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AppTopHeader
import com.example.ui.components.RewardCelebrationDialog
import com.example.ui.screens.AdsScreen
import com.example.ui.screens.CaptchaScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PromoCodeScreen
import com.example.ui.screens.RedeemScreen
import com.example.ui.screens.ReferralScreen
import com.example.ui.theme.FFBackground
import com.example.ui.theme.FFBorder
import com.example.ui.theme.FFCyanDiamond
import com.example.ui.theme.FFOrange
import com.example.ui.theme.FFSurfaceCard
import com.example.ui.theme.FFTextMuted
import com.example.ui.theme.FFTextPrimary
import com.example.ui.theme.FFTextSecondary
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.UiEvent
import com.example.ui.viewmodel.WalletViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

enum class ScreenNav {
    HOME,
    ADS,
    CAPTCHA,
    REDEEM,
    PROMO,
    REFERRALS,
    HISTORY
}

class MainActivity : ComponentActivity() {
    private val walletViewModel: WalletViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContent(viewModel = walletViewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: WalletViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var currentScreen by remember { mutableStateOf(ScreenNav.HOME) }
    var activeCelebration by remember {
        mutableStateOf<UiEvent.RewardCelebration?>(null)
    }

    val wallet by viewModel.wallet.collectAsStateWithLifecycle()
    val dailyChallenges by viewModel.dailyChallenges.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val orders by viewModel.orders.collectAsStateWithLifecycle()

    // Handle back button when not on Home screen
    BackHandler(enabled = currentScreen != ScreenNav.HOME) {
        currentScreen = ScreenNav.HOME
    }

    // Listen to ViewModel events (celebrations, errors, toasts)
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.RewardCelebration -> {
                    activeCelebration = event
                }
                is UiEvent.ShowToast -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(event.message)
                    }
                }
                is UiEvent.Error -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(event.message)
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(FFBackground)
            .testTag("main_scaffold"),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AppTopHeader(
                points = wallet?.points ?: 0,
                onPointsClick = { currentScreen = ScreenNav.HISTORY }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = FFSurfaceCard,
                contentColor = FFTextPrimary,
                tonalElevation = 8.dp,
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                NavigationBarItem(
                    selected = currentScreen == ScreenNav.HOME,
                    onClick = { currentScreen = ScreenNav.HOME },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("Home", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = FFOrange,
                        selectedTextColor = FFOrange,
                        unselectedIconColor = FFTextSecondary,
                        unselectedTextColor = FFTextSecondary,
                        indicatorColor = FFOrange.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("nav_item_home")
                )

                NavigationBarItem(
                    selected = currentScreen == ScreenNav.ADS,
                    onClick = { currentScreen = ScreenNav.ADS },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.PlayCircle,
                            contentDescription = "Ads",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("Ads +100", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = FFOrange,
                        selectedTextColor = FFOrange,
                        unselectedIconColor = FFTextSecondary,
                        unselectedTextColor = FFTextSecondary,
                        indicatorColor = FFOrange.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("nav_item_ads")
                )

                NavigationBarItem(
                    selected = currentScreen == ScreenNav.CAPTCHA,
                    onClick = { currentScreen = ScreenNav.CAPTCHA },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Captcha",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("Captcha", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = FFCyanDiamond,
                        selectedTextColor = FFCyanDiamond,
                        unselectedIconColor = FFTextSecondary,
                        unselectedTextColor = FFTextSecondary,
                        indicatorColor = FFCyanDiamond.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("nav_item_captcha")
                )

                NavigationBarItem(
                    selected = currentScreen == ScreenNav.REDEEM,
                    onClick = { currentScreen = ScreenNav.REDEEM },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Diamond,
                            contentDescription = "Redeem",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("Redeem", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = FFCyanDiamond,
                        selectedTextColor = FFCyanDiamond,
                        unselectedIconColor = FFTextSecondary,
                        unselectedTextColor = FFTextSecondary,
                        indicatorColor = FFCyanDiamond.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("nav_item_redeem")
                )

                NavigationBarItem(
                    selected = currentScreen == ScreenNav.PROMO,
                    onClick = { currentScreen = ScreenNav.PROMO },
                    icon = {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = FFOrange,
                                    contentColor = Color.White
                                ) {
                                    Text("MG69", fontSize = 8.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Black)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ConfirmationNumber,
                                contentDescription = "Promo",
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    },
                    label = { Text("Promo", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = FFOrange,
                        selectedTextColor = FFOrange,
                        unselectedIconColor = FFTextSecondary,
                        unselectedTextColor = FFTextSecondary,
                        indicatorColor = FFOrange.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("nav_item_promo")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(FFBackground)
        ) {
            when (currentScreen) {
                ScreenNav.HOME -> HomeScreen(
                    wallet = wallet,
                    dailyChallenges = dailyChallenges,
                    onNavigateToAds = { currentScreen = ScreenNav.ADS },
                    onNavigateToCaptcha = { currentScreen = ScreenNav.CAPTCHA },
                    onNavigateToRedeem = { currentScreen = ScreenNav.REDEEM },
                    onNavigateToPromo = { currentScreen = ScreenNav.PROMO },
                    onClaimStreak = { viewModel.claimDailyStreak() }
                )
                ScreenNav.ADS -> AdsScreen(
                    adsWatchedToday = wallet?.adsWatchedToday ?: 0,
                    onAdCompleted = { title -> viewModel.onWatchAdCompleted(title) }
                )
                ScreenNav.CAPTCHA -> CaptchaScreen(
                    captchasSolvedToday = wallet?.captchasSolvedToday ?: 0,
                    onCaptchaSolved = { type -> viewModel.onCaptchaSolved(type) }
                )
                ScreenNav.REDEEM -> RedeemScreen(
                    wallet = wallet,
                    orders = orders,
                    onSubmitRedemption = { uid, region, diamonds, cost ->
                        viewModel.submitWithdrawal(uid, region, diamonds, cost)
                    }
                )
                ScreenNav.PROMO -> PromoCodeScreen(
                    wallet = wallet,
                    onRedeemPromo = { code -> viewModel.redeemPromoCode(code) }
                )
                ScreenNav.REFERRALS -> ReferralScreen(
                    wallet = wallet,
                    onApplyReferral = { code -> viewModel.applyReferral(code) }
                )
                ScreenNav.HISTORY -> HistoryScreen(
                    transactions = transactions,
                    orders = orders
                )
            }
        }
    }

    // Reward Celebration Modal
    activeCelebration?.let { celebration ->
        RewardCelebrationDialog(
            title = celebration.title,
            amount = celebration.amount,
            description = celebration.description,
            onDismiss = { activeCelebration = null }
        )
    }
}
