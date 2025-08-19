package com.hfad.antiplag.model

sealed class PlagiatCheckState{
    data object Idle: PlagiatCheckState()
    data object SendText: PlagiatCheckState()

    data class WaitingStatus(
        val textId: Int
    ): PlagiatCheckState()

    data class CheckingStatus(
        val textId: Int,
        val progress: Int
    ): PlagiatCheckState()

    data class Success(
        val report: ReportResponse
    ): PlagiatCheckState()

    data class Error(
        val message: String
    ): PlagiatCheckState()
}