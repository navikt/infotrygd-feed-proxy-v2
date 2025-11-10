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
class OppgaveClient (
    @Value("\${OPPGAVE_URL}") private val oppgaveUri: URI,
    @Qualifier("azureCC") restOperations: RestOperations,
) : AbstractRestClient(restOperations) {

    fun opprettOppgave(personident: String, orgnr: String, tildeltEnhetsnr: String,
                       opprettetAvEnhetsnr: String, saksreferanse: String, beskrivelse: String, tema: String,
                       behandlingstype: String, oppgavetype: String, aktivDato: String, prioritet: String): String {
        val opprettOppgaveUri =
            UriComponentsBuilder
                .fromUri(oppgaveUri)
                .pathSegment("api/v1/oppgaver")
                .build().toUri()
        logger.info("Oppretter oppgave med URI=$opprettOppgaveUri")

        return postForEntity<String, OpprettOppgaveRequest>(opprettOppgaveUri, headers(),
            OpprettOppgaveRequest(personident, orgnr, tildeltEnhetsnr, opprettetAvEnhetsnr,
                saksreferanse, beskrivelse, tema, behandlingstype, oppgavetype, aktivDato, prioritet)).also {
            logger.info("Opprettet oppgave med URI=$opprettOppgaveUri. Kall ok.")
        }
    }

    fun ferdigstillOppgave(oppgaveId: Long, tekst: String): String {
        val status = "FERDIGSTILT"
        val ferdigstillOppgaveUri =
            UriComponentsBuilder
                .fromUri(oppgaveUri)
                .pathSegment("api/v1/oppgaver/" + oppgaveId)
                .build().toUri()
        logger.info("Ferdigstiller oppgave med URI=$ferdigstillOppgaveUri")
        return patchForEntity<String, FerdigstillOppgaveRequest>(ferdigstillOppgaveUri, headers(),
            FerdigstillOppgaveRequest(status,
                FerdigstillOppgaveKommentar(tekst))).also {
            logger.info("Ferdigstilt oppgave med URI=$ferdigstillOppgaveUri. Kall ok.")
        }
    }

    private fun headers(): HttpHeaders = HttpHeaders().apply {
        contentType = MediaType.APPLICATION_JSON
        accept = listOf(MediaType.APPLICATION_JSON)
        set("X-Correlation-ID", UUID.randomUUID().toString())
    }

    data class OpprettOppgaveRequest(val personident: String, val orgnr: String, val tildeltEnhetsnr: String,
                                     val opprettetAvEnhetsnr: String, val saksreferanse: String,
                                     val beskrivelse: String, val tema: String, val behandlingstype: String,
                                     val oppgavetype: String, val aktivDato: String, val prioritet: String)

    data class FerdigstillOppgaveRequest(val status: String, val kommentar: FerdigstillOppgaveKommentar)

    data class FerdigstillOppgaveKommentar(val tekst: String)

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}