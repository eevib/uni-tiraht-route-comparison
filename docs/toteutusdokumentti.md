# Toteutusdokumentti

## Ohjelman yleisrakenne

Ohjelman ongelma-alueen ytimessä on `Ruudukko`-luokka, joka esittää ohjelman
läpikoluamaa tietorakennetta. Ratkaisualgoritmien luokat (`JPS`, `AStar`)
toteuttavat `Reitinhakija`-rajapinnan kuvaamat toiminnallisuudet.
Reitinhakijoiden suoritusdetaljien vertailua varten on kehitetty luokka
`Diagnostiikka`, joka kerää näistä ajonaikaista dataa kuten suoritusaikaa ja
syklimäärää. Reitinhakijoiden tuottamien reittien tutkimista varten on
kehitetty luokka `Reitintutkija`. Tämä on hyödyllinen reitin oikeellisuuden
tarkistamiseen ja on kykenevä myös reittien tekstimuotoiseen visualisoitiin.

## Toteutetut algoritmit

Toteutetut algoritmit ovat Jump Point Search (JPS) ja A\*.

## Puutteita ja parannusehdotuksia

- Käyttöliittymä ei ole vielä interaktiivinen
- Algoritmit eivät ole vielä vaiheisiin purettuja, joten vaiheittainen
  visualisointi ei ole vielä mahdollista
- Tällä hetkellä ohjelmalla ei ole ruudukkotiedostojen lukumahdollisuutta
