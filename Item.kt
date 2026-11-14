package com.example.lab7

import androidx.annotation.DrawableRes

data class Item(
    @DrawableRes val photo: Int, // 限定傳入有效的 Drawable 資源 ID，提升型別安全性
    val name: String,            // 水果名稱
    val price: Int               // 水果價格
)
