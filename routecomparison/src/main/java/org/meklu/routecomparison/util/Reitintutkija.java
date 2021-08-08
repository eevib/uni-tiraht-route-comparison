
package org.meklu.routecomparison.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.meklu.routecomparison.domain.Koordinaatti;
import org.meklu.routecomparison.domain.Ruudukko;

public class Reitintutkija {
    private final static double diagonaalinenPaino = Math.sqrt(2);
    private final static double suoraPaino = 1;
    private Ruudukko ruudukko;
    private Ruudukko reitti;

    boolean reitissaKoloja = false;
    boolean reitissaTormayksia = false;
    boolean reitissaPaallekkaisyyksia = false;
    boolean reittiOhiRuudukosta = false;

    double reitinPituus = Double.NaN;

    public double getReitinPituus() {
        return reitinPituus;
    }

    public Reitintutkija(Ruudukko ruudukko, Koordinaatti[] reitti) {
        this.ruudukko = ruudukko;
        try {
            this.reitti = new Ruudukko(ruudukko.getLeveys(), ruudukko.getKorkeus());
        } catch (Exception ex) {
            System.out.println("Hupsista keikkaa.");
            this.reitti = null;
            return;
        }
        if (reitti == null) {
            this.reitti = null;
            return;
        }
        this.reitinPituus = 0.0;
        int aiempiX = reitti[0].getX();
        int aiempiY = reitti[0].getY();
        for (int i = 0; i < reitti.length; ++i) {
            int nykyinenX = reitti[i].getX();
            int nykyinenY = reitti[i].getY();
            int dx = Math.abs(nykyinenX - aiempiX);
            int dy = Math.abs(nykyinenY - aiempiY);
            if (dx > 1 || dy > 1) {
                reitissaKoloja = true;
            }
            if (dx == 1 && dy == 1) {
                reitinPituus += diagonaalinenPaino;
            } else if (dx == 1 && dy == 0 || dx == 0 && dy == 1) {
                reitinPituus += suoraPaino;
            } else if (dx == 0 && dy == 0) {
                // NOP
            } else {
                // Kolon hyppiminen, joten otetaan vain geometrinen etäisyys.
                // Periaatteessa tämä oltaisiin voitu ottaa joka kohdassa, mutta
                // tämä on laskennallisesti hitusen kalliimpaa kuin etukäteen
                // laskettujen painojen ynnääminen. Eipä tällä niin väliä ole.
                reitinPituus += Math.sqrt(dx * dx + dy * dy);
            }
            if (!this.reitti.ruudukonSisalla(nykyinenX, nykyinenY)) {
                reittiOhiRuudukosta = true;
            }
            if (this.reitti.ruutuEstynyt(nykyinenX, nykyinenY)) {
                reitissaPaallekkaisyyksia = true;
            }
            if (this.ruudukko.ruutuEstynyt(nykyinenX, nykyinenY)) {
                reitissaTormayksia = true;
            }
            this.reitti.asetaEste(true, nykyinenX, nykyinenY);
            aiempiX = nykyinenX;
            aiempiY = nykyinenY;
        }
    }

    public boolean onkoReitissaKoloja() {
        return reitissaKoloja;
    }

    public boolean onkoReitissaTormayksia() {
        return reitissaTormayksia;
    }

    public boolean onkoReitissaPaallekkaisyyksia() {
        return reitissaPaallekkaisyyksia;
    }

    public boolean onkoReittiOhiRuudukosta() {
        return reittiOhiRuudukosta;
    }

    public void tulostaTekstina() {
        for (int y = 0; y < this.ruudukko.getKorkeus(); ++y) {
            for (int x = 0; x < this.ruudukko.getLeveys(); ++x) {
                boolean este = this.ruudukko.ruutuEstynyt(x, y);
                boolean askel = (this.reitti != null) && this.reitti.ruutuEstynyt(x, y);
                if (este && askel) {
                    System.out.print(" X");
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
        if (this.reitti == null) {
            System.out.println("Ei ratkaisua.");
            return;
        }
        System.out.println("Reitin pituus on " + this.reitinPituus + ".");
        if (this.reitissaKoloja) {
            System.out.println("Reitissä on koloja. Reitin pituus voi olla erikoinen.");
        }
        if (this.reitissaTormayksia) {
            System.out.println("Törmäys havaittu! Yllä olevassa kartassa X merkkaa esteen ja reitin yhteentörmäystä samassa ruudussa.");
        }
        if (this.reittiOhiRuudukosta) {
            System.out.println("Reitti kulkee osittain ruudukon ulkopuolella.");
        }
        if (this.reitissaPaallekkaisyyksia) {
            System.out.println("Reitissä on havaittu sama solmu useampaan kertaan.");
        }
    }
}
