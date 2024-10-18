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
class Inst2FeedClient (
    @Value("\${INST2_URL}") private val inst2Uri: URI,
    @Qualifier("azureCC") restOperations: RestOperations,
) : AbstractRestClient(restOperations) {

    fun hentInstitusjonsoppholdFeed(callId: String,
                                    consumerId: String,
                                    sekvensnummer: Long): String {
        val hentInstOppholdFeedUri =
            UriComponentsBuilder
                .fromUri(inst2Uri)
                .pathSegment("api/v1/hendelse/after-id/")
                .queryParam("antall-hendelser", sekvensnummer)
                .build()
                .toUri()
        logger.info("Henter institusjonsopphold feed med URI=$hentInstOppholdFeedUri")
        return getForEntity<String>(hentInstOppholdFeedUri, headers(callId, consumerId)).also {
            logger.info("Hentet institusjonsopphold feed med URI=$hentInstOppholdFeedUri. Kall ok")
        }
    }

    private fun headers(callId : String,
                        consumerId : String): HttpHeaders = HttpHeaders().apply {
                            contentType = MediaType.APPLICATION_JSON
                            accept = listOf(MediaType.APPLICATION_JSON)
                            set("Nav-Call-Id", callId)
                            set("Nav-Consumer-Id", consumerId)
        }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}