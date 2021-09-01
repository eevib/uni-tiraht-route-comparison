
package org.meklu.routecomparison;

import org.meklu.routecomparison.domain.*;
import org.meklu.routecomparison.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.print("Nykyinen hakemisto: ");
        System.out.println(new java.io.File(".").getCanonicalPath());

        ASCIILukija lukija = new ASCIILukija(new java.util.Scanner(new java.io.File("./thrc/bar.thrc")));
        Ruudukko ruudukko = lukija.lue();

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
