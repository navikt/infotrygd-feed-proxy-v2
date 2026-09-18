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
class FpsakFeedClient(
    @Value("\${FPSAK_FEED_URL}") private val fpsvFeedUri: URI,
    @Qualifier("azureCC") restOperations: RestOperations,
) : AbstractRestClient(restOperations) {

    fun hentForeldrepengerFeed(sekvensnummer: Long): String {
        val hentForeldrepengerFeedUri = UriComponentsBuilder.fromUri(fpsvFeedUri)
            .pathSegment("fpsak/api/feed/vedtak/foreldrepenger")
            .queryParam("sistLesteSekvensId", sekvensnummer)
            .build().toUri()
        return getForEntity(hentForeldrepengerFeedUri, headers())
    }

    fun hentSvangerskapspengerFeed(sekvensnummer: Long): String {
        val hentSvangerskapspengerFeedUri = UriComponentsBuilder.fromUri(fpsvFeedUri)
            .pathSegment("fpsak/api/feed/vedtak/svangerskapspenger")
            .queryParam("sistLesteSekvensId", sekvensnummer)
            .build().toUri()
        return getForEntity(hentSvangerskapspengerFeedUri, headers())
    }

    private fun headers(): HttpHeaders = HttpHeaders().apply {
        contentType = MediaType.APPLICATION_JSON
        accept = listOf(MediaType.APPLICATION_JSON)
    }

}
