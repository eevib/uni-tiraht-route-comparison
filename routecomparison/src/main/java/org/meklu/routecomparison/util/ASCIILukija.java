
package org.meklu.routecomparison.util;

import org.meklu.routecomparison.domain.Ruudukko;

import java.util.LinkedList;
import java.util.Scanner;

/** Lukee ruudukon ASCII-muotoisesta tiedostosta.
 *
 * <p>Tiedostomuoto on kirjainkokoriippuvainen.
 *
 * <p>Tiedoston otsakerivi koostuu "thrc" -etuliitteestä ja vapaan ja estyneen
 * ruudun merkeistä. Alla olevassa esimerkissä vapaan ruudun merkiksi on
 * asetettu merkki '0' ja estyneen ruudun merkiksi '1'. Tyhjät merkit ovat
 * vapaaehtoisia. Ruudukon koko on 4x3 ja siinä on este koordinaateissa (1, 2).
 * <pre>
 *    thrc 0 1
 *     0 0 0 0
 *     0 0 0 0
 *     0 1 0 0
 * </pre>
 *
 * <p>Tiedoston otsakkeessa voi määriteltyjen ruutumerkkien jälleen olla mitä
 * tahansa sisältöä, koska sen lukeminen lopetetaan estyneen ruudun merkin
 * jälkeen.
 * <pre>
 *    thrc a b Tämä sisältö voi olla esimerkiksi jokin hyödyllinen kommentti.
 *    a a a a
 *    a b b a
 * </pre>
 *
 * <p>Tiedoston ensimmäinen otsakkeen jälkeinen rivi määrittelee ruudukon
 * leveyden. Kaikki otsakkeessa määriteltyyn merkistöön kuulumattomat merkit
 * jätetään huomiotta.
 * <pre>
 *    thrc # X
 *     # # # #    Leveys on 4
 *    x#y#z#w#    Huhhels, tyhjä rivi
 *    -#-#-#-#-X  Tämän rivin X-merkkiä ei lasketa ruudukkoon
 * </pre>
 *
 * <p>Alimääritellyt rivit käsitellään oikealla olevana tyhjänä tilana
 * <pre>
 *    thrc - *
 *    - - - *
 *          * Tämä este onkin vasemmanpuoleisimmassa sarakkeessa!
 *    - *     Este on toisessa sarakkeessa vasemmalta ja loppurivi on tyhjä
 *            Tyhjä rivi on tyhjä rivi
 * </pre>
 */
public class ASCIILukija {
    private final static String OTSAKE = "thrc";

    private Scanner lukija;

    /** Luo ASCIILukija-instanssin
     *
     * @param lukija Tiedoston lukemiseen käytettävä Scanner-instanssi
     */
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

    /** Lukee ruudukon konstruktorissa annetun lukijan avulla
     *
     * @return Ruudukko, joka vastaa luettua tietoa tai null.
     */
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
