# infotrygd-feed-proxy

Applikasjon som kan brukes mellom gcp<->on-prem for kommunikasjon med infotrygd tjenester. Eks. for infotrygd som kaller 
på familie-baks-infotrygd-feed med sts token, så mottar denne sts, og kaller videre på familie-baks-infotrygd-feed med
azure-token.

Deployments
Appen deployes til teamfamilie, både i dev og prod.