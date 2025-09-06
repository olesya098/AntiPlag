package com.hfad.antiplag.data

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import com.hfad.antiplag.model.PlagiatCheckState
import com.hfad.antiplag.model.SendResponse
import com.hfad.antiplag.model.StatusResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlagiarismCheckManager(
    private val plagiatService: PlagiatService
) {
    private val _state = MutableStateFlow<PlagiatCheckState>(PlagiatCheckState.Idle)
    val state = _state.asStateFlow()

    suspend fun checkTextAndGetReport(text: String) {
        _state.value = PlagiatCheckState.SendText
        try {

            val sendResponse = plagiatService.sendCheckText(text = text)
            val textId = sendResponse.data?.text?.id ?: throw Exception("Error no text id")
            _state.value = PlagiatCheckState.WaitingStatus(textId)

            val finalStatus = checkStatusFive(textId)

            if (finalStatus.data.state == 5){
                val report = plagiatService.reportResponse(textId)
                _state.value = PlagiatCheckState.Success(report)
            }else{
                _state.value = PlagiatCheckState.Error("checkTextAndGetReport error")
            }


        } catch (e: Exception) {
            _state.value = PlagiatCheckState.Error("Ошибка ${e.message}")
            Log.e("PlagiarismCheckManager", "Ошибка ${e.message}")

        }

    }

    private suspend fun checkStatusFive(textId: Int): StatusResponse {
        var count = 0
        val maxCount = 20
        val time = 10000L

        while (count < maxCount) {
            val statusResponse = plagiatService.statusResponse(id = textId)
            val currentState = statusResponse.data.state
            when (currentState) {
                5 -> return statusResponse
                2, 3 -> {
                    val progress = (count * 100 / maxCount).coerceAtMost(100)
                    _state.value = PlagiatCheckState.CheckingStatus(textId = textId, progress)
                    delay(time)
                    count++
                }
                else -> throw Exception("Error checkStatusFive")
            }
        }
        throw Exception("Exceeded the number of attempts")
    }


    fun reset() {
        _state.value = PlagiatCheckState.Idle
    }
}