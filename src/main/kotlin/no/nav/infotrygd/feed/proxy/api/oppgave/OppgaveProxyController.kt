package no.nav.infotrygd.feed.proxy.api.oppgave

import io.swagger.v3.oas.annotations.Operation
import no.nav.infotrygd.feed.proxy.integration.OppgaveClient
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/oppgave")
@ProtectedWithClaims(issuer = "sts")
class OppgaveProxyController(
    private val oppgaveClient: OppgaveClient) {

    // Kalles fra Infotrygd program K278U757
    @Operation(
        summary = "Opprett oppgave.",
        description = "Oppretter en oppgave.",
    )
    @PostMapping("/v1/opprett", produces = ["application/json; charset=us-ascii"])
    fun opprettOppgave(
        @RequestBody(required = true) oppgaveBody: OpprettOppgaveBody,
    ): ResponseEntity<String> =
        Result
            .runCatching {
                oppgaveClient.opprettOppgave(oppgaveBody.personident, oppgaveBody.orgnr,
                    oppgaveBody.tildeltEnhetsnr, oppgaveBody.opprettetAvEnhetsnr, oppgaveBody.saksreferanse,
                    oppgaveBody.beskrivelse, oppgaveBody.tema, oppgaveBody.behandlingstema, oppgaveBody.behandlingstype,
                    oppgaveBody.oppgavetype, oppgaveBody.aktivDato, oppgaveBody.prioritet)
            }.fold(
                onSuccess = { oppgave ->
                    logger.info("Opprettet oppgave for bruker identifisert med personident eller orgnr.")
                    secureLogger.info("Opprettet oppgave for person identifisert med personnummer eller organisasjon" +
                            " identifsert med organisasjonsnummer.")
                    ResponseEntity.ok(oppgave)
                },
                onFailure = {
                    logger.error("Feil ved oppretting av oppgave", it)
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
                },
            )

    // Kalles fra Infotrygd program K278U717
    @Operation(
        summary = "Ferdigstill oppgave.",
        description = "Ferdigstiller oppgave.",
    )
    @GetMapping("/v1/ferdigstill/{oppgaveId}", produces = ["application/json; charset=us-ascii"])
    fun ferdigstillOppgave(
        @PathVariable("oppgaveId") oppgaveId: Long,
        @RequestHeader("Beskrivelse") beskrivelse: String,
    ): ResponseEntity<String> =
        Result
            .runCatching {
                logger.info("oppgaveId: $oppgaveId")
                logger.info("Beskrivelse : $beskrivelse")
                oppgaveClient.ferdigstillOppgave(oppgaveId, beskrivelse)
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

    data class OpprettOppgaveBody(val personident: String, val orgnr: String, val tildeltEnhetsnr: String,
                                  val opprettetAvEnhetsnr: String, val saksreferanse: String, val beskrivelse: String,
                                  val tema: String, val behandlingstema: String, val behandlingstype: String,
                                  val oppgavetype: String, val aktivDato: String, val prioritet: String)

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
        private val secureLogger = LoggerFactory.getLogger("secureLogger")
    }
}
