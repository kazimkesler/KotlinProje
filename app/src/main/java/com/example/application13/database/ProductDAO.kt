package com.example.application13.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductDAO {
    @Query("SELECT * FROM products")
    fun getAll(): List<Product>

    @Query("SELECT * FROM products WHERE uid IN (:productIds)")
    fun loadAllByIds(productIds: IntArray): List<Product>

    @Insert
    fun insert(vararg products: Product): LongArray

    @Update
    fun update(vararg products: Product)

    @Delete
    fun delete(product: Product)
}