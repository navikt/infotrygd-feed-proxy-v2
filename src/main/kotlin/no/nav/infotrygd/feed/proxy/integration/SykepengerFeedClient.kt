package no.nav.infotrygd.feed.proxy.integration

import no.nav.infotrygd.feed.proxy.integration.http.klient.AbstractRestClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestOperations
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Service
class SykepengerFeedClient(
    @Value("\${SYKEPENGER_FEED_URL}") private val sykepengerFeedUri: URI,
    @Qualifier("azureCC") restOperations: RestOperations,
) : AbstractRestClient(restOperations) {
    fun hentSykepengerFeed(sekvensnummer: Long): String {
        val hentSykepengerFeedUri =
            UriComponentsBuilder
                .fromUri(sykepengerFeedUri)
                .pathSegment("feed")
                .queryParam("sistLesteSekvensId", sekvensnummer)
                .build()
                .toUri()

        return getForEntity(hentSykepengerFeedUri, headers())
    }

    private fun headers(): HttpHeaders =
        HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            accept = listOf(MediaType.APPLICATION_JSON)
        }

}
