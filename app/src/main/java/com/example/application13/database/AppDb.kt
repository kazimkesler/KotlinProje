package com.example.application13.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Product::class, Order::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun productDAO(): ProductDAO
    abstract fun orderDAO(): OrderDAO

    companion object {
        private var INSTANCE: AppDb? = null
        fun getDatabase(): AppDb {
            return INSTANCE!!
        }
        fun initDatabase(context: Context) {
            synchronized(this) {
                INSTANCE = Room
                    .databaseBuilder(context, AppDb::class.java, "database1")
                    .allowMainThreadQueries()
                    .build()
            }
        }
    }
}
