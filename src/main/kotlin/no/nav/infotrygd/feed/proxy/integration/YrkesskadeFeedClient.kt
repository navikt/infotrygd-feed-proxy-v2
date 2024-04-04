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
class YrkesskadeFeedClient(
    @Value("\${YRKESSKADE_FEED_URL}") private val yrkesskadeFeedUri: URI,
    @Qualifier("azure") restOperations: RestOperations,
) : AbstractRestClient(restOperations, "yrkesskadeFeed") {

    fun hentYrkesskadeFeed(sekvensnummer: Long): String {
        val hentYrkesskadeFeedUri = UriComponentsBuilder.fromUri(yrkesskadeFeedUri)
            .pathSegment("/api/yrkesskade/v1/feed")
            .queryParam("sistLesteSekvensId", sekvensnummer)
            .build().toUri()
        logger.info("Henter yrkesskade feed med URI=$hentYrkesskadeFeedUri")
        return getForEntity<String>(hentYrkesskadeFeedUri, headers()).also {
            logger.info("Hentet yrkesskade feed med URI=$hentYrkesskadeFeedUri. Kall ok")
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
