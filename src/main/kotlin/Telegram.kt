package org.example

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

fun main(args: Array<String>) {
    val botToken = args[0]

    val urlGetMe = "https://api.telegram.org/bot$botToken/getMe"
    val urlGetUpdates = "https://api.telegram.org/bot$botToken/getUpdates"

    val client: HttpClient = HttpClient.newBuilder().build()

    val request: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlGetMe)).build()
    val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())
    println(response.body())

    val requestUpdates: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlGetUpdates)).build()
    val responseUpdates: HttpResponse<String> = client.send(requestUpdates, HttpResponse.BodyHandlers.ofString())
    println(responseUpdates.body())
}