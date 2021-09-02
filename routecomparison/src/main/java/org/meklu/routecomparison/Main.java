
package org.meklu.routecomparison;

import org.meklu.routecomparison.domain.*;
import org.meklu.routecomparison.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.print("Nykyinen hakemisto: ");
        System.out.println(new java.io.File(".").getCanonicalPath());

        if (0 == args.length) {
            args = new String[] { "-random" };
        }

        for (String tiedostoNimi : args) {
            ASCIILukija lukija;
            SatunnaisRuudukko satunnaisruudukko;
            Ruudukko ruudukko = null;
            try {
                if (tiedostoNimi.startsWith("-random")) {
                    satunnaisruudukko = new SatunnaisRuudukko(100, 100);
                    ruudukko = satunnaisruudukko.generoi(3, 8);
                } else {
                    lukija = new ASCIILukija(new java.util.Scanner(new java.io.File(tiedostoNimi)));
                    ruudukko = lukija.lue();
                }
            } catch (Exception e) {
            }
            if (null != ruudukko) {
                System.out.println("Luettiin ruudukko tiedostosta `" + tiedostoNimi + "'.");
                System.out.println();
            } else {
                System.out.println("Lukiessa ruudukkoa tiedostosta `" + tiedostoNimi + "' sattui virhe.");
                System.out.println();
                continue;
            }

            Heuristiikka heuristiikka = new Heuristiikka();
            Reitinhakija[] reitinhakijat = {
                new AStar(ruudukko, heuristiikka),
                new JPS(ruudukko, heuristiikka)
            };
            String[] otsikot = {
                "=== A*  ===",
                "=== JPS ==="
            };
            for (int i = 0; i < reitinhakijat.length; ++i) {
                String otsikko = otsikot[i];
                Reitinhakija rh = reitinhakijat[i];
                System.out.println(otsikko);
                Koordinaatti[] reitti = rh.etsiReitti(0, 0, ruudukko.getLeveys() - 1, ruudukko.getKorkeus() - 1);
                System.out.println(rh.getDiagnostiikka());
                rh.getDiagnostiikka().getReitintutkija().tulostaTekstina();
            }
        }
    }
}
