package no.nav.infotrygd.feed.proxy.api.kontantstotte

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import no.nav.infotrygd.feed.proxy.integration.BarnetrygdKontantstotteFeedClient
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/kontantstotte")
@ProtectedWithClaims(issuer = "sts")
class KontantstotteFeedProxyController(
    private val barnetrygdKontantstotteFeedClient: BarnetrygdKontantstotteFeedClient,
) {
    @Operation(
        summary = "Hent liste med hendelser.",
        description = "Henter hendelser med sekvensId større enn sistLesteSekvensId.",
    )
    @GetMapping("/v1/feed", produces = ["application/json; charset=us-ascii"])
    fun hentFeed(
        @Parameter(description = "Sist leste sekvensnummer.", required = true, example = "0")
        @RequestParam("sistLesteSekvensId")
        sekvensnummer: Long,
    ): ResponseEntity<String> =
        Result
            .runCatching {
                barnetrygdKontantstotteFeedClient.hentKontantstotteFeed(sekvensnummer = sekvensnummer)
            }.fold(
                onSuccess = { feed ->
                    logger.info("Hentet feeds fra sekvensnummer $sekvensnummer")
                    secureLogger.info("Hentet feeds $feed fra sekvensnummer $sekvensnummer")
                    ResponseEntity.ok(feed)
                },
                onFailure = {
                    logger.error("Feil ved henting av KS-feed fra sekvensnummer $sekvensnummer", it)
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
                },
            )

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
        private val secureLogger = LoggerFactory.getLogger("secureLogger")
    }
}
