
package org.meklu.routecomparison;

import org.meklu.routecomparison.domain.*;
import org.meklu.routecomparison.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Ruudukko ruudukko = new Ruudukko(6, 4);
        for (int y = 0; y < ruudukko.getKorkeus() - 1; ++y) {
            ruudukko.asetaEste(true, 1, y);
        }
        for (int y = 1; y < ruudukko.getKorkeus(); ++y) {
            ruudukko.asetaEste(true, 3, y);
        }
        Heuristiikka heuristiikka = new Heuristiikka();
        System.out.println("=== A*  ===");
        AStar astar = new AStar(ruudukko, heuristiikka);
        Koordinaatti[] reitti = astar.etsiReitti(0, 0, 5, 3);
        System.out.println(astar.getDiagnostiikka());
        Reitintutkija rt = new Reitintutkija(ruudukko, reitti);
        rt.tulostaTekstina();
        System.out.println("=== JPS ===");
        JPS jps = new JPS(ruudukko, heuristiikka);
        reitti = jps.etsiReitti(0, 0, 5, 3);
        System.out.println(jps.getDiagnostiikka());
        rt = new Reitintutkija(ruudukko, reitti);
        rt.salliKolot(true);
        rt.tulostaTekstina();
    }
}
