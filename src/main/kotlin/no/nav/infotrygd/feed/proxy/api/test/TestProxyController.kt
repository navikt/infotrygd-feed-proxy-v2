package no.nav.infotrygd.feed.proxy.api.test

import io.swagger.v3.oas.annotations.Operation
import no.nav.infotrygd.feed.proxy.integration.TestClient
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
@ProtectedWithClaims(issuer = "sts")
class TestProxyController(
    private val testClient: TestClient
) {

    // Kalles fra Infotrygd
    @Operation(
        summary = "Test.",
        description = "Tester at kommunikasjonen fungerer.",
    )
    @PostMapping("/v1/test", produces = ["application/json; charset=us-ascii"])
    fun testRespons(
        @RequestBody testBody: TestBody
    ): ResponseEntity<String> =
        Result
            .runCatching {
                testClient.testRespons(testBody.test)
            }.fold(
                onSuccess = { testen ->
                    logger.info("Testen var vellykket")
                    ResponseEntity.ok(testen)
                },
                onFailure = {
                    logger.error("Feil ved test", it)
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
                },
            )

    data class TestBody(val test: String)

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}
