package com.example.app_mobile

import androidx.annotation.DrawableRes

data class Reservation(
    val title: String,
    val description: String,
    val start: String,
    val end: String,
    @DrawableRes val imageRes: Int
)