package com.kiss.mickandrorty

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import java.io.IOException
import java.net.ConnectException

object AppHttpClient {
    val client: HttpClient by lazy {
        HttpClient(CIO) {
            expectSuccess = true
            install(Logging)
            install(ContentNegotiation) {
                json()
            }
        }
    }

    suspend inline fun <reified T> safeGet(
        urlString: String,
        onSuccess: (response: T) -> Unit,
        onError: (errorMessage: String) -> Unit
    ) {
        try {
            onSuccess(client.get(urlString).body())
        } catch (ex: ClientRequestException) {
            onError(ex.message)
        } catch (ex: ServerResponseException) {
            onError(ex.message)
        } catch (ex: RedirectResponseException) {
            onError(ex.message)
        } catch (ex: ConnectException) {
            onError(ex.message ?: "Unable to connect to server")
        } catch (ex: IOException) {
            onError(ex.message ?: "Unable to connect to server")
        }
    }
}