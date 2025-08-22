package com.hfad.antiplag.data

import androidx.compose.runtime.mutableStateMapOf
import com.hfad.antiplag.model.PlagiatCheckState
import com.hfad.antiplag.model.StatusResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlagiarismCheckManager(
    private val plagiatService: PlagiatService
) {
    private val _state = MutableStateFlow<PlagiatCheckState>(PlagiatCheckState.Idle)
    val state = _state.asStateFlow()
    suspend fun checkText(text: String) {
        _state.value = PlagiatCheckState.SendText
        try {
            val sendResponse =
                plagiatService.sendCheckText(text = text)//отправка текста на проверку
            val textId = sendResponse.data?.text?.id
            textId?.let { _state.value = PlagiatCheckState.WaitingStatus(it) }

            var statusResponse: StatusResponse? = null
            var count = 0
            val maxCount = 30
            do {
                delay(10000L)
                count++
                statusResponse = textId?.let { plagiatService.statusResponse(it) }
                textId?.let {
                    _state.value = PlagiatCheckState.CheckingStatus(
                        it, progress = (count * 100 / maxCount).coerceAtMost(100)
                    )
                }

            } while (statusResponse?.data?.state != 5 && count < maxCount)
            if (statusResponse?.data?.state != 5) {
                _state.value = PlagiatCheckState.Error(" Timeout, Waiting Plagiarism is over")
                return
            }

            val report = textId?.let { plagiatService.reportResponse(it) }
            report?.let { _state.value = PlagiatCheckState.Success(it) }

        } catch (e: Exception) {
            _state.value = PlagiatCheckState.Error(e.message.toString())

        }
    }

    fun reset() {
        _state.value = PlagiatCheckState.Idle
    }
}