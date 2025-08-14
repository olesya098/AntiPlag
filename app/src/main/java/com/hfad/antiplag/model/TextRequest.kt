package com.hfad.antiplag.model

import kotlinx.serialization.Serializable

@Serializable
data class TextRequest(
    val language : String,
    val text: String
)
