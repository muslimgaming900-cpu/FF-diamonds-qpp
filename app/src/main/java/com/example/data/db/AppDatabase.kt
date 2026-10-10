package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.RedemptionOrder
import com.example.data.model.TransactionEntity
import com.example.data.model.UserWallet

@Database(
    entities = [
        UserWallet::class,
        TransactionEntity::class,
        RedemptionOrder::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun walletDao(): WalletDao
    abstract fun transactionDao(): TransactionDao
    abstract fun redemptionDao(): RedemptionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ff_diamonds_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
