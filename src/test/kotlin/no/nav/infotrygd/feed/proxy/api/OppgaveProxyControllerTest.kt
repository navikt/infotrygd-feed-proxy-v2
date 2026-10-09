package no.nav.infotrygd.feed.proxy.api

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import no.nav.infotrygd.feed.proxy.integration.OppgaveClient
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withStatus
import org.springframework.web.client.RestTemplate
import java.net.URI

class OppgaveProxyControllerTest {
    private val restTemplate = RestTemplate()
    private val server = MockRestServiceServer.createServer(restTemplate)
    private val controller = OppgaveProxyController(OppgaveClient(URI.create("http://localhost"), restTemplate))

    @ParameterizedTest
    @ValueSource(strings = ["opprett", "ferdigstill", "underkjent"])
    fun `duplikat eller annen konflikt logges som warning uten sensitivt innhold`(operasjon: String) {
        verifiserFeil(
            operasjon, HttpStatus.CONFLICT,
            """{"uuid":"korrelasjon","feilmelding":"sensitiv fritekst","oppgaveId":123}""",
            Level.WARN,
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["opprett", "ferdigstill", "underkjent"])
    fun `konflikt pakket i RessursException logges som warning`(operasjon: String) {
        verifiserFeil(
            operasjon, HttpStatus.CONFLICT,
            """{"data":null,"status":"FUNKSJONELL_FEIL","melding":"sensitiv fritekst","stacktrace":null}""",
            Level.WARN,
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["opprett", "ferdigstill", "underkjent"])
    fun `andre HTTP feil logges som error uten sensitivt innhold`(operasjon: String) {
        verifiserFeil(operasjon, HttpStatus.INTERNAL_SERVER_ERROR, "sensitiv fritekst", Level.ERROR)
    }

    @ParameterizedTest
    @ValueSource(strings = ["opprett", "ferdigstill", "underkjent"])
    fun `valideringsfeil logges uten responsbody`(operasjon: String) {
        verifiserFeil(operasjon, HttpStatus.BAD_REQUEST, "sensitiv fritekst", Level.ERROR)
    }

    @ParameterizedTest
    @ValueSource(strings = ["opprett", "ferdigstill", "underkjent"])
    fun `vellykket oppgavekall returnerer fortsatt HTTP 200 med body`(operasjon: String) {
        forventSvar(operasjon, HttpStatus.CREATED, """{"id":123}""")
        val svar = kall(operasjon)
        assertEquals(HttpStatus.OK, svar.statusCode)
        assertEquals("""{"id":123}""", svar.body)
        server.verify()
    }

    private fun verifiserFeil(operasjon: String, status: HttpStatus, body: String, nivå: Level) {
        forventSvar(operasjon, status, body)
        val logger = LoggerFactory.getLogger(OppgaveProxyController::class.java) as Logger
        val appender = ListAppender<ILoggingEvent>()
        appender.start()
        logger.addAppender(appender)
        try {
            val svar = kall(operasjon)
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, svar.statusCode)
            assertNull(svar.body)
            val hendelse = appender.list.single()
            assertEquals(nivå, hendelse.level)
            val handling = if (operasjon == "opprett") "oppretting" else "ferdigstilling"
            val melding = if (status == HttpStatus.CONFLICT) "Konflikt ved" else "Oppgave avviste"
            assertEquals("$melding $handling av oppgave. status=${status.value()}", hendelse.formattedMessage)
            assertNull(hendelse.throwableProxy)
            server.verify()
        } finally {
            logger.detachAppender(appender)
            appender.stop()
        }
    }

    private fun forventSvar(operasjon: String, status: HttpStatus, body: String) {
        val sti = if (operasjon == "opprett") "" else "/1"
        server.expect(requestTo("http://localhost/api/v1/oppgaver$sti"))
            .andRespond(withStatus(status).contentType(MediaType.APPLICATION_JSON).body(body))
    }

    private fun kall(operasjon: String): ResponseEntity<String> = when (operasjon) {
        "opprett" -> controller.opprettOppgave(
            OppgaveProxyController.OpprettOppgaveBody(
                "", "", "1234", "1234", "sak", "beskrivelse", "SYK",
                "", "", "BEH_SAK", "2026-10-09", "NORM",
            ),
        )
        "ferdigstill" -> controller.ferdigstillOppgave(
            OppgaveProxyController.FerdigstillOppgaveBody(1, "2026-10-09", "10:00", "bruker", "1234", "aksjon", "resultat"),
        )
        "underkjent" -> controller.ferdigstillOppgaveUk(
            OppgaveProxyController.FerdigstillOppgaveUkBody(1, "a", "b", "c"),
        )
        else -> error("Ukjent testoperasjon")
    }
}
