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

    fun hentInstitusjonsoppholdPerson(personIdent: String): String {
        val hentInstOppholdPersonUri =
            UriComponentsBuilder
                .fromUri(inst2Uri)
                .pathSegment("api/v1/person/institusjonsopphold")
                .build().toUri()
        logger.info("Henter institusjonsopphold for person med URI=$hentInstOppholdPersonUri")
        return getForEntity<String>(hentInstOppholdPersonUri, headersPerson(personIdent)).also {
            logger.info("Hentet institusjonsopphold for person med URI=$hentInstOppholdPersonUri. Kall ok.")
        }
    }

    fun hentInstitusjonsoppholdPersoner(personIdenter: String): String {
        val hentInstOppholdPersonerUri =
            UriComponentsBuilder
                .fromUri(inst2Uri)
                .pathSegment("api/v1/personer/institusjonsopphold")
                .build().toUri()
        logger.info("Henter institusjonsopphold for liste med personer med URI=$hentInstOppholdPersonerUri")
        return getForEntity<String>(hentInstOppholdPersonerUri, headersPersoner(personIdenter)).also {
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
        return getForEntity<String>(hentInstOppholdFeedUri, headersFeed()).also {
            logger.info("Hentet institusjonsopphold feed med URI=$hentInstOppholdFeedUri. Kall ok.")
        }
    }

    private fun headersPerson(
        personIdent: String): HttpHeaders = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            accept = listOf(MediaType.APPLICATION_JSON)
            set("Nav-Call-Id", "institusjonsopphold-person")
            set("Nav-Consumer-Id", "infotrygd-feed-proxy-v2")
            set("Nav-Personident", personIdent)
        }

    private fun headersPersoner(
        personIdenter: String): HttpHeaders = HttpHeaders().apply {
        contentType = MediaType.APPLICATION_JSON
        accept = listOf(MediaType.APPLICATION_JSON)
        set("Nav-Call-Id", "institusjonsopphold-personer")
        set("Nav-Consumer-Id", "infotrygd-feed-proxy-v2")
        set("Nav-Personident", personIdenter)
    }

    private fun headersFeed(): HttpHeaders = HttpHeaders().apply {
        contentType = MediaType.APPLICATION_JSON
        accept = listOf(MediaType.APPLICATION_JSON)
        set("Nav-Call-Id", "institusjonsopphold-feed")
        set("Nav-Consumer-Id", "infotrygd-feed-proxy-v2")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}