
package org.meklu.routecomparison.domain;

/** Ruudukkoa esittävä olio, jonka taustalla on totuusarvotaulukko
 *
 * TODO: tämä voisi olla interface ja/tai tästä voisi tehdä bittikarttaversion
 */
public class Ruudukko {
    private final int leveys;
    private final int korkeus;
    private boolean[][] ruudut;

    /** Luo ruudukon
     *
     * Ruudukko on alustavasti tyhjä, eli jokaisen ruudun arvo on epätosi.
     *
     * @param leveys     Luotavan ruudukon leveys ruutuina
     * @param korkeus    Luotavan ruudukon korkeus ruutuina
     *
     * @throws Exception Heittää poikkeuksen, jos annettu koko on epäkelpo
     */
    public Ruudukko(int leveys, int korkeus) throws Exception {
        if (leveys <= 0 || korkeus <= 0) {
            throw new Exception("Ruudukon koon tulee olla positiivinen");
        }
        this.leveys = leveys;
        this.korkeus = korkeus;
        this.ruudut = new boolean[korkeus][leveys];
        /* Varmistetaan ruudukon alustus, koska emme tiedä mitä JVM tekee :D */
        for (int y = 0; y < korkeus; ++y) {
            for (int x = 0; x < leveys; ++x) {
                this.ruudut[y][x] = false;
            }
        }
    }

    /** Palauttaa leveyden
     *
     * @return Leveys ruutuina
     */
    public int getLeveys() {
        return leveys;
    }

    /** Palauttaa korkeuden
     *
     * @return Korkeus ruutuina
     */
    public int getKorkeus() {
        return korkeus;
    }

    /** Tarkistaa, onko tietty koordinaatti ruudukon sisällä
     *
     * @param x x-koordinaatti
     * @param y y-koordinaatti
     * @return Tosi, jos koordinaatti on ruudukon sisällä.
     */
    public boolean ruudukonSisalla(int x, int y) {
        if (x < 0 || y < 0) {
            return false;
        }
        if (x >= this.leveys || y >= this.korkeus) {
            return false;
        }
        return true;
    }

    /** Asettaa esteen johonkin ruutuun
     *
     * @param tila Tosi asettaa esteen, epätosi poistaa esteen.
     * @param x    Käsiteltävän ruudun x-koordinaatti
     * @param y    Käsiteltävän ruudun y-koordinaatti
     */
    public void asetaEste(boolean tila, int x, int y) {
        /* Ruudukon ulkopuolisiin ruutuihin ei kosketa */
        if (!this.ruudukonSisalla(x, y)) {
            return;
        }
        this.ruudut[y][x] = tila;
    }

    /** Palauttaa totuusarvon siitä, onko ruudussa este
     *
     * @param x Käsiteltävän ruudun x-koordinaatti
     * @param y Käsiteltävän ruudun y-koordinaatti
     * @return  Onko ruudussa este vai ei
     */
    public boolean ruutuEstynyt(int x, int y) {
        /* Ruudukon ulkopuoliset ruudut ovat esteitä */
        if (!this.ruudukonSisalla(x, y)) {
            return true;
        }
        return this.ruudut[y][x];
    }
}
