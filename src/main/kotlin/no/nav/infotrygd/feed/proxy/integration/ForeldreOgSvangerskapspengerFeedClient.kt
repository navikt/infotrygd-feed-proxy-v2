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
class ForeldreOgSvangerskapspengerFeedClient(
    @Value("\${FPSAK_FEED_URL}") private val fpsvFeedUri: URI,
    @Qualifier("azureCC") restOperations: RestOperations,
) : AbstractRestClient(restOperations) {

    fun hentForeldrepengerFeed(sekvensnummer: Long): String {
        val hentForeldrepengerFeedUri = UriComponentsBuilder.fromUri(fpsvFeedUri)
            .pathSegment("/fpsak/api/feed/vedtak/foreldrepenger")
            .queryParam("sistLesteSekvensId", sekvensnummer)
            .build().toUri()
        logger.info("Henter foreldrepenger feed med URI=$hentForeldrepengerFeedUri")
        return getForEntity<String>(hentForeldrepengerFeedUri, headers()).also {
            logger.info("Hentet foreldrepenger feed med URI=$hentForeldrepengerFeedUri. Kall ok")
        }
    }

    fun hentSvangerskapspengerFeed(sekvensnummer: Long): String {
        val hentSvangerskapspengerFeedUri = UriComponentsBuilder.fromUri(fpsvFeedUri)
            .pathSegment("/fpsak/api/feed/vedtak/svangerskapspenger")
            .queryParam("sistLesteSekvensId", sekvensnummer)
            .build().toUri()
        logger.info("Henter svangerskapspenger feed med URI=$hentSvangerskapspengerFeedUri")
        return getForEntity<String>(hentSvangerskapspengerFeedUri, headers()).also {
            logger.info("Hentet svangerskapspenger feed med URI=$hentSvangerskapspengerFeedUri. Kall ok")
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
