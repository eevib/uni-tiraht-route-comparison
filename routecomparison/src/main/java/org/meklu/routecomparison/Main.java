
package org.meklu.routecomparison;

import org.meklu.routecomparison.domain.*;
import org.meklu.routecomparison.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.print("Nykyinen hakemisto: ");
        System.out.println(new java.io.File(".").getCanonicalPath());

        if (0 == args.length) {
            args = new String[] { "./thrc/foo.thrc", "./thrc/bar.thrc", "./thrc/zot.thrc" };
        }

        for (String tiedostoNimi : args) {
            ASCIILukija lukija = new ASCIILukija(new java.util.Scanner(new java.io.File(tiedostoNimi)));
            Ruudukko ruudukko = lukija.lue();
            if (null != ruudukko) {
                System.out.println("Luettiin ruudukko tiedostosta `" + tiedostoNimi + "'.");
                System.out.println();
            }

            Heuristiikka heuristiikka = new Heuristiikka();
            System.out.println("=== A*  ===");
            AStar astar = new AStar(ruudukko, heuristiikka);
            Koordinaatti[] reitti = astar.etsiReitti(0, 0, ruudukko.getLeveys() - 1, ruudukko.getKorkeus() - 1);
            System.out.println(astar.getDiagnostiikka());
            Reitintutkija rt = new Reitintutkija(ruudukko, reitti);
            rt.tulostaTekstina();
            System.out.println("=== JPS ===");
            JPS jps = new JPS(ruudukko, heuristiikka);
            reitti = jps.etsiReitti(0, 0, ruudukko.getLeveys() - 1, ruudukko.getKorkeus() - 1);
            System.out.println(jps.getDiagnostiikka());
            rt = new Reitintutkija(ruudukko, reitti);
            rt.salliKolot(true);
            rt.tulostaTekstina();
        }
    }
}
