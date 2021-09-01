
package org.meklu.routecomparison.util;

import org.meklu.routecomparison.domain.Ruudukko;

import java.util.LinkedList;
import java.util.Scanner;

/** Lukee ruudukon ASCII-muotoisesta tiedostosta.
 *
 * TODO: kuvaile tiedostomuoto
 */
public class ASCIILukija {
    private final static String OTSAKE = "thrc";

    private Scanner lukija;

    public ASCIILukija(Scanner lukija) {
        this.lukija = lukija;
    }

    private boolean ohitettavaMerkki(char merkki) {
        switch (merkki) {
            case ' ':
            case '\t':
            case '\r':
            case '\n':
            case '\0':
                return true;
        }
        return false;
    }

    public Ruudukko lue() {
        Ruudukko ruudukko = null;
        String rivi;
        String siivottuRivi;
        char merkki;
        char vapaa = '\0';
        char este = '\0';

        // Luetaan otsake
        if (!this.lukija.hasNextLine()) {
            return null;
        }
        rivi = this.lukija.nextLine();
        if (!rivi.startsWith(OTSAKE)) {
            // epäkelpo tiedosto
            return null;
        }
        for (int i = OTSAKE.length(); i < rivi.length(); ++i) {
            merkki = rivi.charAt(i);
            if (this.ohitettavaMerkki(merkki)) {
                continue;
            }
            if ('\0' == vapaa) {
                vapaa = merkki;
            } else {
                este = merkki;
                break;
            }
        }
        // Otsakkeessa ei määritelty vapaan ruudun ja/tai estyneen ruudun
        // merkkejä
        if ('\0' == vapaa || '\0' == este) {
            return null;
        }

        int leveys = 0;
        int korkeus = 0;

        LinkedList<String> rivit = new LinkedList<>();

        while (this.lukija.hasNextLine()) {
            rivi = this.lukija.nextLine();
            // Ensimmäisen rivin ollessa kyseessä, selvitetään ruudukon leveys
            if (0 == leveys) {
                for (int i = 0; i < rivi.length(); ++i) {
                    merkki = rivi.charAt(i);
                    if (merkki != vapaa && merkki != este) {
                        continue;
                    }
                    leveys += 1;
                }
            }
            // Jos ensimmäinen rivi oli nollalevyinen, heitetään hansikkaat
            // tiskiin
            if (0 == leveys) {
                return null;
            }
            siivottuRivi = "";
            for (int i = 0; i < rivi.length(); ++i) {
                merkki = rivi.charAt(i);
                if (merkki != vapaa && merkki != este) {
                    continue;
                }
                siivottuRivi += merkki;
            }
            rivit.add(siivottuRivi);
            korkeus += 1;
        }

        if (0 == korkeus) {
            return null;
        }

        try {
            ruudukko = new Ruudukko(leveys, korkeus);
        } catch (Exception ex) {
            System.out.println("Ruudukon luonti epäonnistui.");
            return null;
        }

        int y = 0;
        for (String r : rivit) {
            for (int x = 0; x < leveys && x < r.length(); ++x) {
                merkki = r.charAt(x);
                ruudukko.asetaEste(merkki == este, x, y);
            }
            ++y;
        }

        return ruudukko;
    }
}
