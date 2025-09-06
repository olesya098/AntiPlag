package com.hfad.antiplag.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StatusResponse(
    val data: StatusData
)
@Serializable
data class StatusData(
    val id: Int,
    val filename: String,
    val state: Int,
    val language: String,
    val pages: Int,
    val report: Report? = null,

)
@Serializable
data class Report(
    val id: Int,
    @SerialName("source_count")
    val sourceCount: Int,
    val percent: String,
    @SerialName("text_id")
    val textId: Int? = null
)