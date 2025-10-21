package no.nav.infotrygd.feed.proxy.api.gsak

import io.swagger.v3.oas.annotations.Operation
import no.nav.infotrygd.feed.proxy.integration.GsakFeedClient
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/gsak")
@ProtectedWithClaims(issuer = "sts")
class GsakFeedProxyController(
    private val gsakFeedClient: GsakFeedClient) {

    // Kalles fra Infotrygd program K278U757
    @Operation(
        summary = "Opprett oppgave med brukertype person.",
        description = "Oppretter en oppgave i GSAK med brukertype person.",
    )
    @GetMapping("v1/opprettoppgave/person", produces = ["application/json; charset=us-ascii"])
    fun gsakOpprettOppgavePerson(
        @RequestBody(required = true) gsakBody: GsakOpprettOppgaveBody,
    ): ResponseEntity<String> =
        Result
            .runCatching {
                gsakFeedClient.opprettOppgave(gsakBody.brukerident, gsakBody.tildeltEnhetsnr,
                    gsakBody.opprettetAvEnhetsnr, gsakBody.beskrivelse, gsakBody.oppgavetype)
            }.fold(
                onSuccess = { person ->
                    logger.info("Opprettet oppgave for person identifisert med personident.")
                    secureLogger.info("Opprettet oppgave for person identifisert med personnummer.")
                    ResponseEntity.ok(person)
                },
                onFailure = {
                    logger.error("Feil ved oppretting av oppgave for person", it)
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
                },
            )

    // Kalles fra Infotrygd program K278U757
    @Operation(
        summary = "Opprett oppgave med brukertype organisasjon.",
        description = "Oppretter en oppgave i GSAK med brukertype organisasjon.",
    )
    @GetMapping("v1/opprettoppgave/organisasjon", produces = ["application/json; charset=us-ascii"])
    fun gsakOpprettOppgaveOrg(
        @RequestBody(required = true) gsakBody: GsakOpprettOppgaveBody,
    ): ResponseEntity<String> =
        Result
            .runCatching {
                gsakFeedClient.opprettOppgave(gsakBody.brukerident, gsakBody.tildeltEnhetsnr,
                    gsakBody.opprettetAvEnhetsnr, gsakBody.beskrivelse, gsakBody.oppgavetype)
            }.fold(
                onSuccess = { org ->
                    logger.info("Opprettet oppgave for organisasjon identifisert med organisasjonsnummer.")
                    secureLogger.info("Opprettet oppgave for organisasjon identifisert med organisasjonsnummer.")
                    ResponseEntity.ok(org)
                },
                onFailure = {
                    logger.error("Feil ved oppretting av oppgave for organisasjon", it)
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
                },
            )

    // Kalles fra Infotrygd program K278U717
    @Operation(
        summary = "Ferdigstill oppgave.",
        description = "Ferdigstiller oppgave i GSAK.",
    )
    @PostMapping("v1/ferdigstilloppgave", produces = ["application/json; charset=us-ascii"])
    fun gsakFerdigstillOppgave(
        @RequestBody(required = true) gsakBody: GsakFerdigstillOppgaveBody,
    ): ResponseEntity<String> =
        Result
            .runCatching {
                gsakFeedClient.ferdigstillOppgave(gsakBody.oppgaveid, gsakBody.tekst)
            }.fold(
                onSuccess = { oppgave ->
                    logger.info("Ferdigstiller oppgave identifisert med oppgaveid.")
                    secureLogger.info("Ferdigstiller oppgave identifisert med oppgaveid.")
                    ResponseEntity.ok(oppgave)
                },
                onFailure = {
                    logger.error("Feil ved ferdigstilling av oppgave", it)
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
                },
            )

    data class GsakOpprettOppgaveBody(val brukerident: String, val tildeltEnhetsnr: String,
                                      val opprettetAvEnhetsnr: String, val beskrivelse: String,
                                      val oppgavetype: String)

    data class GsakFerdigstillOppgaveBody(val oppgaveid: Long, val tekst: String)

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
        private val secureLogger = LoggerFactory.getLogger("secureLogger")
    }
}
