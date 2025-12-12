package no.nav.infotrygd.feed.proxy.integration

import no.nav.infotrygd.feed.proxy.integration.http.klient.AbstractRestClient
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.client.RestOperations

@Service
class TestClient (
    @Qualifier("azureCC") restOperations: RestOperations,
) : AbstractRestClient(restOperations) {

    fun testRespons(test: String): String {

        logger.info("Mottok test-request: $test")

        return "{\"test\": \"$test\", \"respons\": \"Det virker!\"}"
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}