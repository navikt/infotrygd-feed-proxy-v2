package no.nav.infotrygd.feed.proxy.integration.http.interceptor

import com.nimbusds.oauth2.sdk.GrantType
import no.nav.security.token.support.client.core.ClientProperties
import no.nav.security.token.support.client.core.oauth2.OAuth2AccessTokenService
import no.nav.security.token.support.client.spring.ClientConfigurationProperties
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Component
import java.net.URI

@Component
class BearerTokenClientCredentialsClientInterceptor(
    private val oAuth2AccessTokenService: OAuth2AccessTokenService,
    private val clientConfigurationProperties: ClientConfigurationProperties,
) : ClientHttpRequestInterceptor {
    override fun intercept(
        request: HttpRequest,
        body: ByteArray,
        execution: ClientHttpRequestExecution,
    ): ClientHttpResponse {
        generateAccessToken(
            request,
            clientConfigurationProperties,
            oAuth2AccessTokenService,
        )?.let {
            request.headers.setBearerAuth(
                it,
            )
        }
        return execution.execute(request, body)
    }
}

private fun generateAccessToken(
    request: HttpRequest,
    clientConfigurationProperties: ClientConfigurationProperties,
    oAuth2AccessTokenService: OAuth2AccessTokenService,
): String? {
    val clientProperties = clientPropertiesFor(
            request.uri,
            clientConfigurationProperties,
        )
    return oAuth2AccessTokenService.getAccessToken(clientProperties).accessToken
}

/**
 * Finds client property for grantType if specified.
 *
 * If the grantType isn't specified:
 *  - Returns first client property, if there is only one
 *  - Returns client property for client_credentials or jwt_bearer
 */
private fun clientPropertiesFor(
    uri: URI,
    clientConfigurationProperties: ClientConfigurationProperties,
): ClientProperties {
    val clientProperties = filterClientProperties(clientConfigurationProperties, uri)
    return clientPropertiesForGrantType(clientProperties, uri)
}

private fun filterClientProperties(
    clientConfigurationProperties: ClientConfigurationProperties,
    uri: URI,
) = clientConfigurationProperties
    .registration
    .values
    .filter { uri.toString().startsWith(it.resourceUrl.toString()) }

private fun clientPropertiesForGrantType(
    values: List<ClientProperties>,
    uri: URI,
) =
    values.firstOrNull { GrantType.CLIENT_CREDENTIALS.equals(it.grantType) }
        ?: error("could not find oauth2 client config for uri=$uri and grant type=CLIENT_CREDENTIALS")
