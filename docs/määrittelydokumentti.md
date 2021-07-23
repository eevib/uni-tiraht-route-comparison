# Määrittelydokumentti

## Meta

Opinto-ohjelma: TKT kandiohjelma

Projektissa käytetty luonnollinen kieli: suomi

Ohjelmointikieli: Java

## Mitä?

Työn aiheena on vertailla erilaisia ruudukossa toimivia reitinhakualgoritmeja.
Käytettävä ohjelmointikieli on Java. Ruudukot toteutettaneen bittikarttoina,
joita ohjelma syö ja koluaa läpi. Visualisointiin käytettäneen erillistä
tilaavievämpää matriisia, koska siinä vaiheessa on tarvetta useammalle kuin
kahdelle mahdolliselle tilalle solua kohden. Koko höskä maalataan sitten
hienolle JavaFX-kankaalle tarkastelijan iloksi.

### Algoritmit, tavoiteltavat aika- ja tilavaativuudet

Vaativuusluokissa `b` viittaa keskimääräiseen solmusta haarautuvien polkujen
määrään ja `d` ratkaisupolun eli lyhyimmän polun syvyyteen/pituuteen. Lisäksi
`n` viittaa solmujen määrään ja `m` kaarien määrään. Tässä oletetaan
heuristiikan olevan hyvä.

- JPS
	- pahin aikavaativuus `O(m) = O(b^d)`
	- pahin tilavaativuus `O(n) = O(b^d)`
	- (hyvässä tapauksessa huomattavasti vähemmän vaativa kuin naiivi A\*)
- IDA\*
	- pahin aikavaativuus `O(b^d)`
	- pahin tilavaativuus `O(d)`

Ruudukkomme tapauksessa jokaisella ei reunalla sijaitsevalla solulla on 8
naapuria ja reunat ja kulmat huomioiden tämä tippuu hieman alle tämän pyöreän
tasaluvun. Voidaan sanoa, että algoritmiemme pahin mahdollinen aikavaativuus on
eksponentiaalisesti kasvava polun pituuden suhteen. Tämä on kuitenkin JPS:n
kohdalla rajautunut ylärajan puolesta kaarien kokonaismäärään, koska se toimii
pahimmassakin tapauksessa yhtä nopeasti kuin A\*, joka hyvällä heuristiikalla
on taattu käsittelemään jokainen solmu enimmillään yhden kerran.

IDA\*:n pahin mahdollinen tapaus on tosin hitaampi, koska se voi joutua
käsittelemään samoja solmuja useampaan kertaan. Se on kuitenkin rajoitetun
muistin tapauksissa tilavaativuudeltaan suopeampi kuin JPS, mikä voi auttaa
tietyissä tapauksissa.

### Tietorakenteet

- implisiittinen suuntaamaton verkko (ruudukko)
- pino (IDA\*)
- puu (JPS)
- keko (JPS)

## Miksi?

Algoritmien valinta perustui kurssimateriaalin aihelistan mainintoihin.

## Lähteet

- ["A\* search algorithm", Wikipedia](https://en.wikipedia.org/wiki/A\*\_search\_algorithm)
- ["Jump Point Search", Daniel Haraborin blogi](https://harablog.wordpress.com/2011/09/07/jump-point-search/)
- ["Iterative deepening A\*", Wikipedia](https://en.wikipedia.org/wiki/Iterative\_deepening_A\*)
