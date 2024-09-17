package no.nav.infotrygd.feed.proxy.integration.http.config

import no.nav.infotrygd.feed.proxy.integration.http.interceptor.*
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.web.client.RestOperations

@Suppress("SpringFacetCodeInspection")
@Configuration
@Import(
    RestTemplateBuilderBean::class,
    ConsumerIdClientInterceptor::class,
    BearerTokenClientInterceptor::class,
    BearerTokenClientCredentialsClientInterceptor::class,
    BearerTokenOnBehalfOfClientInterceptor::class,
)
class RestTemplateAzure {
    @Bean("azure")
    fun restTemplateJwtBearer(
        restTemplateBuilder: RestTemplateBuilder,
        consumerIdClientInterceptor: ConsumerIdClientInterceptor,
        bearerTokenClientInterceptor: BearerTokenClientInterceptor,
    ): RestOperations =
        restTemplateBuilder
            .additionalInterceptors(
                consumerIdClientInterceptor,
                bearerTokenClientInterceptor,
                MdcValuesPropagatingClientInterceptor(),
            ).build()

    @Bean("azureClientCredential")
    fun restTemplateClientCredentialBearer(
        restTemplateBuilder: RestTemplateBuilder,
        consumerIdClientInterceptor: ConsumerIdClientInterceptor,
        bearerTokenClientInterceptor: BearerTokenClientCredentialsClientInterceptor,
    ): RestOperations =
        restTemplateBuilder
            .additionalInterceptors(
                consumerIdClientInterceptor,
                bearerTokenClientInterceptor,
                MdcValuesPropagatingClientInterceptor(),
            ).build()
}
