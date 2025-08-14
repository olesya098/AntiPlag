package com.hfad.antiplag.data.core

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlin.coroutines.Continuation

class ClientCore {
    companion object {
        val instance: ClientCore = ClientCore()
    }
    //JSON сериализация
    val serializer = Json{
        ignoreUnknownKeys = true//игнор неизвестных ключей
        encodeDefaults = true//если есть значения можно поменять

    }
    //клиент для ктора (проводник сетевых запросов)
    val client = HttpClient(
        CIO //движок работы ктора
    ) {
        //настройка клиента Для JSON
        install(ContentNegotiation){
            json(
                serializer
            )
        }
    }
}