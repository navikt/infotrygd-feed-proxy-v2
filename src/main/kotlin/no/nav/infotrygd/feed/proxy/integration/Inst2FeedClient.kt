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
import java.util.UUID

@Service
class Inst2FeedClient (
    @Value("\${INST2_URL}") private val inst2Uri: URI,
    @Qualifier("azureCC") restOperations: RestOperations,
) : AbstractRestClient(restOperations) {

    fun hentInstitusjonsoppholdPerson(personIdent: String): String {
        val hentInstOppholdPersonUri =
            UriComponentsBuilder
                .fromUri(inst2Uri)
                .pathSegment("api/v1/person/institusjonsopphold/soek")
                .build().toUri()
        logger.info("Henter institusjonsopphold for person med URI=$hentInstOppholdPersonUri")
        return postForEntity<String, inst2PersonRequest>(hentInstOppholdPersonUri, headers(),
            inst2PersonRequest(personIdent)).also {
            logger.info("Hentet institusjonsopphold for person med URI=$hentInstOppholdPersonUri. Kall ok.")
        }
    }

    fun hentInstitusjonsoppholdPersoner(personIdenter: List<String>): String {
        val hentInstOppholdPersonerUri =
            UriComponentsBuilder
                .fromUri(inst2Uri)
                .pathSegment("api/v1/personer/institusjonsopphold/soek")
                .build().toUri()
        logger.info("Henter institusjonsopphold for liste med personer med URI=$hentInstOppholdPersonerUri")
        return postForEntity<String, inst2PersonerRequest>(hentInstOppholdPersonerUri, headers(),
            inst2PersonerRequest(personIdenter)).also {
            logger.info("Hentet institusjonsopphold for personer med URI=$hentInstOppholdPersonerUri. Kall ok.")
        }
    }

    fun hentInstitusjonsoppholdFeed(sekvensnummer: Long, antallhendelser: Long): String {
        val hentInstOppholdFeedUri =
            UriComponentsBuilder
                .fromUri(inst2Uri)
                .pathSegment("api/v1/hendelse/after-id/" + sekvensnummer)
                .queryParam("antall-hendelser", antallhendelser)
                .build().toUri()
        logger.info("Henter institusjonsopphold feed med URI=$hentInstOppholdFeedUri")
        return getForEntity<String>(hentInstOppholdFeedUri, headers()).also {
            logger.info("Hentet institusjonsopphold feed med URI=$hentInstOppholdFeedUri. Kall ok.")
        }
    }

    private fun headers(): HttpHeaders = HttpHeaders().apply {
        contentType = MediaType.APPLICATION_JSON
        accept = listOf(MediaType.APPLICATION_JSON)
        set("Nav-Call-Id", UUID.randomUUID().toString())
    }

    data class inst2PersonRequest(val personident: String)

    data class inst2PersonerRequest(val personidenter: List<String>)

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}