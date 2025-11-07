package com.example.lab_week_09.ui.theme

import androidx.compose.foundation.layout.add
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object MoshiAdapter {
    val moshi: Moshi = Moshi.Builder()
        // Tambahkan KotlinJsonAdapterFactory agar Moshi bisa bekerja dengan class Kotlin
        .add(KotlinJsonAdapterFactory())
        .build()
}