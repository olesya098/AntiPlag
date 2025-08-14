package com.hfad.antiplag.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendResponse(
    val success: Boolean,
    val data: Data
    )

@Serializable
data class Data(
    val text: TextData,
    val charged: Int,
    @SerialName("bonus_charged")
    val bonusCharged: Int
)

@Serializable
data class TextData(
    val id: Int,
    val filename: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("submitted_at")
    val submittedAt: String,
    @SerialName("is_deleted")
    val isDeleted: Boolean,
    @SerialName("deleted_at")
    val deletedAt: String? = null,
    val state: Int,
    val language: String,
    val pages: Int,
    @SerialName("group_id")
    val groupId: Int? = null,
    @SerialName("user_id")
    val userId: Int,
    @SerialName("report_id")
    val reportId: Int?  = null,
)

