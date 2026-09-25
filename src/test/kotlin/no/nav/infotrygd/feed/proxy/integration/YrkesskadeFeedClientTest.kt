package no.nav.infotrygd.feed.proxy.integration

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.method
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import org.springframework.web.client.RestTemplate
import java.net.URI

class YrkesskadeFeedClientTest {
    private val restTemplate = RestTemplate()
    private val mockServer = MockRestServiceServer.createServer(restTemplate)

    private val ysFeedClient = YrkesskadeFeedClient(URI.create("http://localhost:8080"), restTemplate)

    @Test
    fun `skal hente yrkesskade feed`() {
        mockServer
            .expect(requestTo("http://localhost:8080/api/v1/feed?sistLesteSekvensId=0"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(feedMelding(), MediaType.APPLICATION_JSON))

        val feed = ysFeedClient.hentYrkesskadeFeed(0)

        assertNotNull(feed)
        mockServer.verify()
    }

    private fun feedMelding(): String =
        """
{
  "elementer": [
    {
      "innhold": {
        "fnrStoenadsmottaker": "12345678910",
        "skadeDato": "2024-03-31",
        "fomDato": null,
        "tomDato": null,
        "tekst": "avslag"
      },
      "metadata": {
        "opprettetDato": "2024-03-31"
      },
      "sekvensId": 1,
      "type": "Ys_avslag"
    },
    {
      "innhold": {
        "fnrStoenadsmottaker": "09510674968",
        "skadeDato": "2024-04-01",
        "fomDato": null,
        "tomDato": null,
        "tekst": ""
      },
      "metadata": {
        "opprettetDato": "2024-05-01"
      },
      "sekvensId": 4,
      "type": "Ys_godkjent"
    },
    {
      "innhold": {
        "fnrStoenadsmottaker": "17458938083",
        "skadeDato": "2024-03-04",
        "fomDato": null,
        "tomDato": null,
        "tekst": ""
      },
      "metadata": {
        "opprettetDato": "2024-05-29"
      },
      "sekvensId": 100,
      "type": "Ys_godkjent"
    }
  ],
  "inneholderFlereElementer": true,
  "tittel": "Yrkesskade Infotrygd feed"
}
        """.trimIndent()
}
