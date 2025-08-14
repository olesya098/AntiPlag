package com.hfad.antiplag.data

import android.R.attr.text
import com.hfad.antiplag.data.core.ClientCore
import com.hfad.antiplag.model.ReportResponse
import com.hfad.antiplag.model.SendResponse
import com.hfad.antiplag.model.StatusResponse
import com.hfad.antiplag.model.TextRequest
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.headers

class PlagiatService {
    val client = ClientCore.instance.client
    suspend fun sendCheckText(language: String = "en", text: String) : SendResponse{
        val response = client.post(
            "https://plagiarismcheck.org/api/v1/text"
        ) {
            headers { append(HttpHeaders.Authorization, "X-API-TOKEN: Pb-K3tC_-BPILFkHwmsdKvWrtGVikRsY" ) }
            setBody(TextRequest(language, text))
        }
        return response.body()
    }
    suspend fun statusResponse(id: Int) : StatusResponse{
        val statusResponse = client.get(
            "https://plagiarismcheck.org/api/v1/text/$id"
        ) {
            headers { append(HttpHeaders.Authorization, "X-API-TOKEN: Pb-K3tC_-BPILFkHwmsdKvWrtGVikRsY" ) }
        }
        return statusResponse.body()
    }
    suspend fun reportResponse(id: Int) : ReportResponse{
        val reportResponse = client.get(
            "https://plagiarismcheck.org/api/v1/text/report/$id"
        ) {
            headers { append(HttpHeaders.Authorization, "X-API-TOKEN: Pb-K3tC_-BPILFkHwmsdKvWrtGVikRsY" ) }
        }
        return reportResponse.body()
    }


}