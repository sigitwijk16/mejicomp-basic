package com.gitz.mejicomp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Parts(
    val title: String,
    val description: String,
    val image: Int,
    val price: Int
) : Parcelable
