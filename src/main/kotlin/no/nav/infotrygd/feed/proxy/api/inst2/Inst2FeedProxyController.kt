package no.nav.infotrygd.feed.proxy.api.inst2

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import no.nav.infotrygd.feed.proxy.integration.Inst2FeedClient
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/inst2")
@ProtectedWithClaims(issuer = "sts")
class Inst2FeedProxyController(
    private val inst2FeedClient: Inst2FeedClient) {

    // Kalles fra Infotrygd program K278CPIN
    @Operation(
        summary = "Hent institusjonsopphold for én person.",
        description = "Henter institusjonsopphold for person identifisert med personnummer.",
    )
    @GetMapping("v1/person", produces = ["application/json; charset=us-ascii"])
    fun hentInstPerson(
        @RequestHeader("Nav-Personident") personIdent: String,
    ): ResponseEntity<String> =
        Result
            .runCatching {
                inst2FeedClient.hentInstitusjonsoppholdPerson(personIdent)
            }.fold(
                onSuccess = { person ->
                    logger.info("Hentet institusjonsopphold for person identifisert med personident.")
                    secureLogger.info("Hentet institusjonsopphold for person identifisert med personnummer.")
                    ResponseEntity.ok(person)
                },
                onFailure = {
                    logger.error("Feil ved henting av institusjonsopphold for person", it)
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
                },
        )

    // Kalles fra Infotrygd program K278CPI2
    @Operation(
        summary = "Hent institusjonsopphold for flere personer.",
        description = "Henter institusjonsopphold for flere personer identifisert med liste med flere personident.",
    )
    @GetMapping("v1/personer", produces = ["application/json; charset=us-ascii"])
    fun hentInstPersoner(
        @RequestHeader("Nav-Personident") personIdent: List<String>,
    ): ResponseEntity<String> =
        Result
            .runCatching {
                inst2FeedClient.hentInstitusjonsoppholdPersoner(personIdent)
            }.fold(
                onSuccess = { person ->
                    logger.info("Hentet institusjonsopphold for person identifisert med personident.")
                    secureLogger.info("Hentet institusjonsopphold for person identifisert med personnummer.")
                    ResponseEntity.ok(person)
                },
                onFailure = {
                    logger.error("Feil ved henting av institusjonsopphold for person", it)
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
                },
    )

    // Kalles fra Infotrygd program K278CPI3
    @Operation(
        summary = "Hent liste med hendelser.",
        description = "Henter hendelser med sekvensId større enn sistLesteSekvensId.",
    )
    @GetMapping("/v1/feed", produces = ["application/json; charset=us-ascii"])
    fun hentFeed(
        @Parameter(description = "Sist leste sekvensnummer.", required = true, example = "1254")
        @RequestParam("sistLesteSekvensId") sekvensnummer: Long,
        @Parameter(description = "Max antall hendelser returnert i ett kall.", required = true, example = "250")
        @RequestParam("antall-hendelser") antallhendelser: Long,
    ): ResponseEntity<String> =
        Result
            .runCatching {
                inst2FeedClient.hentInstitusjonsoppholdFeed(sekvensnummer = sekvensnummer, antallhendelser)
            }.fold(
                onSuccess = { feed ->
                    logger.info("Hentet INST2-feed fra sekvensnummer $sekvensnummer")
                    secureLogger.info("Hentet INST2-feed $feed fra sekvensnummer $sekvensnummer")
                    ResponseEntity.ok(feed)
                },
                onFailure = {
                    logger.error("Feil ved henting av INST2-feed fra sekvensnummer $sekvensnummer", it)
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
                },
            )

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
        private val secureLogger = LoggerFactory.getLogger("secureLogger")
    }
}
