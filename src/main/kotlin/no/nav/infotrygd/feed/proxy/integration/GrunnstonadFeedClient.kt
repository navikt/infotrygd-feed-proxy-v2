package no.nav.infotrygd.feed.proxy.integration

import no.nav.infotrygd.feed.proxy.integration.http.klient.AbstractRestClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestOperations
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Service
@Profile("dev")
class GrunnstonadFeedClient(
    @Value("\${GRUNN_OG_HJELP_URL}") private val grunnOgHjelpFeedUri: URI,
    @Qualifier("azureCC") restOperations: RestOperations,
) : AbstractRestClient(restOperations) {
    fun hentGrunnstonadFeed(sekvensnummer: Long): String {
        val hentGrunnstonadFeedUri =
            UriComponentsBuilder
                .fromUri(grunnOgHjelpFeedUri)
                .pathSegment("api/grunnstonad/v1/feed")
                .queryParam("sistLesteSekvensId", sekvensnummer)
                .build()
                .toUri()
        return getForEntity(hentGrunnstonadFeedUri, headers())
    }

    private fun headers(): HttpHeaders =
        HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            accept = listOf(MediaType.APPLICATION_JSON)
        }
}
