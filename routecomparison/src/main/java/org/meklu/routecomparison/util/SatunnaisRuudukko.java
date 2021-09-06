
package org.meklu.routecomparison.util;

import org.meklu.routecomparison.domain.Ruudukko;

import java.util.Random;

public class SatunnaisRuudukko {
    private Random random;
    private int leveys;
    private int korkeus;

    public SatunnaisRuudukko(int leveys, int korkeus) {
        this.random = new Random();
        this.leveys = Math.max(1, leveys);
        this.korkeus = Math.max(1, korkeus);
    }

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
