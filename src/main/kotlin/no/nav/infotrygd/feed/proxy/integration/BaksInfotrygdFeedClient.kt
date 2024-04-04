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

@Suppress("NonAsciiCharacters")
@Service
class BaksInfotrygdFeedClient(
    @Value("\${BAKS_INFOTRYGD_FEED_URL}") private val baksInfotrygdFeedUri: URI,
    @Qualifier("azure") restOperations: RestOperations,
) : AbstractRestClient(restOperations, "baksInfotrygdFeed") {

    fun hentBarnetrygdFeed(sekvensnummer: Long): String {
        val hentBarnetrygdFeedUri = UriComponentsBuilder.fromUri(baksInfotrygdFeedUri)
            .pathSegment("/api/barnetrygd/v1/feed")
            .queryParam("sistLesteSekvensId", sekvensnummer)
            .build().toUri()
        logger.info("Henter barnetrygd feed med URI=$hentBarnetrygdFeedUri")
        return getForEntity<String>(hentBarnetrygdFeedUri, headers()).also {
            logger.info("Hentet barnetrygd feed med URI=$hentBarnetrygdFeedUri. Kall ok")
        }
    }

    fun hentKontantstøtteFeed(sekvensnummer: Long): String {
        val hentBarnetrygdFeedUri = UriComponentsBuilder.fromUri(baksInfotrygdFeedUri)
            .pathSegment("/api/kontantstotte/v1/feed")
            .queryParam("sistLesteSekvensId", sekvensnummer)
            .build().toUri()
        logger.info("Henter kontantstotte feed med URI=$hentBarnetrygdFeedUri")
        return getForEntity<String>(hentBarnetrygdFeedUri, headers()).also {
            logger.info("Hentet kontantstotte feed med URI=$hentBarnetrygdFeedUri. Kall ok")
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
