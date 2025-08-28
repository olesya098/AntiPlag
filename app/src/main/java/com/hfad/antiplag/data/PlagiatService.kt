package com.hfad.antiplag.data

import android.R.attr.text
import android.util.Log
import com.hfad.antiplag.data.core.ClientCore
import com.hfad.antiplag.model.ReportResponse
import com.hfad.antiplag.model.SendResponse
import com.hfad.antiplag.model.StatusResponse
import com.hfad.antiplag.model.TextRequest
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.headers
const val TOKEN = "pfgk8q2FD5yMTe9pTX7iBxWMmbgmHnz3"
class PlagiatService {

    val client = ClientCore.instance.client
    suspend fun sendCheckText(language: String = "en", text: String): SendResponse {
        val response = client.post(
            "https://plagiarismcheck.org/api/v1/text"
        ) {
            headers {
                append("X-API-TOKEN", TOKEN)
                append(HttpHeaders.ContentType, "application/x-www-form-urlencoded")
            }
            setBody(FormDataContent(Parameters.build {
                append("language", language)
                append("text", text)
            }))
        }
        Log.d("PlagiatService Send", response.bodyAsText())

        return response.body()
    }

    suspend fun statusResponse(id: Int): StatusResponse {
        val statusResponse = client.get(
            "https://plagiarismcheck.org/api/v1/text/$id"
        ) {
            headers {
                append("X-API-TOKEN",TOKEN)

            }
        }
        Log.d("PlagiatService Status", "$statusResponse")

        return statusResponse.body()
    }

    suspend fun reportResponse(id: Int): ReportResponse {
        val reportResponse = client.get(
            "https://plagiarismcheck.org/api/v1/text/report/$id"
        ) {
            headers {
                append("X-API-TOKEN", TOKEN)

            }
        }
        Log.d("PlagiatService Reports", reportResponse.bodyAsText())
        return reportResponse.body()
    }


}