package no.nav.infotrygd.feed.proxy.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    private val bearer = "Bearer"

    @Bean
    fun openApi(): OpenAPI =
        OpenAPI()
            .info(Info().title("Infotrygd Feed Proxy API"))
            .components(Components().addSecuritySchemes(bearer, bearerTokenSecurityScheme()))
            .addSecurityItem(SecurityRequirement().addList(bearer, listOf("read", "write")))

    private fun bearerTokenSecurityScheme(): SecurityScheme =
        SecurityScheme()
            .type(SecurityScheme.Type.APIKEY)
            .scheme("bearer")
            .bearerFormat("JWT")
            .`in`(SecurityScheme.In.HEADER)
            .name("Authorization")
}
