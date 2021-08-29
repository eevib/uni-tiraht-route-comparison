
package org.meklu.routecomparison.domain;

/** Diagnostiikkapalikka reitinhakualgoritmin suorituksen tiedonkeruuta varten.
 */
public class Diagnostiikka {
    private int ruuduissaKayty = 0;
    private int syklejaSuoritettu = 0;

    private Long suoritusaika = 0L;
    private Long suoritusAloitettu = null;
    private Long suoritusPaattynyt = null;

    /** Inkrementoi käytyjen ruutujen laskuria
     */
    public void kayRuudussa() {
        ruuduissaKayty += 1;
    }

    /** Inkrementoi syklilaskuria
     */
    public void suoritaSykli() {
        syklejaSuoritettu += 1;
    }

    /** Palauttaa käytyjen ruutujen määrä
     *
     * @return Käytyjen ruutujen määrä
     */
    public int getRuuduissaKayty() {
        return ruuduissaKayty;
    }

    /** Palauttaa suoritettujen syklien määrän
     *
     * @return Suoritettujen syklien määrä.
     */
    public int getSyklejaSuoritettu() {
        return syklejaSuoritettu;
    }

    /** Asettaa suorituksen aloitusajan
     */
    public void aloitaSuoritus() {
        this.suoritusAloitettu = System.nanoTime();
        this.suoritusPaattynyt = null;
    }

    /** Asettaa suorituksen päättymisajan ja lisää erotuksen suoritusaikaan
     */
    public void paataSuoritus() {
        if (null == this.suoritusAloitettu) {
            return;
        }
        this.suoritusPaattynyt = System.nanoTime();
        this.suoritusaika += this.suoritusPaattynyt - this.suoritusAloitettu;
        this.suoritusAloitettu = null;
        this.suoritusPaattynyt = null;
    }

    /** Palauttaa suoritusajan nanosekunteina
     *
     * @return Suoritusaika nanosekunteina.
     */
    public long getSuoritusaikaNs() {
        return suoritusaika;
    }

    /** Palauttaa suoritusajan sekunteina.
     *
     * @return Suoritusaika sekunteina.
     */
    public double getSuoritusaikaS() {
        return (double) this.getSuoritusaikaNs() / 1000000000.0;
    }

    @Override
    public String toString() {
        return "Diagnostiikka{" + "ruuduissaKayty=" + ruuduissaKayty + ", syklejaSuoritettu=" + syklejaSuoritettu + ", suoritusAika=" + this.getSuoritusaikaS() + "s}";
    }
}
