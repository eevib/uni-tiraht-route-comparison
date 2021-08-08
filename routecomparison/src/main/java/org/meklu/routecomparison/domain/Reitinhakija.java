
package org.meklu.routecomparison.domain;

/** Rajapinta reitinhakualgoritmille käyttöliittymää varten.
 */
interface Reitinhakija {
    /** Tekee reitinhaun ruudukossa pisteiden A ja B välillä
     *
     * @param ax A-pisteen x-koordinaatti
     * @param ay A-pisteen y-koordinaatti
     * @param bx B-pisteen x-koordinaatti
     * @param by B-pisteen y-koordinaatti
     * @return   Taulukko, joka sisältää löytyneen reitin solmujen koordinaatit
     *           järjestyksessä A:sta B:hen
     */
    public Koordinaatti[] etsiReitti(int ax, int ay, int bx, int by);
    /** Asettaa reitinhakijan käyttämän ruudukon
     *
     * @param ruudukko Ruudukko, jossa reittejä tullaan hakemaan
     */
    public void asetaRuudukko(Ruudukko ruudukko);
}
