# Testausdokumentti

## Yksikkötestauksesta

Ohjelmalla on ongelma-alueellaan kattavat yksikkötestit, jotka mittaavat
ohjelman toiminnan oikeellisuutta. Apuluokka `Reitintutkija` on tärkeässä
asemassa varsinaisten reitinhakijatoteutusten ratkaisujen mielekkään
oikeellisuustestauksen kanssa.

Yksikkötestit voi suorittaa ajamalla repositorion juurihakemistosta löytyvässä
`routecomparison/` -hakemistossa komennon `mvn test`.

## Koodikattavuusraportti

Tämänhetkinen koodikattavuusraportti löytyy osoitteesta
  https://meklu.org/code/uni/tiraht/jacoco-vko6/

Koodikattavuusraportin voi generoida ajamalla repositorion juurihakemistosta
löytyvässä `routecomparison/` -hakemistossa komennon `mvn jacoco:report`.

## Suorituskyvyn vertailu

Pääohjelma suorittaa toteutetut algoritmit ongelmaruudukossa ja tulostaa
saatujen reittien ohessa algoritmikohtaisen suoritusajan.

Ohjelman voi kääntää ajamalla repositorion juurihakemistosta löytyvässä
`routecomparison/` hakemistossa komennon `mvn install`.

Ohjelman voi suorittaa kääntämisen jälkeen samaisessa hakemistossa komennolla
`java -jar target/RouteComparison-1.0-SNAPSHOT.jar`

## Suorituskykyvertailun tulokset

TODO
