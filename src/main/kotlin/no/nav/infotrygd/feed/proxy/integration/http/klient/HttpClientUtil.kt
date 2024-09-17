package no.nav.infotrygd.feed.proxy.integration.http.klient

import java.net.http.HttpClient
import java.time.Duration

object HttpClientUtil {
    fun create(): HttpClient =
        HttpClient
            .newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build()
}
