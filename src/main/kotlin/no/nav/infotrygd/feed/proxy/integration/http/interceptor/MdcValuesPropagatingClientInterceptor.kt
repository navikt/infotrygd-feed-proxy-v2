package no.nav.infotrygd.feed.proxy.integration.http.interceptor

import no.nav.infotrygd.feed.proxy.integration.http.klient.IdUtils
import no.nav.infotrygd.feed.proxy.integration.http.klient.MDCConstants
import no.nav.infotrygd.feed.proxy.integration.http.util.NavHttpHeaders
import org.slf4j.MDC
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Component

@Component
class MdcValuesPropagatingClientInterceptor : ClientHttpRequestInterceptor {
    override fun intercept(
        request: HttpRequest,
        body: ByteArray,
        execution: ClientHttpRequestExecution,
    ): ClientHttpResponse {
        val callId = MDC.get(MDCConstants.MDC_CALL_ID) ?: IdUtils.generateId()
        val requestId = MDC.get(MDCConstants.MDC_REQUEST_ID) ?: callId
        request.headers.add(NavHttpHeaders.NAV_CALL_ID.asString(), callId)
        request.headers.add(NavHttpHeaders.NGNINX_REQUEST_ID.asString(), requestId)

        return execution.execute(request, body)
    }
}
