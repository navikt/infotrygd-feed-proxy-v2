package no.nav.infotrygd.feed.proxy.integration

import no.nav.infotrygd.feed.proxy.integration.http.klient.AbstractRestClient
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
class BarnetrygdKontantstotteFeedClient(
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
        logger.info("Henter barnetrygd feed med URI=$hentBarnetrygdFeedUri")
        return getForEntity<String>(hentBarnetrygdFeedUri, headers()).also {
            logger.info("Hentet barnetrygd feed med URI=$hentBarnetrygdFeedUri. Kall ok")
        }
    }

    fun hentKontantstotteFeed(sekvensnummer: Long): String {
        val hentKontantstotteFeedUri =
            UriComponentsBuilder
                .fromUri(baksFeedUri)
                .pathSegment("/api/kontantstotte/v1/feed")
                .queryParam("sistLesteSekvensId", sekvensnummer)
                .build()
                .toUri()
        logger.info("Henter kontantstotte feed med URI=$hentKontantstotteFeedUri")
        return getForEntity<String>(hentKontantstotteFeedUri, headers()).also {
            logger.info("Hentet kontantstotte feed med URI=$hentKontantstotteFeedUri. Kall ok")
        }
    }

    private fun headers(): HttpHeaders =
        HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            accept = listOf(MediaType.APPLICATION_JSON)
        }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}
