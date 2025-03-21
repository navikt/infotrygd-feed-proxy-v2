package no.nav.infotrygd.feed.proxy.api.fpsak

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import no.nav.infotrygd.feed.proxy.integration.ForeldreOgSvangerskapspengerFeedClient
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/fpsak")
@ProtectedWithClaims(issuer = "sts")
class ForeldrepengerOgSvangerskapspengerFeedProxyController(private val foreldrepengerFeedClient: ForeldreOgSvangerskapspengerFeedClient) {

    @Operation(
        summary = "Hent liste med hendelser.",
        description = "Henter hendelser med sekvensId større enn sistLesteSekvensId.",
    )
    @GetMapping("/foreldrepenger/v1/feed", produces = ["application/json; charset=us-ascii"])
    fun hentFeedForeldrepenger(
        @Parameter(description = "Sist leste sekvensnummer.", required = true, example = "0")
        @RequestParam("sistLesteSekvensId") sekvensnummer: Long,
    ): ResponseEntity<String> {
        return Result.runCatching {
            foreldrepengerFeedClient.hentForeldrepengerFeed(sekvensnummer = sekvensnummer)
        }.fold(
            onSuccess = { feed ->
                logger.info("Hentet FP-feed fra sekvensnummer $sekvensnummer")
                secureLogger.info("Hentet FP-feed $feed fra sekvensnummer $sekvensnummer")
                ResponseEntity.ok(feed)
            },
            onFailure = {
                logger.error("Feil ved henting av FP-feed fra sekvensnummer $sekvensnummer", it)
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
            },
        )
    }

    @Operation(
        summary = "Hent liste med hendelser.",
        description = "Henter hendelser med sekvensId større enn sistLesteSekvensId.",
    )
    @GetMapping("/svangerskapspenger/v1/feed", produces = ["application/json; charset=us-ascii"])
    fun hentFeedSvangerskapspenger(
        @Parameter(description = "Sist leste sekvensnummer.", required = true, example = "0")
        @RequestParam("sistLesteSekvensId")
        sekvensnummer: Long,
    ): ResponseEntity<String> {
        return Result.runCatching {
            foreldrepengerFeedClient.hentSvangerskapspengerFeed(sekvensnummer = sekvensnummer)
        }.fold(
            onSuccess = { feed ->
                logger.info("Hentet SV-feed fra sekvensnummer $sekvensnummer")
                secureLogger.info("Hentet SV-feed $feed fra sekvensnummer $sekvensnummer")
                ResponseEntity.ok(feed)
            },
            onFailure = {
                logger.error("Feil ved henting av SV-feed fra sekvensnummer $sekvensnummer", it)
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
            },
        )
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
        private val secureLogger = LoggerFactory.getLogger("secureLogger")
    }
}
