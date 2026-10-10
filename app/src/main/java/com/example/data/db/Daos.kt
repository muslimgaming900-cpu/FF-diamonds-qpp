package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.RedemptionOrder
import com.example.data.model.TransactionEntity
import com.example.data.model.UserWallet
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDao {
    @Query("SELECT * FROM user_wallet WHERE id = 1")
    fun getWalletFlow(): Flow<UserWallet?>

    @Query("SELECT * FROM user_wallet WHERE id = 1")
    suspend fun getWallet(): UserWallet?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(wallet: UserWallet)

    @Update
    suspend fun update(wallet: UserWallet)
}

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity)
}

@Dao
interface RedemptionDao {
    @Query("SELECT * FROM redemption_orders ORDER BY timestamp DESC")
    fun getAllOrders(): Flow<List<RedemptionOrder>>

    @Query("SELECT * FROM redemption_orders WHERE id = :orderId")
    suspend fun getOrderById(orderId: String): RedemptionOrder?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: RedemptionOrder)

    @Update
    suspend fun update(order: RedemptionOrder)
}
