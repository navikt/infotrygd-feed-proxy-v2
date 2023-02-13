package no.nav.infotrygd.feed.proxy

import no.nav.infotrygd.feed.proxy.config.ApplicationConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication(exclude = [ErrorMvcAutoConfiguration::class])
class ApplicationLocal

fun main(args: Array<String>) {
    SpringApplicationBuilder(ApplicationConfig::class.java)
        .profiles("local")
        .run(*args)
}
