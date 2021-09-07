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

Satunnaisruudukossa JPS vaikuttaisi toimivan usein A\*:a hitaammin. Vaikuttaisi
siltä, että suuremmilla täyttöasteilla JPS on todennäköisemmin hitaampi. Mitä
yksinkertaisempi ruudukko on, sitä nopeammin JPS näyttäisi toimivan. Tyhjässä
ruudukossa se näyttäisi toimivan jopa 15-kertaisella nopeudella A\*:iin nähden.

Mikäli JPS toimii jossain ruudukossa A\*:a nopeammin, se voi olla useita
kertaluokkia nopeampi. Silloinkin, kun JPS häviää A\*:lle, se vaikuttaisi
olevan kohtuullisen pienen vakiokertoimen päässä A\*:sta (usein n. 1,3x).

Alla on erään suorituskykyvertailun tulokset niin kuin pääohjelma ne esitti.

Huom! Koska algoritmien tietorakenteiden alustamisaskel on identtinen, on se
jätetty pois ajanoton laskuista.

```
% java -jar target/RouteComparison-1.0-SNAPSHOT.jar thrc/{foo,bar,zot,plort,simple,randomtest,randomtest2}.thrc -random -bench
Nykyinen hakemisto: /home/meklu/src/uni-tiraht-route-comparison/routecomparison
Suoritetaan suorituskykyvertailua

n = 100, koko = 6x4, tiedosto = `thrc/foo.thrc'
   A*: yht. 12.65565 ms, ka 0.126556 ms
   JPS: yht. 11.949609 ms, ka 0.119496 ms
   JPS oli 1.0590848621072038 kertaa niin nopea kuin A*
n = 100, koko = 6x4, tiedosto = `thrc/bar.thrc'
   A*: yht. 3.325929 ms, ka 0.033259 ms
   JPS: yht. 3.45622 ms, ka 0.034562 ms
   A* oli 1.0391743179123787 kertaa niin nopea kuin JPS
n = 100, koko = 24x12, tiedosto = `thrc/zot.thrc'
   A*: yht. 37.858445 ms, ka 0.378584 ms
   JPS: yht. 38.83899 ms, ka 0.388389 ms
   A* oli 1.0259002978067377 kertaa niin nopea kuin JPS
n = 100, koko = 24x24, tiedosto = `thrc/plort.thrc'
   A*: yht. 64.199129 ms, ka 0.641991 ms
   JPS: yht. 34.822597 ms, ka 0.348225 ms
   JPS oli 1.8436054324150493 kertaa niin nopea kuin A*
n = 100, koko = 17x9, tiedosto = `thrc/simple.thrc'
   A*: yht. 21.524028 ms, ka 0.21524 ms
   JPS: yht. 4.565927 ms, ka 0.045659 ms
   JPS oli 4.714054342086503 kertaa niin nopea kuin A*
n = 100, koko = 100x101, tiedosto = `thrc/randomtest.thrc'
   A*: yht. 574.613601 ms, ka 5.746136 ms
   JPS: yht. 740.878208 ms, ka 7.408782 ms
   A* oli 1.2893502811465822 kertaa niin nopea kuin JPS
n = 100, koko = 32x16, tiedosto = `thrc/randomtest2.thrc'
   A*: yht. 29.854313 ms, ka 0.298543 ms
   JPS: yht. 18.195197 ms, ka 0.181951 ms
   JPS oli 1.640779871743076 kertaa niin nopea kuin A*
n = 100, koko = 512x256, satunnaisruudukon täyttöaste = 0/1
   A*: yht. 19675.286689 ms, ka 196.752866 ms
   JPS: yht. 1293.37998 ms, ka 12.933799 ms
   JPS oli 15.212301870483568 kertaa niin nopea kuin A*
n = 100, koko = 512x256, satunnaisruudukon täyttöaste = 1/32
   A*: yht. 25254.302676 ms, ka 252.543026 ms
   JPS: yht. 33394.070177 ms, ka 333.940701 ms
   A* oli 1.3223121067894499 kertaa niin nopea kuin JPS
n = 100, koko = 512x256, satunnaisruudukon täyttöaste = 1/16
   A*: yht. 22007.697895 ms, ka 220.076978 ms
   JPS: yht. 28927.457956 ms, ka 289.274579 ms
   A* oli 1.314424529726579 kertaa niin nopea kuin JPS
n = 100, koko = 512x256, satunnaisruudukon täyttöaste = 1/8
   A*: yht. 18967.059848 ms, ka 189.670598 ms
   JPS: yht. 23979.248376 ms, ka 239.792483 ms
   A* oli 1.264257537444767 kertaa niin nopea kuin JPS
n = 100, koko = 512x256, satunnaisruudukon täyttöaste = 2/8
   A*: yht. 18307.704925 ms, ka 183.077049 ms
   JPS: yht. 26534.088937 ms, ka 265.340889 ms
   A* oli 1.4493399935000317 kertaa niin nopea kuin JPS
n = 100, koko = 512x256, satunnaisruudukon täyttöaste = 3/8
   A*: yht. 10529.183015 ms, ka 105.29183 ms
   JPS: yht. 13554.079819 ms, ka 135.540798 ms
   A* oli 1.2872869433165608 kertaa niin nopea kuin JPS
```
