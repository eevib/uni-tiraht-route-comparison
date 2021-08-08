# Pari pikaohjetta käyttöön

## Koko höskä tiivistettynä

`mvn clean install javadoc:javadoc checkstyle:checkstyle`

## Kääntäminen

`mvn install`

Tämä ajaa myös esimerkiksi testit, paketoinnin ja koodikattavuuden

## Ajaminen

Kääntäminen tuottaa ohjelmasta suoritettavan `.jar` -paketin tästä hakemistosta
löytyvän `target/` -hakemiston alle. Sen voi suorittaa ajamalla tässä
hakemistossa seuraavan komennon:

`java -jar target/RouteComparison-1.0-SNAPSHOT.jar`

## Testien ajaminen

`mvn test`

## Koodikattavuusraportti

`mvn jacoco:report`

Tämä tuottaa koodikattavuusraportin polkuun `target/site/jacoco/index.html`.

## Dokumentaation luonti

`mvn javadoc:javadoc`

Tämä tuottaa dokumentaatiota polkuun `target/site/apidocs/index.html`.

Huom! Tämä saattaa vaatia `mvn clean` -operaation allensa.

## Tyylintarkastus

`mvn checkstyle:checkstyle`

Tämä tuottaa raportin polkuun `target/site/checkstyle.html`.
