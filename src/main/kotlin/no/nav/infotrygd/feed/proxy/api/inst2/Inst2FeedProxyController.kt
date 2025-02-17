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
@RequestMapping("/after-id")
@ProtectedWithClaims(issuer = "sts")
class Inst2FeedProxyController(
    private val inst2FeedClient: Inst2FeedClient) {

    @Operation(
        summary = "Hent liste med hendelser.",
        description = "Henter hendelser med sekvensId større enn sistLesteSekvensId.",
    )
    @GetMapping("/v1/hendelse", produces = ["application/json; charset=us-ascii"])
    fun hentFeed(
        @Parameter(description = "Sist leste sekvensnummer.", required = true, example = "0")
        @RequestParam("antall-hendelser") sekvensnummer: Long,
        @RequestHeader("Nav-Call-Id") callId: String,
        @RequestHeader("Nav-Consumer-Id") consumerId: String,
    ): ResponseEntity<String> =
        Result
            .runCatching {
                inst2FeedClient.hentInstitusjonsoppholdFeed(callId, consumerId, sekvensnummer = sekvensnummer)
            }.fold(
                onSuccess = { feed ->
                    logger.info("Hentet feeds fra sekvensnummer $sekvensnummer")
                    secureLogger.info("Hentet feeds $feed fra sekvensnummer $sekvensnummer")
                    ResponseEntity.ok(feed)
                },
                onFailure = {
                    logger.error("Feil ved henting av Inst2-feed fra sekvensnummer $sekvensnummer", it)
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
                },
            )

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
        private val secureLogger = LoggerFactory.getLogger("secureLogger")
    }
}
