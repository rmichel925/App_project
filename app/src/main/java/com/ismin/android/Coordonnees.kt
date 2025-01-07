package com.ismin.android

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Coordonnees(
    @SerializedName("lon") val y: Double = 0.0,
    @SerializedName("lat") val x: Double = 0.0
): Serializable {
    operator fun get(s: Int): Double {
        return if (s == 0) x else y
    }
}
