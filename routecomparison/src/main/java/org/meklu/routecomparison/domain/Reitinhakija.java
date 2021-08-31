
package org.meklu.routecomparison.domain;

/** Rajapinta reitinhakualgoritmille käyttöliittymää varten.
 */
interface Reitinhakija {
    /** Tekee reitinhaun ruudukossa pisteiden A ja B välillä alusta loppuun
     *
     * @param ax A-pisteen x-koordinaatti
     * @param ay A-pisteen y-koordinaatti
     * @param bx B-pisteen x-koordinaatti
     * @param by B-pisteen y-koordinaatti
     * @return   Taulukko, joka sisältää löytyneen reitin solmujen koordinaatit
     *           järjestyksessä A:sta B:hen tai null
     */
    public Koordinaatti[] etsiReitti(int ax, int ay, int bx, int by);
    /** Alustaa vaiheittain suoritettavan reitinhaun ruudukossa pisteiden A ja B
     * välillä.
     *
     * <p>Tarvitaan vain vaiheittain suoritettavissa reitinhauissa.
     *
     * @param ax A-pisteen x-koordinaatti
     * @param ay A-pisteen y-koordinaatti
     * @param bx B-pisteen x-koordinaatti
     * @param by B-pisteen y-koordinaatti
     *
     * @see {@link #suoritaSykli()}
     * @see {@link #onkoValmis()}
     * @see {@link #keraaTulos()}
     * @see {@link #keraaNykyinenReitti()}
     */
    public void alusta(int ax, int ay, int bx, int by);
    /** Suorittaa yhden syklin vaiheittain suoritettavaa reitinhakua.
     *
     * @see {@link #alusta()}
     * @see {@link #onkoValmis()}
     * @see {@link #keraaTulos()}
     */
    public void suoritaSykli();
    /** Tarkistaa, onko vaiheittain suoritettava reitinhaku valmis.
     *
     * @return Totuusarvo siitä, onko vaiheittain suoritettava reitinhaku valmis
     *
     * @see {@link #alusta()}
     * @see {@link #suoritaSykli()}
     * @see {@link #keraaTulos()}
     */
    public boolean onkoValmis();
    /** Kerää vaiheittain suoritettavan reitinhaun tuottaman reitin.
     *
     * @return   Taulukko, joka sisältää löytyneen reitin solmujen koordinaatit
     *           järjestyksessä A:sta B:hen tai null
     *
     * @see {@link #alusta()}
     * @see {@link #suoritaSykli()}
     * @see {@link #onkoValmis()}
     * @see {@link #keraaNykyinenReitti()}
     */
    public Koordinaatti[] keraaTulos();
    /** Kerää reitinhakijan tällä hetkellä arvioitavan reitin.
     *
     * <p>Hyödyllinen esim. visualisaatiossa
     *
     * @return Taulukko reitin sisältämiä koordinaatteja
     *
     * @see {@link #alusta()}
     * @see {@link #suoritaSykli()}
     * @see {@link #onkoValmis()}
     * @see {@link #keraaTulos()}
     */
    public Koordinaatti[] keraaNykyinenReitti();
    /** Asettaa reitinhakijan käyttämän ruudukon.
     *
     * <p>Tätä ei tule käyttää kesken vaiheittain suoritettavaa reitinhakua
     *
     * @param ruudukko Ruudukko, jossa reittejä tullaan hakemaan
     */
    public void asetaRuudukko(Ruudukko ruudukko);
    /** Palauttaa reitinhakijaan kuuluvan diagnostiikkainstanssin
     *
     * @return Diagnostiikka tälle reitinhakijainstanssille.
     */
    public Diagnostiikka getDiagnostiikka();
}
