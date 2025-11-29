package no.nav.infotrygd.feed.proxy.integration.http.config

import no.nav.infotrygd.feed.proxy.integration.http.interceptor.BearerTokenClientCredentialsClientInterceptor
import no.nav.infotrygd.feed.proxy.integration.http.interceptor.ConsumerIdClientInterceptor
import no.nav.infotrygd.feed.proxy.integration.http.interceptor.MdcValuesPropagatingClientInterceptor
import org.springframework.boot.restclient.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.web.client.RestOperations

@Suppress("SpringFacetCodeInspection")
@Configuration
@Import(
    RestTemplateBuilderBean::class,
    ConsumerIdClientInterceptor::class,
    BearerTokenClientCredentialsClientInterceptor::class,
)
class RestTemplateAzure {
    @Bean("azureCC")
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
