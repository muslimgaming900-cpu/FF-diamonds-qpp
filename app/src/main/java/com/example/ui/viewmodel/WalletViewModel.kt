package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.model.DailyChallengeItem
import com.example.data.model.RedemptionOrder
import com.example.data.model.TransactionEntity
import com.example.data.model.UserWallet
import com.example.data.repository.WalletRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class UiEvent {
    data class ShowToast(val message: String) : UiEvent()
    data class RewardCelebration(val title: String, val amount: Int, val description: String) : UiEvent()
    data class Error(val message: String) : UiEvent()
}

class WalletViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getInstance(application)
    private val repository = WalletRepository(database)

    val wallet: StateFlow<UserWallet?> = repository.walletFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val transactions: StateFlow<List<TransactionEntity>> = repository.transactionsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val orders: StateFlow<List<RedemptionOrder>> = repository.ordersFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    // Daily Challenges derived state
    val dailyChallenges: StateFlow<List<DailyChallengeItem>> = wallet.combine(transactions) { w, _ ->
        val adsCount = w?.adsWatchedToday ?: 0
        val captchasCount = w?.captchasSolvedToday ?: 0
        listOf(
            DailyChallengeItem(
                id = "ad_challenge",
                title = "Watch 3 Video Ads",
                description = "Watch 3 sponsored video ads to claim bonus",
                current = adsCount.coerceAtMost(3),
                target = 3,
                rewardPoints = 150,
                isCompleted = adsCount >= 3,
                isClaimed = adsCount >= 3 // Auto claims bonus
            ),
            DailyChallengeItem(
                id = "captcha_challenge",
                title = "Solve 5 Captchas",
                description = "Complete 5 security tests today",
                current = captchasCount.coerceAtMost(5),
                target = 5,
                rewardPoints = 200,
                isCompleted = captchasCount >= 5,
                isClaimed = captchasCount >= 5
            ),
            DailyChallengeItem(
                id = "daily_login",
                title = "Daily Check-in",
                description = "Claim your daily login streak bonus",
                current = if (w?.lastClaimDate?.isNotBlank() == true) 1 else 0,
                target = 1,
                rewardPoints = 100,
                isCompleted = w?.lastClaimDate?.isNotBlank() == true,
                isClaimed = w?.lastClaimDate?.isNotBlank() == true
            ),
            DailyChallengeItem(
                id = "refer_friend",
                title = "Refer a Squad Mate",
                description = "Share your FF code with a friend",
                current = if (w?.referredBy != null) 1 else 0,
                target = 1,
                rewardPoints = 250,
                isCompleted = w?.referredBy != null,
                isClaimed = w?.referredBy != null
            )
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.getOrCreateWallet()
        }
    }

    fun onWatchAdCompleted(adTitle: String) {
        viewModelScope.launch {
            val reward = repository.recordAdWatched(adTitle)
            _eventFlow.emit(
                UiEvent.RewardCelebration(
                    title = "Ad Watched Successfully!",
                    amount = reward,
                    description = "+$reward Points added to your balance for watching '$adTitle'!"
                )
            )
        }
    }

    fun onCaptchaSolved(type: String) {
        viewModelScope.launch {
            val reward = repository.recordCaptchaSolved(type)
            _eventFlow.emit(
                UiEvent.RewardCelebration(
                    title = "Captcha Verified!",
                    amount = reward,
                    description = "+$reward Points credited for completing security verification!"
                )
            )
        }
    }

    fun redeemPromoCode(code: String) {
        viewModelScope.launch {
            val result = repository.redeemPromoCode(code)
            result.fold(
                onSuccess = { reward ->
                    val isSpecial = code.trim().equals("MG69", ignoreCase = true)
                    _eventFlow.emit(
                        UiEvent.RewardCelebration(
                            title = if (isSpecial) "VIP CREATOR CODE ACTIVATED!" else "Promo Code Redeemed!",
                            amount = reward,
                            description = if (isSpecial)
                                "Awesome! MG69 granted you a massive +$reward Points bonus!"
                            else
                                "Code successfully applied! +$reward Points credited to your account."
                        )
                    )
                },
                onFailure = { error ->
                    _eventFlow.emit(UiEvent.Error(error.message ?: "Failed to redeem code"))
                }
            )
        }
    }

    fun claimDailyStreak() {
        viewModelScope.launch {
            val result = repository.claimDailyStreak()
            result.fold(
                onSuccess = { reward ->
                    _eventFlow.emit(
                        UiEvent.RewardCelebration(
                            title = "Daily Streak Claimed!",
                            amount = reward,
                            description = "+$reward Points added! Come back tomorrow for an even bigger bonus!"
                        )
                    )
                },
                onFailure = { error ->
                    _eventFlow.emit(UiEvent.Error(error.message ?: "Failed to claim daily streak"))
                }
            )
        }
    }

    fun applyReferral(code: String) {
        viewModelScope.launch {
            val result = repository.applyReferralCode(code)
            result.fold(
                onSuccess = { reward ->
                    _eventFlow.emit(
                        UiEvent.RewardCelebration(
                            title = "Referral Bonus Applied!",
                            amount = reward,
                            description = "+$reward Points added to your account for using friend's invite!"
                        )
                    )
                },
                onFailure = { error ->
                    _eventFlow.emit(UiEvent.Error(error.message ?: "Invalid referral code"))
                }
            )
        }
    }

    fun submitWithdrawal(uid: String, region: String, diamonds: Int, pointsCost: Int) {
        viewModelScope.launch {
            val result = repository.createRedemptionOrder(uid, region, diamonds, pointsCost)
            result.fold(
                onSuccess = { order ->
                    _eventFlow.emit(
                        UiEvent.ShowToast("Withdrawal Order #${order.id} Submitted! Diamonds will be credited to Free Fire UID: $uid")
                    )
                },
                onFailure = { error ->
                    _eventFlow.emit(UiEvent.Error(error.message ?: "Withdrawal failed"))
                }
            )
        }
    }
}
