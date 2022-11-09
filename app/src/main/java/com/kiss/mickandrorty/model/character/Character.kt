package com.kiss.mickandrorty.model.character

import android.os.Parcelable
import com.kiss.mickandrorty.com.kiss.mickandrorty.model.character.Location
import com.kiss.mickandrorty.com.kiss.mickandrorty.model.character.Origin
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Character(
    val created: String,
    val episode: List<String>,
    val gender: String,
    val id: Int,
    val image: String,
    val location: Location,
    val name: String,
    val origin: Origin,
    val species: String,
    val status: String,
    val type: String,
    val url: String
) : Parcelable