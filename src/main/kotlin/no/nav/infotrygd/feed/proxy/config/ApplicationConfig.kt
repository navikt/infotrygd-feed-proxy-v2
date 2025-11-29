package no.nav.infotrygd.feed.proxy.config

import no.nav.infotrygd.feed.proxy.integration.http.config.RestTemplateAzure
import no.nav.infotrygd.feed.proxy.integration.http.interceptor.ConsumerIdClientInterceptor
import no.nav.infotrygd.feed.proxy.integration.http.interceptor.MdcValuesPropagatingClientInterceptor
import no.nav.infotrygd.feed.proxy.integration.http.mapper.objectMapper
import no.nav.security.token.support.client.spring.oauth2.EnableOAuth2Client
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.web.client.RestOperations

@SpringBootConfiguration
@ConfigurationPropertiesScan
@ComponentScan("no.nav.infotrygd.feed.proxy")
@EnableJwtTokenValidation(ignore = ["org.springframework", "org.springdoc"])
@Import(RestTemplateAzure::class)
@EnableOAuth2Client(cacheEnabled = true)
class ApplicationConfig {

    @Bean
    @Primary
    fun objectMapper() = objectMapper

    @Bean
    fun restOperations(
        restTemplateBuilder: RestTemplateBuilder,
        consumerIdClientInterceptor: ConsumerIdClientInterceptor,
    ): RestOperations =
        restTemplateBuilder
            .additionalInterceptors(
                consumerIdClientInterceptor,
                MdcValuesPropagatingClientInterceptor(),
            ).build()
}
