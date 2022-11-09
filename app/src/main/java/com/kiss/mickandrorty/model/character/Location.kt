package com.kiss.mickandrorty.com.kiss.mickandrorty.model.character

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Location(
    val name: String,
    val url: String
) : Parcelable