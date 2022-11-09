package com.kiss.mickandrorty.com.kiss.mickandrorty.model.character

import kotlinx.serialization.Serializable

@Serializable
data class CharactersResponse(
    val info: Info,
    val results: List<com.kiss.mickandrorty.model.character.Character>
)