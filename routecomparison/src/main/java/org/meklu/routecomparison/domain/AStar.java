
package org.meklu.routecomparison.domain;

import java.util.PriorityQueue;
import org.meklu.routecomparison.util.Reitintutkija;

/** Toteuttaa A* -reitinhakualgoritmin
 *
 * Tämä implementaatio on mukailtu pseudokoodista, joka on haettu osoitteesta
 *    https://en.wikipedia.org/wiki/A*_search_algorithm
 */
public class AStar implements Reitinhakija {
    private Ruudukko ruudukko;
    private Heuristiikka heuristiikka;
    private Diagnostiikka diagnostiikka;

    private double[] naapurienPainot = new double[8];
    private Koordinaatti[] naapurit = new Koordinaatti[8];

    public AStar(Ruudukko ruudukko, Heuristiikka heuristiikka) {
        this.ruudukko = ruudukko;
        this.heuristiikka = heuristiikka;

        this.naapurienPainot = new double[8];
        this.naapurit = new Koordinaatti[8];
        double diagonaaliPaino = Math.sqrt(2);
        for (int i = 0, x = -1; x <= 1 && i < naapurit.length; ++x) {
            for (int y = -1; y <= 1 && i < naapurit.length; ++y) {
                if (x == 0 && y == 0) {
                    continue;
                }
                if (x != 0 && y != 0) {
                    naapurienPainot[i] = diagonaaliPaino;
                } else {
                    naapurienPainot[i] = 1;
                }
                naapurit[i] = new Koordinaatti(x, y);
                ++i;
            }
        }
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
        this.diagnostiikka = new Diagnostiikka(this);
        this.diagnostiikka.aloitaSuoritus();

        this.nykyinen = null;
        this.tulos = null;
        this.lahto = new Koordinaatti(lahtoX, lahtoY);
        this.maali = new Koordinaatti(maaliX, maaliY);
        this.valmis = false;

        // Paetaan sen kummemmitta, mikäli koordinaatit ruudukon ulkopuolella
        // tai ruudut ovat muutoin estyneet
        if (this.ruudukko.ruutuEstynyt(maaliX, maaliY) || this.ruudukko.ruutuEstynyt(lahtoX, lahtoY)) {
            this.valmis = true;
            this.diagnostiikka.paataSuoritus();
            return;
        }

        // Sisältää mahdollisesti läpikäyntiä tarvitsevat solmut minimikekona
        // Parin a-komponentin suhteen. Parin a-komponenttina on siihen
        // täsmäävän solmun paras hinta-arvio.
        this.avoimetSolmut =
                new PriorityQueue<>(
                    this.ruudukko.getLeveys() * this.ruudukko.getKorkeus(),
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
        Koordinaatti[] reitti = this.kokoaReitti(this.tulos);
        this.diagnostiikka.asetaTulos(reitti);
        return reitti;
    }

    @Override
    public void suoritaSykli() {
        if (this.onkoValmis()) {
            return;
        }
        this.diagnostiikka.aloitaSuoritus();
        if (null == avoimetSolmut.peek()) {
            this.valmis = true;
            this.diagnostiikka.paataSuoritus();
            return;
        }
        this.diagnostiikka.suoritaSykli();
        nykyinen = avoimetSolmut.poll().getB();
        this.diagnostiikka.kayRuudussa(nykyinen);
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

        for (int i = 0; i < naapurit.length; ++i) {
            int naapuriX = nykyinenX + naapurit[i].getX();
            int naapuriY = nykyinenY + naapurit[i].getY();
            double paino = naapurienPainot[i];
            Koordinaatti naapuri = new Koordinaatti(naapuriX, naapuriY);
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

    /** Suorittaa A*-reitinhakualgoritmin asetetussa ruudukossa
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

    @Override
    public Ruudukko getRuudukko() {
        return this.ruudukko;
    }

    @Override
    public void konfiguroiReitintutkija(Reitintutkija rt) {
        // A* ei tarvitse erikoisuuksia
    }

    @Override
    public String nimi() {
        return "A*";
    }
}
