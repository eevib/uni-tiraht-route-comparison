# Käyttöohje

Generoitua .jar-muotoista java-ohjelmaa voi ajaa jokusella eri tavalla. Alle on
avattu pari komentoriviparametria.

Ohjelma suoritetaan antamalla `routecomparison/` -hakemistossa komento
```
java -jar target/RouteComparison-1.0-SNAPSHOT.jar
```

Oletusarvoisesti ohjelma ajetaan kuin sille olisi annettu komentoriviparametrit
`-random -bench`. Tämä tarkoittaa sitä, että ajetaan suorituskykytestit
satunnaistäytetyt ruudukot mukaanlukien.
```
java -jar target/RouteComparison-1.0-SNAPSHOT.jar -random -bench
```

Ohjelman voi komentaa myös lukemaan ruudukoita tiedostoista. Alla oleva komento
asettaa myös suorituskykyvertailun toistomääräksi luvun 1000, eli jokaisen
ongelmaruudukon kohdalla ajetaan reitinhakualgoritmit 1000 kertaa.
```
java -jar target/RouteComparison-1.0-SNAPSHOT.jar thrc/simple.thrc -bench1000
```

Suorituskykyvertailun voi myös jättää suorittamatta jättämällä
`-bench`-parametrin pois. Alla oleva komento ajaa reitinhakualgoritmit
tiedostosta `thrc/simple.thrc` haetulle ruudukolle ja kahdelle
satunnaisruudukolle. Tällöin se myös tulostaa reitit ASCII-muodossa
komentorivi-ikkunaan tiettyjen suoritustietojen kanssa.
```
java -jar target/RouteComparison-1.0-SNAPSHOT.jar thrc/simple.thrc -random -random
```

Mikäli haluaa nähdä hieman väriä reitin varrella, voi komennon putkittaa
ainakin Unixin kaltaisilla järjestelmillä esimerkiksi `grep`-ohjelmalle.
```
java -jar target/RouteComparison-1.0-SNAPSHOT.jar -random | grep --color=always -E '@|!|'
```
