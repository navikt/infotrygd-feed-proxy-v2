package no.nav.infotrygd.feed.proxy

import no.nav.infotrygd.feed.proxy.config.ApplicationConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication
class ApplicationLocal

fun main(args: Array<String>) {
    SpringApplicationBuilder(ApplicationConfig::class.java)
        .profiles("local")
        .run(*args)
}
