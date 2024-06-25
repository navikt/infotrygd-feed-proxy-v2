package no.nav.infotrygd.feed.proxy.integration

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.http.*
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestOperations
import org.springframework.web.client.exchange
import java.net.URI

class YrkesskadeFeedClientTest {

    private val restOperationsMock: RestOperations = mockk()

    private val ysFeedClient = YrkesskadeFeedClient(URI.create("http://localhost:8080"), restOperationsMock)


    @Test
    fun `skal hente yrkesskade feed`() {
        val headers = LinkedMultiValueMap<String, String>()

        every {
            restOperationsMock.exchange<String>(
                any<URI>(),
                eq(HttpMethod.GET),
                any<HttpEntity<String>>()
            )
        } returns ResponseEntity(feedMelding(), headers, HttpStatus.OK)

        val feed = ysFeedClient.hentYrkesskadeFeed(0)

        assertNotNull(feed)
    }

    private fun feedMelding() : String = """
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