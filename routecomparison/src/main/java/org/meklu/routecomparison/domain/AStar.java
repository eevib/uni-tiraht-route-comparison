
package org.meklu.routecomparison.domain;

import java.util.LinkedList;
import java.util.PriorityQueue;

/** Toteuttaa A* -reitinhakualgoritmin
 * 
 * Tämä implementaatio on mukailtu pseudokoodista, joka on haettu osoitteesta
 *    https://en.wikipedia.org/wiki/A*_search_algorithm
 */
public class AStar implements Reitinhakija {
    private Ruudukko ruudukko;
    private Heuristiikka heuristiikka;

    public AStar(Ruudukko ruudukko, Heuristiikka heuristiikka) {
        this.ruudukko = ruudukko;
        this.heuristiikka = heuristiikka;
    }

    private Koordinaatti[] kokoaReitti(Koordinaatti[][] tulosuunnat, Koordinaatti nykyinen) {
        LinkedList<Koordinaatti> reitti = new LinkedList<>();
        while (nykyinen != null) {
            reitti.addFirst(nykyinen);
            nykyinen = tulosuunnat[nykyinen.getY()][nykyinen.getX()];
        }
        Koordinaatti[] reittiTaulukko = new Koordinaatti[reitti.size()];
        int i = 0;
        for (Koordinaatti k : reitti) {
            reittiTaulukko[i] = k;
            ++i;
        }
        return reittiTaulukko;
    }

    @Override
    public Koordinaatti[] etsiReitti(int lahtoX, int lahtoY, int maaliX, int maaliY) {
        if (!this.ruudukko.ruudukonSisalla(maaliX, maaliY) || !this.ruudukko.ruudukonSisalla(lahtoX, lahtoY)) {
            return null;
        }

        PriorityQueue<Pari<Double, Koordinaatti>> avoimetSolmut =
                new PriorityQueue<Pari<Double, Koordinaatti>>(
                    (pA, pB) -> pA.getA().compareTo(pB.getA())
                );
        avoimetSolmut.add(new Pari<Double, Koordinaatti>(0.0, new Koordinaatti(lahtoX, lahtoY)));

        Koordinaatti[][] tulosuunnat = new Koordinaatti[this.ruudukko.getKorkeus()][this.ruudukko.getLeveys()];
        for (int y = 0; y < this.ruudukko.getKorkeus(); ++y) {
            for (int x = 0; x < this.ruudukko.getLeveys(); ++x) {
                tulosuunnat[y][x] = null;
            }
        }

        double[][] halvinReittiTahan = new double[this.ruudukko.getKorkeus()][this.ruudukko.getLeveys()];
        for (int y = 0; y < this.ruudukko.getKorkeus(); ++y) {
            for (int x = 0; x < this.ruudukko.getLeveys(); ++x) {
                halvinReittiTahan[y][x] = Double.POSITIVE_INFINITY;
            }
        }
        halvinReittiTahan[lahtoY][lahtoX] = 0;

        double[][] halvinReittiMaaliin = new double[this.ruudukko.getKorkeus()][this.ruudukko.getLeveys()];
        for (int y = 0; y < this.ruudukko.getKorkeus(); ++y) {
            for (int x = 0; x < this.ruudukko.getLeveys(); ++x) {
                halvinReittiMaaliin[y][x] = Double.POSITIVE_INFINITY;
            }
        }
        halvinReittiMaaliin[lahtoY][lahtoX] = this.heuristiikka.lyhinMahdollinenEtaisyys(lahtoX, lahtoY, maaliX, maaliY);

        Koordinaatti maali = new Koordinaatti(maaliX, maaliY);
        Koordinaatti nykyinen = null;

        double[] naapurienPainot = new double[8];
        Koordinaatti[] naapurit = new Koordinaatti[8];
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
        while (null != avoimetSolmut.peek()) {
            nykyinen = avoimetSolmut.poll().getB();
            if (nykyinen.equals(maali)) {
                return this.kokoaReitti(tulosuunnat, nykyinen);
            }
            int nykyinenX = nykyinen.getX();
            int nykyinenY = nykyinen.getY();

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
                    if (!avoimetSolmut.stream().anyMatch((p) -> p.getB().equals(naapuri))) {
                        avoimetSolmut.add(new Pari<>(halvinReittiMaaliin[naapuriY][naapuriX], naapuri));
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void asetaRuudukko(Ruudukko ruudukko) {
        this.ruudukko = ruudukko;
    }
}
