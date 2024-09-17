package no.nav.infotrygd.feed.proxy.config

import no.nav.infotrygd.feed.proxy.integration.http.config.RestTemplateBuilderBean
import no.nav.infotrygd.feed.proxy.integration.http.interceptor.ConsumerIdClientInterceptor
import no.nav.infotrygd.feed.proxy.integration.http.interceptor.MdcValuesPropagatingClientInterceptor
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.web.client.RestOperations

@Suppress("SpringFacetCodeInspection")
@Configuration
@Import(RestTemplateBuilderBean::class, ConsumerIdClientInterceptor::class)
class RestTemplateSts {
    @Bean("noToken")
    fun restTemplateNoToken(
        restTemplateBuilder: RestTemplateBuilder,
        consumerIdClientInterceptor: ConsumerIdClientInterceptor,
    ): RestOperations =
        restTemplateBuilder
            .additionalInterceptors(
                consumerIdClientInterceptor,
                MdcValuesPropagatingClientInterceptor(),
            ).build()
}
