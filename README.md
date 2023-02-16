# infotrygd-feed-proxy

Applikasjon som kan brukes mellom gcp<->on-prem for kommunikasjon med infotrygd tjenester. Eks. for infotrygd som kaller 
på familie-baks-infotrygd-feed med sts token, så mottar denne sts, og kaller videre på familie-baks-infotrygd-feed med
azure-token.

## Deployments
Appen deployes til teamfamilie, både i dev og prod.

## Test
For å teste applikasjon i dev må man bruke sts-token. Slik generer du STS token i dev.
1. Åpen STS swagger-ui med denne lenke 
https://security-token-service.nais.preprod.local/swagger-ui/index.html i Chrome SKSS
2. logger på swagger via username "srvinfotrygd-feed" og passord. 
3. Passordet finner du enten i vault 
https://vault.adeo.no/ui/vault/secrets/serviceuser/show/dev/srvinfotrygd-feed 
eller ved å logge på POD.
4. Kall /rest/v1/sts/token tjeneste for å generer et token.
5. Genererte tokenet kan brukes som Bearer token for å logge på swagger-ui 
https://infotrygd-feed-proxy.dev.intern.nav.no/swagger-ui/index.html  