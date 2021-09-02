
package org.meklu.routecomparison.domain;

import org.meklu.routecomparison.util.Reitintutkija;

/** Diagnostiikkapalikka reitinhakualgoritmin suorituksen tiedonkeruuta varten.
 */
public class Diagnostiikka {
    private int ruuduissaKayty = 0;
    private int ruuduissaUudelleenKayty = 0;
    private int syklejaSuoritettu = 0;

    private Long suoritusaika = 0L;
    private Long suoritusAloitettu = null;
    private Long suoritusPaattynyt = null;

    private Reitinhakija reitinhakija;
    private Reitintutkija reitintutkija = null;

    private Ruudukko kaydytRuudut = null;
    private boolean valmis = false;

    public Diagnostiikka(Reitinhakija rh) {
        this.reitinhakija = rh;
        try {
            Ruudukko ruudukko = rh.getRuudukko();
            this.kaydytRuudut = new Ruudukko(ruudukko.getLeveys(), ruudukko.getKorkeus());
        } catch (Exception ex) {
            System.out.println("Diagnostiikan ruudukonluonti epäonnistui. Käytyjen ruutujen seuraaminen pois käytöstä.");
        }
    }

    /** Inkrementoi käytyjen ruutujen laskuria
     *
     * @param ruutu Ruutu, jossa käydään
     */
    public void kayRuudussa(Koordinaatti ruutu) {
        if (null == ruutu) {
            return;
        }
        ruuduissaKayty += 1;
        if (null == this.kaydytRuudut) {
            return;
        }
        if (!this.kaydytRuudut.ruudukonSisalla(ruutu.getX(), ruutu.getY())) {
            return;
        }
        if (this.kaydytRuudut.ruutuEstynyt(ruutu.getX(), ruutu.getY())) {
            this.ruuduissaUudelleenKayty += 1;
        }
        this.kaydytRuudut.asetaEste(true, ruutu.getX(), ruutu.getY());
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

    /** Palauttaa ruudukon, jonka esteet kuvaavat käytyjä ruutuja
     *
     * @return Ruudukko, johon on merkitty käydyt ruudut esteinä.
     */
    public Ruudukko getKaydytRuudut() {
        return this.kaydytRuudut;
    }

    /** Palauttaa ruuduissa uudelleenkäyntien määrän
     *
     * @return Ruuduissa uudelleenkäyntien määrä
     */
    public int getRuuduissaUudelleenKayty() {
        return this.ruuduissaUudelleenKayty;
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

    /** Asettaa tulosreitin
     *
     * @param tulos Reitinhakijan tulos
     */
    public void asetaTulos(Koordinaatti[] tulos) {
        this.valmis = true;
        this.reitintutkija = new Reitintutkija(this.reitinhakija.getRuudukko(), tulos);
        this.reitinhakija.konfiguroiReitintutkija(this.reitintutkija);
    }

    /** Palauttaa tulokselle ajetun reitintutkijan
     *
     * @return Reitintutkija tai null, jos reitinhaku ei ole valmis.
     */
    public Reitintutkija getReitintutkija() {
        if (this.valmis) {
            return this.reitintutkija;
        }
        return null;
    }

    @Override
    public String toString() {
        return
                "Diagnostiikka{\n" +
                "   ruuduissaKayty=" + ruuduissaKayty + ",\n" +
                "   ruuduissaUudelleenKayty=" + ruuduissaUudelleenKayty + ",\n" +
                "   syklejaSuoritettu=" + syklejaSuoritettu + ",\n" +
                "   suoritusAika=" + this.getSuoritusaikaS() + "s\n" +
                "}";
    }
}
