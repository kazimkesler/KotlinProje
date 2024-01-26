package com.example.application13.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true) val uid: Long,
    @ColumnInfo(name = "productId") val productId: Long,
    @ColumnInfo(name = "quantity") val quantity: Int,
    @ColumnInfo(name = "date") val date: String,
    )