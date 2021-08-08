
package org.meklu.routecomparison;

import org.meklu.routecomparison.domain.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Ruudukko ruudukko = new Ruudukko(6, 4);
        Heuristiikka heuristiikka = new Heuristiikka();
        AStar astar = new AStar(ruudukko, heuristiikka);
        Koordinaatti[] reitti = astar.etsiReitti(0, 0, 5, 3);
        Ruudukko reittiVisualisaatio = new Ruudukko(ruudukko.getLeveys(), ruudukko.getKorkeus());
        for (int i = 0; i < reitti.length; ++i) {
            System.out.println(i + ": " + reitti[i].toString());
            reittiVisualisaatio.asetaEste(true, reitti[i].getX(), reitti[i].getY());
        }
        boolean tormays = false;
        for (int y = 0; y < ruudukko.getKorkeus(); ++y) {
            for (int x = 0; x < ruudukko.getLeveys(); ++x) {
                boolean este = ruudukko.ruutuEstynyt(x, y);
                boolean askel = reittiVisualisaatio.ruutuEstynyt(x, y);
                if (este && askel) {
                    System.out.print(" X");
                    tormays = true;
                } else if (este) {
                    System.out.print(" *");
                } else if (askel) {
                    System.out.print(" @");
                } else {
                    System.out.print(" -");
                }
            }
            System.out.println("");
        }
        if (tormays) {
            System.out.println("Törmäys havaittu! Yllä olevassa kartassa X merkkaa esteen ja reitin yhteentörmäystä samassa ruudussa.");
        }
    }
}
