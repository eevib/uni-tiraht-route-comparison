
package org.meklu.routecomparison.util;

import org.meklu.routecomparison.domain.Ruudukko;

import java.util.Random;

/** Luokka satunnaisten ruudukkojen luomiseen
 */
public class SatunnaisRuudukko {
    private Random random;
    private int leveys;
    private int korkeus;

    /** Luo satunnaisruudukkoinstanssin halutunkokoisten ruudukoiden luontiin
     *
     * @param leveys  Luotavien ruudukkojen leveys
     * @param korkeus Luotavien ruudukkojen korkeus
     */
    public SatunnaisRuudukko(int leveys, int korkeus) {
        this.random = new Random();
        this.leveys = Math.max(1, leveys);
        this.korkeus = Math.max(1, korkeus);
    }

    /** Luo suhteessa luku / jakaja täytetyn ruudukon, kun oletetaan
     * satunnaislukugeneraattorin tuottavan tasainen levinneisyys
     *
     * @param luku   Arvo, jota pienemmät satunnaisluvut muunnetaan esteiksi
     * @param jakaja Eksklusiivinen yläraja tuotetuille satunnaisluvuille
     * @return Ruudukko, jossa on esteitä suunnilleen (luku / jakaja) * n.
     */
    public Ruudukko generoi(int luku, int jakaja) {
        luku = Math.max(0, luku);
        jakaja = Math.max(1, jakaja);
        Ruudukko ruudukko;
        try {
            ruudukko = new Ruudukko(leveys, korkeus);
        } catch (Exception ex) {
            return null;
        }
        for (int y = 0; y < korkeus; ++y) {
            for (int x = 0; x < leveys; ++x) {
                if (this.random.nextInt(jakaja) < luku) {
                    ruudukko.asetaEste(true, x, y);
                }
            }
        }
        return ruudukko;
    }
}
