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
class BaksFeedClient(
    @Value("\${BAKS_FEED_URL}") private val baksFeedUri: URI,
    @Qualifier("azureCC") restOperations: RestOperations,
) : AbstractRestClient(restOperations) {

    fun hentBarnetrygdFeed(sekvensnummer: Long): String {
        val hentBarnetrygdFeedUri =
            UriComponentsBuilder
                .fromUri(baksFeedUri)
                .pathSegment("api/barnetrygd/v1/feed")
                .queryParam("sistLesteSekvensId", sekvensnummer)
                .build()
                .toUri()
        return getForEntity(hentBarnetrygdFeedUri, headers())
    }

    private fun headers(): HttpHeaders =
        HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            accept = listOf(MediaType.APPLICATION_JSON)
        }

}
