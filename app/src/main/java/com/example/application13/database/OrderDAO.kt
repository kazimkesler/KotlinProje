package com.example.application13.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface OrderDAO {
    @Query("SELECT * FROM orders")
    fun getAll(): List<Order>

    @Query("SELECT * FROM orders WHERE uid IN (:orderIds)")
    fun loadAllByIds(orderIds: IntArray): List<Order>

    @Query("DELETE FROM orders WHERE productId = (:productId)")
    fun deleteByProduct(productId: Long)

    @Insert
    fun insert(vararg orders: Order)

    @Update
    fun update(vararg orders: Order)

    @Delete
    fun delete(orders: Order)
}