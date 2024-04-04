package no.nav.infotrygd.feed.proxy.integration

import no.nav.familie.http.client.AbstractRestClient
import org.slf4j.LoggerFactory
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
    @Qualifier("azure") restOperations: RestOperations,
) : AbstractRestClient(restOperations, "vedtaksfeed") {

    fun hentSykepengerFeed(sekvensnummer: Long): String {
        val hentSykepengerFeedUri = UriComponentsBuilder.fromUri(sykepengerFeedUri)
            .pathSegment("/feed")
            .queryParam("sistLesteSekvensId", sekvensnummer)
            .build().toUri()

        logger.info("Henter sykepenger feed med URI=$hentSykepengerFeedUri")
        return getForEntity<String>(hentSykepengerFeedUri, headers()).also {
            logger.info("Hentet sykepenger feed med URI=$hentSykepengerFeedUri. Kall ok")
        }
    }

    private fun headers(): HttpHeaders = HttpHeaders().apply {
        contentType = MediaType.APPLICATION_JSON
        accept = listOf(MediaType.APPLICATION_JSON)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}
