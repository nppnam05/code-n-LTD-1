package com.example.doan_nhom9_sang6.PhamNam_Activity.GioHang

import Nam_Database.Table.CartItem
import Nam_Database.Table.Product
import androidx.room.Embedded
import androidx.room.Relation

data class CartItemWithProduct(
    @Embedded val cartItem: CartItem, // Dữ liệu từ bảng CartItem
    @Relation(
        parentColumn = "productId",
        entityColumn = "id"
    )
    val product: Product // Dữ liệu từ bảng Product
)
