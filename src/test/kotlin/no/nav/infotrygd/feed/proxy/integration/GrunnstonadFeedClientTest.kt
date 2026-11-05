package no.nav.infotrygd.feed.proxy.integration

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.method
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import org.springframework.web.client.RestTemplate
import java.net.URI

class GrunnstonadFeedClientTest {
    private val restTemplate = RestTemplate()
    private val mockServer = MockRestServiceServer.createServer(restTemplate)

    private val grunnstonadFeedClient = GrunnstonadFeedClient(URI.create("http://localhost:8080"), restTemplate)

    
    @Test
    fun `skal hente grunnstønad feed`() {
        mockServer
            .expect(requestTo("http://localhost:8080/api/grunnstonad/v1/feed?sistLesteSekvensId=0"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(feedMelding(), MediaType.APPLICATION_JSON))

        val feed = grunnstonadFeedClient.hentGrunnstonadFeed(0)

        assertEquals(feedMelding(), feed)
        mockServer.verify()
    }

    private fun feedMelding(): String =
        """
        {
          "elementer": [
            {
              "sekvensId": 1,
              "type": "GS_Vedtak",
              "metadata": {
                "opprettetDato": "2026-01-01T12:00:00"
              },
              "innhold": {
                "personIdent": "12345678910",
                "datoStartNyGrunnstoenad": "2026-02-01"
              }
            },
            {
              "sekvensId": 2,
              "type": "GS_StartBeh",
              "metadata": {
                "opprettetDato": "2026-01-02T12:00:00"
              },
              "innhold": {
                "personIdent": "12345678910"
              }
            }
          ],
          "inneholderFlereElementer": false,
          "tittel": "Grunnstoenad Infotrygd feed"
        }
        """.trimIndent()
}
