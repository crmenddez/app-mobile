package com.example.app_mobile.reservation

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReservationDetail(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val modality: String,
    @DrawableRes val imageRes: Int
) : Parcelable
