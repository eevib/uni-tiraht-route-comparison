
package org.meklu.routecomparison.domain;

import java.util.PriorityQueue;

/** Toteuttaa Jump Point Search -reitinhakualgoritmin.
 *
 * <p>Tämän hakualgoritmin taustalla oleva A*-implementaatio on mukailtu
 * pseudokoodista, joka on haettu osoitteesta
 *    https://en.wikipedia.org/wiki/A*_search_algorithm
 *
 * <p>Tämän hakualgoritmin varsinainen mielenkiintoinen osuus (oksinta ja hypyt,
 * engl. kirjallisuudessa "pruning" ja "jumping") on mukailtu artikkelista
 *    "Online Graph Pruning for Pathfinding on Grid Maps",
 *       Harabor, Daniel &amp; Grastien, Alban (2011), 25th National Conference
 *       on Artificial Intelligence. AAAI.
 */
public class JPS implements Reitinhakija {
    private Ruudukko ruudukko;
    private Heuristiikka heuristiikka;
    private Diagnostiikka diagnostiikka;
    private final Koordinaatti[] suunnat;

    public JPS(Ruudukko ruudukko, Heuristiikka heuristiikka) {
        this.ruudukko = ruudukko;
        this.heuristiikka = heuristiikka;

        Koordinaatti[] naapurit = new Koordinaatti[8];
        for (int i = 0, x = -1; x <= 1 && i < naapurit.length; ++x) {
            for (int y = -1; y <= 1 && i < naapurit.length; ++y) {
                if (x == 0 && y == 0) {
                    continue;
                }
                naapurit[i] = new Koordinaatti(x, y);
                ++i;
            }
        }
        this.suunnat = naapurit;
    }

    private Koordinaatti[] kokoaReitti(Koordinaatti nykyinen) {
        Koordinaatti solmu = nykyinen;
        int reitinPituus = 0;
        while (solmu != null) {
            reitinPituus += 1;
            solmu = this.tulosuunnat[solmu.getY()][solmu.getX()];
        }
        if (0 == reitinPituus) {
            return null;
        }
        Koordinaatti[] reittiTaulukko = new Koordinaatti[reitinPituus];
        solmu = nykyinen;
        for (int i = 0; i < reittiTaulukko.length && solmu != null; ++i) {
            reittiTaulukko[reitinPituus - 1 - i] = solmu;
            solmu = this.tulosuunnat[solmu.getY()][solmu.getX()];
        }
        return reittiTaulukko;
    }

    /** Palauttaa solmun naapurit absoluuttisten koordinaattien listana
     *
     * @param solmu Solmu, jonka naapurit halutaan
     * @return Taulukko naapureita
     */
    private Koordinaatti[] naapurit(Koordinaatti solmu) {
        Koordinaatti[] abs = new Koordinaatti[this.suunnat.length];
        for (int i = 0; i < this.suunnat.length; ++i) {
            abs[i] = new Koordinaatti(
                solmu.getX() + this.suunnat[i].getX(),
                solmu.getY() + this.suunnat[i].getY()
            );
        }
        return abs;
    }

    /** Suorittaa oksintaa
     *
     * @param  solmu    Solmu, jonka naapureita oksitaan
     * @param  vanhempi Solmu, josta saavuttiin tähän solmuun
     * @return Palauttaa taulukon sellaisia naapureita, joita on järkevä
     *         käsitellä. Vähemmän järkevät naapurit on vain nullattu pois.
     */
    public Koordinaatti[] oksi(Koordinaatti solmu, Koordinaatti vanhempi) {
        Koordinaatti[] oksitut = naapurit(solmu);
        // mitään ei oksita, jos vanhempaa ei ole
        if (null == vanhempi) {
            return oksitut;
        }
        int sx = solmu.getX();
        int sy = solmu.getY();
        int vx = vanhempi.getX();
        int vy = vanhempi.getY();
        int dx = sx - vx;
        int dy = sy - vy;
        do {
            // normalisoidaan erotus, jotta saadaan kiva suunta
            // TODO: onkohan tämä oikein tarpeellista ":D"
            // olisi varmaankin parempi ottaa sisään suunta ja laskea vanhempi
            // sen perusteella
            int dmax = Math.max(Math.abs(dx), Math.abs(dy));
            if (dmax == 0) {
                break;
            }
            dx /= dmax;
            dy /= dmax;
            vx = sx - dx;
            vy = sx - dy;
        } while (false);
        Koordinaatti naapuri;
        if (dx == 0 || dy == 0) {
            // suora siirto
            //len( [p(x), … , n] \ x ) ≤ len( [p(x), x, n] )
            for (int i = 0; i < oksitut.length; ++i) {
                naapuri = oksitut[i];
                if (naapuri.equals(vanhempi)) {
                    oksitut[i] = null;
                    continue;
                }
                if (ruudukko.ruutuEstynyt(naapuri.getX(), naapuri.getY())) {
                    oksitut[i] = null;
                    continue;
                }
                if (dy == 0) { // siirto sivulle
                    if (sx + dx != naapuri.getX()) {
                        oksitut[i] = null;
                        continue;
                    } else if (sy != naapuri.getY()) {
                        if (!ruudukko.ruutuEstynyt(sx, naapuri.getY())) {
                            oksitut[i] = null;
                            continue;
                        }
                    }
                } else { // dx == 0, siirto ylös/alas
                    if (sy + dy != naapuri.getY()) {
                        oksitut[i] = null;
                        continue;
                    } else if (sx != naapuri.getX()) {
                        if (!ruudukko.ruutuEstynyt(naapuri.getX(), sy)) {
                            oksitut[i] = null;
                            continue;
                        }
                    }
                }
            }
        } else {
            // diagonaalinen siirto
            //len( [p(x), … , n] \ x ) < len( [p(x), x, n] )
            for (int i = 0; i < oksitut.length; ++i) {
                naapuri = oksitut[i];
                if (naapuri.equals(vanhempi)) {
                    oksitut[i] = null;
                    continue;
                }
                if (vx == naapuri.getX()) {
                    if (sy == naapuri.getY()) {
                        oksitut[i] = null;
                        continue;
                    } else if (sy + dy == naapuri.getY()) {
                        if (!ruudukko.ruutuEstynyt(vx, sy)) {
                            oksitut[i] = null;
                            continue;
                        }
                    }
                }
                if (vy == naapuri.getY()) {
                    if (sx == naapuri.getX()) {
                        oksitut[i] = null;
                        continue;
                    } else if (sx + dx == naapuri.getX()) {
                        if (!ruudukko.ruutuEstynyt(sx, vy)) {
                            oksitut[i] = null;
                            continue;
                        }
                    }
                }
            }
        }
        return oksitut;
    }

    /** Tarkistaa, onko solmulla pakotettuja naapureita
     *
     * @param solmu  Tarkasteltava solmu
     * @param suunta Kulkusuunta, eli
     *                  (solmu.x - tulosolmu.x, solmu.y - tulosolmu.y)
     * @return Palauttaa totuusarvon siitä, onko solmulla pakotettuja naapureita
     */
    public boolean pakotettujaNaapureita(Koordinaatti solmu, Koordinaatti suunta) {
        // "nollasuunta" tulee vain ensiaskeleella, eikä silloin karsita naapureita
        if (null == suunta || (suunta.getX() == 0 && suunta.getY() == 0)) {
            return false;
        }
        if (suunta.getX() != 0 && suunta.getY() != 0) {
            // diagonaali
            if (ruudukko.ruutuEstynyt(solmu.getX(), solmu.getY() - suunta.getY())) {
                if (!ruudukko.ruutuEstynyt(solmu.getX() + suunta.getX(), solmu.getY() - suunta.getY())) {
                    return true;
                }
            }
            if (ruudukko.ruutuEstynyt(solmu.getX() - suunta.getX(), solmu.getY())) {
                if (!ruudukko.ruutuEstynyt(solmu.getX() - suunta.getX(), solmu.getY() + suunta.getY())) {
                    return true;
                }
            }
        } else {
            // suora
            if (suunta.getX() != 0) { // vaakasuora
                if (ruudukko.ruutuEstynyt(solmu.getX(), solmu.getY() - 1)) {
                    if (!ruudukko.ruutuEstynyt(solmu.getX() + suunta.getX(), solmu.getY() - 1)) {
                        return true;
                    }
                }
                if (ruudukko.ruutuEstynyt(solmu.getX(), solmu.getY() + 1)) {
                    if (!ruudukko.ruutuEstynyt(solmu.getX() + suunta.getX(), solmu.getY() + 1)) {
                        return true;
                    }
                }
            } else { // pystysuora
                if (ruudukko.ruutuEstynyt(solmu.getX() - 1, solmu.getY())) {
                    if (!ruudukko.ruutuEstynyt(solmu.getX() - 1, solmu.getY() + suunta.getY())) {
                        return true;
                    }
                }
                if (ruudukko.ruutuEstynyt(solmu.getX() + 1, solmu.getY())) {
                    if (!ruudukko.ruutuEstynyt(solmu.getX() + 1, solmu.getY() + suunta.getY())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Koordinaatti hyppaa(Koordinaatti nykyinen, Koordinaatti suunta, Koordinaatti maali) {
        Koordinaatti solmu = new Koordinaatti(nykyinen.getX() + suunta.getX(), nykyinen.getY() + suunta.getY());
        if (ruudukko.ruutuEstynyt(solmu.getX(), solmu.getY())) {
            return null;
        }
        if (solmu.equals(maali)) {
            return solmu;
        }
        if (pakotettujaNaapureita(solmu, suunta)) {
            return solmu;
        }
        if (suunta.getX() != 0 && suunta.getY() != 0) {
            Koordinaatti[] suorat = {
                new Koordinaatti(suunta.getX(), 0),
                new Koordinaatti(0, suunta.getY())
            };
            for (int i = 0; i < suorat.length; ++i) {
                if (hyppaa(solmu, suorat[i], maali) != null) {
                    return solmu;
                }
            }
        }
        return hyppaa(solmu, suunta, maali);
    }

    private Koordinaatti[] seuraajat(Koordinaatti solmu, Koordinaatti vanhempi, Koordinaatti maali) {
        Koordinaatti[] naapurit = this.oksi(solmu, vanhempi);
        Koordinaatti naapuri;
        for (int i = 0; i < naapurit.length; ++i) {
            naapuri = naapurit[i];
            if (null == naapuri) {
                continue;
            }
            naapurit[i] = this.hyppaa(
                solmu,
                new Koordinaatti(naapuri.getX() - solmu.getX(), naapuri.getY() - solmu.getY()),
                maali
            );
        }
        return naapurit;
    }

    private boolean valmis = false;

    private Koordinaatti lahto;
    private Koordinaatti maali;
    private Koordinaatti nykyinen;
    private Koordinaatti tulos;

    private PriorityQueue<Pari<Double, Koordinaatti>> avoimetSolmut;
    private boolean[][] avoimissaSolmuissa;
    private Koordinaatti[][] tulosuunnat;
    private double[][] halvinReittiTahan;
    private double[][] halvinReittiMaaliin;

    @Override
    public void alusta(int lahtoX, int lahtoY, int maaliX, int maaliY) {
        this.diagnostiikka = new Diagnostiikka();
        this.diagnostiikka.aloitaSuoritus();

        this.nykyinen = null;
        this.lahto = new Koordinaatti(lahtoX, lahtoY);
        this.maali = new Koordinaatti(maaliX, maaliY);
        this.valmis = false;

        // Paetaan sen kummemmitta, mikäli koordinaatit ruudukon ulkopuolella
        if (!this.ruudukko.ruudukonSisalla(maaliX, maaliY) || !this.ruudukko.ruudukonSisalla(lahtoX, lahtoY)) {
            this.valmis = true;
            this.diagnostiikka.paataSuoritus();
            return;
        }

        // Sisältää mahdollisesti läpikäyntiä tarvitsevat solmut minimikekona
        // Parin a-komponentin suhteen. Parin a-komponenttina on siihen
        // täsmäävän solmun paras hinta-arvio.
        this.avoimetSolmut =
                new PriorityQueue<>(
                    (pA, pB) -> pA.getA().compareTo(pB.getA())
                );
        // aluksi sisältää vain lähtösolmun
        this.avoimetSolmut.add(new Pari<>(0.0, new Koordinaatti(lahtoX, lahtoY)));
        // ja avustava hakutaulu solmujen esiintymiseen edeltävässä tietorakenteessa
        this.avoimissaSolmuissa = new boolean[this.ruudukko.getKorkeus()][this.ruudukko.getLeveys()];

        // Kätevä hakutaulu tulosuunnallemme. Sisältää käsiteltävän polun
        // jokaista koordinaattia (x,y) vastaavan sitä edeltäneen koordinaatin
        // kohdassa tulosuunnat[y][x].
        this.tulosuunnat = new Koordinaatti[this.ruudukko.getKorkeus()][this.ruudukko.getLeveys()];

        // Solmulle (x,y) sisältää kohdassa [y][x] halvimman tähän mennessä
        // tunnetun lähdöstä solmuun (x,y) vievän polun
        this.halvinReittiTahan = new double[this.ruudukko.getKorkeus()][this.ruudukko.getLeveys()];

        // Solmulle (x,y) sisältää arvon halvinReittiTahan[y][x] + heuristiikka(x, y, maaliX, maaliY).
        // Tämä sisältää toisin sanoen nykyisen parhaan hinta-arvion polulle.
        this.halvinReittiMaaliin = new double[this.ruudukko.getKorkeus()][this.ruudukko.getLeveys()];

        // Alustetaan edeltävät taulukot
        for (int y = 0; y < this.ruudukko.getKorkeus(); ++y) {
            for (int x = 0; x < this.ruudukko.getLeveys(); ++x) {
                this.avoimissaSolmuissa[y][x] = false;
                this.tulosuunnat[y][x] = null;
                this.halvinReittiTahan[y][x] = Double.POSITIVE_INFINITY;
                this.halvinReittiMaaliin[y][x] = Double.POSITIVE_INFINITY;
            }
        }

        this.halvinReittiTahan[lahtoY][lahtoX] = 0;
        this.halvinReittiMaaliin[lahtoY][lahtoX] = this.heuristiikka.lyhinMahdollinenEtaisyys(lahtoX, lahtoY, maaliX, maaliY);

        this.diagnostiikka.paataSuoritus();
    }

    @Override
    public boolean onkoValmis() {
        return this.valmis;
    }

    @Override
    public Koordinaatti[] keraaNykyinenReitti() {
        return this.kokoaReitti(this.nykyinen);
    }

    @Override
    public Koordinaatti[] keraaTulos() {
        return this.kokoaReitti(this.tulos);
    }

    @Override
    public void suoritaSykli() {
        if (this.onkoValmis()) {
            return;
        }
        this.diagnostiikka.aloitaSuoritus();
        if (null == avoimetSolmut.peek()) {
            this.valmis = true;
            this.nykyinen = null;
            this.diagnostiikka.paataSuoritus();
            return;
        }
        this.diagnostiikka.suoritaSykli();
        this.diagnostiikka.kayRuudussa();
        nykyinen = avoimetSolmut.poll().getB();
        if (nykyinen.equals(maali)) {
            this.tulos = nykyinen;
            this.diagnostiikka.paataSuoritus();
            return;
        }
        int nykyinenX = nykyinen.getX();
        int nykyinenY = nykyinen.getY();
        avoimissaSolmuissa[nykyinenY][nykyinenX] = false;

        int maaliX = maali.getX();
        int maaliY = maali.getY();
        Koordinaatti[] seuraajat = this.seuraajat(nykyinen, tulosuunnat[nykyinenY][nykyinenX], maali);
        for (int i = 0; i < seuraajat.length; ++i) {
            Koordinaatti naapuri = seuraajat[i];
            if (null == naapuri) {
                continue;
            }
            int naapuriX = naapuri.getX();
            int naapuriY = naapuri.getY();
            int dx = naapuriX - nykyinenX;
            int dy = naapuriY - nykyinenY;
            double paino = Math.sqrt(dx * dx + dy * dy);
            if (this.ruudukko.ruutuEstynyt(naapuriX, naapuriY)) {
                continue;
            }
            double mahdollinenHalvinReittiTahan = halvinReittiTahan[nykyinenY][nykyinenX] + paino;
            if (mahdollinenHalvinReittiTahan < halvinReittiTahan[naapuriY][naapuriX]) {
                tulosuunnat[naapuriY][naapuriX] = nykyinen;
                halvinReittiTahan[naapuriY][naapuriX] = mahdollinenHalvinReittiTahan;
                halvinReittiMaaliin[naapuriY][naapuriX] = mahdollinenHalvinReittiTahan + this.heuristiikka.lyhinMahdollinenEtaisyys(naapuriX, naapuriY, maaliX, maaliY);
                if (!avoimissaSolmuissa[naapuriY][naapuriX]) {
                    avoimetSolmut.add(new Pari<>(halvinReittiMaaliin[naapuriY][naapuriX], naapuri));
                    avoimissaSolmuissa[naapuriY][naapuriX] = true;
                }
            }
        }
        this.diagnostiikka.paataSuoritus();
    }

    /** Suorittaa JPS-reitinhakualgoritmin asetetussa ruudukossa
     *
     * @param lahtoX Lähtöpisteen x-koordinaatti
     * @param lahtoY Lähtöpisteen y-koordinaatti
     * @param maaliX Maalin x-koordinaatti
     * @param maaliY Maalin y-koordinaatti
     * @return Taulukko koordinaatteja lähdöstä maaliin tai null
     */
    @Override
    public Koordinaatti[] etsiReitti(int lahtoX, int lahtoY, int maaliX, int maaliY) {
        this.alusta(lahtoX, lahtoY, maaliX, maaliY);
        while (!this.onkoValmis()) {
            this.suoritaSykli();
        }
        return this.keraaTulos();
    }

    @Override
    public void asetaRuudukko(Ruudukko ruudukko) {
        this.ruudukko = ruudukko;
    }

    @Override
    public Diagnostiikka getDiagnostiikka() {
        return this.diagnostiikka;
    }
}
