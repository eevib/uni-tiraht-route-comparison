
package org.meklu.routecomparison.util;

import org.meklu.routecomparison.domain.Koordinaatti;
import org.meklu.routecomparison.domain.Ruudukko;

/** Reitintutkija on apuluokka reitinhakijan tuottaman reitin tarkasteluun.
 *
 * Valtaosa tämän luokan tuottamasta tiedosta generoidaan suoraan
 * konstruktorissa.
 */
public class Reitintutkija {
    private final static double DIAGONAALIPAINO = Math.sqrt(2);
    private final static double SUORAPAINO = 1;
    private Ruudukko ruudukko;
    private Ruudukko reitti;

    boolean salliKolot = false;

    boolean reitissaKoloja = false;
    boolean reitissaTormayksia = false;
    boolean reitissaPaallekkaisyyksia = false;
    boolean reittiOhiRuudukosta = false;

    double reitinPituus = Double.NaN;
    int reitissaSolmuja = -1;

    /** Palauttaa reitin pituuden
     *
     * @return Reitin pituus
     */
    public double getReitinPituus() {
        return reitinPituus;
    }

    /** Palauttaa reitissä olevien solmujen määrän
     *
     * @return Reitin sisältämien solmujen määrä
     */
    public int getReitissaSolmuja() {
        return reitissaSolmuja;
    }

    /** Palauttaa totuusarvon siitä, ovatko kolot reitissä sallittu.
     *
     * <p>Vaikuttaa metodiin {@link #onkoReitissaOngelmia()}.
     *
     * @return Ovatko kolot sallittuja
     */
    public boolean sallitaankoKolot() {
        return salliKolot;
    }

    /** Sallii tai kieltää kolot reitissä.
     *
     * <p>Vaikuttaa metodiin {@link #onkoReitissaOngelmia()}.
     *
     * @param sallikolot Totuusarvo siitä, halutaanko kolot reitissä sallia
     */
    public void salliKolot(boolean sallikolot) {
        this.salliKolot = sallikolot;
    }

    /** Luo reitintutkijan
     *
     * @param ruudukko Ruudukko, jota reitti koskee
     * @param reitti   Reitti, jota käsitellään
     */
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
        this.reitissaSolmuja = reitti.length;
        this.reitinPituus = 0.0;
        int aiempiX = reitti[0].getX();
        int aiempiY = reitti[0].getY();
        for (int i = 0; i < reitti.length; ++i) {
            int nykyinenX = reitti[i].getX();
            int nykyinenY = reitti[i].getY();
            int dx = Math.abs(nykyinenX - aiempiX);
            int dy = Math.abs(nykyinenY - aiempiY);
            if (dx > 1 || dy > 1) {
                // Kolon hyppiminen, joten otetaan vain geometrinen etäisyys.
                // Periaatteessa tämä oltaisiin voitu ottaa joka kohdassa, mutta
                // tämä on laskennallisesti hitusen kalliimpaa kuin etukäteen
                // laskettujen painojen ynnääminen. Eipä tällä niin väliä ole.
                reitinPituus += Math.sqrt(dx * dx + dy * dy);
                reitissaKoloja = true;
            } else if (dx == 1 && dy == 1) {
                // Teoriassa saisimme paremman tarkkuuden, jos laskisimme
                // diagonaalisten siirtojen määrän ja ynnäisimme sen perusteella
                // hienomman luvun loppusummaan.
                reitinPituus += DIAGONAALIPAINO;
            } else if (dx == 1 || dy == 1) {
                reitinPituus += SUORAPAINO;
            }
            // Tässä olisi vielä tyhjä konditionaali (dx == 0 && dy == 0),
            // mutta se olisi vain NOP.
            if (!this.reitti.ruudukonSisalla(nykyinenX, nykyinenY)) {
                reittiOhiRuudukosta = true;
            } else {
                if (this.reitti.ruutuEstynyt(nykyinenX, nykyinenY)) {
                    reitissaPaallekkaisyyksia = true;
                }
                if (this.ruudukko.ruutuEstynyt(nykyinenX, nykyinenY)) {
                    reitissaTormayksia = true;
                }
            }
            this.reitti.asetaEste(true, nykyinenX, nykyinenY);
            aiempiX = nykyinenX;
            aiempiY = nykyinenY;
        }
    }

    /** Mikäli reitissä on ongelmia, tämä metodi kertoo siitä
     *
     * @return Tosi, jos reitissä on ongelmia
     */
    // Checkstyle on väärässä ja haluaa syvemmän sisennyksen returnin sulkevalle
    // sulkeelle. Jollain muullakin on sama ongelma:
    // https://stackoverflow.com/questions/47079346/how-allow-less-indent-for-closing-parentheses-inside-a-method-with-checkstyle
    @SuppressWarnings("checkstyle:Indentation")
    public boolean onkoReitissaOngelmia() {
        return (
            (this.salliKolot ^ this.onkoReitissaKoloja()) ||
            this.onkoReitissaPaallekkaisyyksia() ||
            this.onkoReitissaTormayksia() ||
            this.onkoReittiOhiRuudukosta()
        );
    }

    /** Palauttaa totuusarvon siitä, onko reitissä koloja.
     *
     * Tällä tarkoitetaan ei-naapurisolmujen välistä liikehdintää. Alla
     * esimerkki, jossa hyppy merkattu huutomerkillä:
     * <pre>
     *   _0_1_2_3_4_5_
     * 0| A - - - - - |
     * 1| - 2 - - - - |
     * 2| - - ! - - - |
     * 3| - - - 3 4 B |
     * </pre>
     *
     * @return Tosi, jos reitissä on koloja
     */
    public boolean onkoReitissaKoloja() {
        return reitissaKoloja;
    }

    /** Palauttaa totuusarvon siitä, onko reitissä törmäyksiä.
     *
     * Tällä tarkoitetaan reitin kulkevan estyneiden ruutujen kautta.
     *
     * @return Tosi, jos reitissä on törmäyksiä
     */
    public boolean onkoReitissaTormayksia() {
        return reitissaTormayksia;
    }

    /** Palauttaa totuusarvon siitä, onko reitissä päällekkäisyyksiä.
     *
     * Tällä tarkoitetaan sitä, että reitissä esiintyy sama solmu useammin kuin
     * vain yhden kerran.
     *
     * @return Tosi, jos reitissä on päällekkäisyyksiä
     */
    public boolean onkoReitissaPaallekkaisyyksia() {
        return reitissaPaallekkaisyyksia;
    }

    /** Palauttaa totuusarvon siitä, onko reitti käynyt ruudukon ulkopuolella
     *
     * @return Tosi, jos reitti käy ruudukon ulkopuolella
     */
    public boolean onkoReittiOhiRuudukosta() {
        return reittiOhiRuudukosta;
    }

    /** Luo reitistä ja ruudukosta karttaa esittävän merkkijonon
     *
     * <p>Alla selitteet ruudukossa mahdollisesti esiintyville merkeille:
     * <pre>
     *    {@literal @} : reitti kulkee tämän ruudun kautta
     *    * : tässä ruudussa on este
     *    - : tyhjä ruutu
     *    X : reitti on törmännyt esteeseen
     * </pre>
     *
     * @return Merkkijonoesitys reitistä ruudukkoa vasten, loppuen rivinvaihtoon
     */
    public String tekstiKartta() {
        String kartta = "";
        for (int y = 0; y < this.ruudukko.getKorkeus(); ++y) {
            for (int x = 0; x < this.ruudukko.getLeveys(); ++x) {
                boolean este = this.ruudukko.ruutuEstynyt(x, y);
                boolean askel = (this.reitti != null) && this.reitti.ruutuEstynyt(x, y);
                if (este && askel) {
                    kartta += " X";
                } else if (este) {
                    kartta += " *";
                } else if (askel) {
                    kartta += " @";
                } else {
                    kartta += " -";
                }
            }
            kartta += "\n";
        }
        return kartta;
    }

    /** Apufunktio reitin ja ruudukon tulostamiseen komentoriville.
     *
     * Tulostaa myös muut reitin ominaisuudet.
     */
    public void tulostaTekstina() {
        System.out.println(this.tekstiKartta());
        if (this.reitti == null) {
            System.out.println("Ei ratkaisua.");
            return;
        }
        System.out.println("Reitin pituus on " + this.reitinPituus + ", solmuja " + this.reitissaSolmuja + ".");
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
