package com.todoapp.data.api

import com.todoapp.data.util.AppJson
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

class ApiClient {
  val httpClient: HttpClient = HttpClient {
    install(ContentNegotiation) {
      json(AppJson)
    }
  }
}
