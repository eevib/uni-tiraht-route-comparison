
import org.meklu.routecomparison.domain.*;
import org.meklu.routecomparison.util.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JPSTest {
    private Ruudukko ruudukko;
    private Heuristiikka heuristiikka;
    private JPS jps;

    @Before
    public void setUp() throws Exception {
        this.ruudukko = new Ruudukko(6, 4);
        this.heuristiikka = new Heuristiikka();
        this.jps = new JPS(this.ruudukko, this.heuristiikka);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void tyhjanRuudukonReittiOptimimittainen() {
        Koordinaatti[] reitti = jps.etsiReitti(0, 0, this.ruudukko.getLeveys() - 1, this.ruudukko.getKorkeus() - 1);
        assertNotNull(reitti);
        assertEquals(3, reitti.length);
        Reitintutkija rt = new Reitintutkija(ruudukko, reitti);
        rt.salliKolot(true);
        assertEquals(rt.getReitinPituus(), heuristiikka.lyhinMahdollinenEtaisyys(0, 0, this.ruudukko.getLeveys() - 1, this.ruudukko.getKorkeus() - 1), 0.0001);
        assertEquals(false, rt.onkoReitissaOngelmia());
    }

    @Test
    public void nullJosRuudukonUlkopuolinenLahtopiste() {
        Koordinaatti[] reitti = jps.etsiReitti(-1, -1, this.ruudukko.getLeveys() - 1, this.ruudukko.getKorkeus() - 1);
        assertNull(reitti);
    }

    @Test
    public void nullJosRuudukonUlkopuolinenMaalipiste() {
        Koordinaatti[] reitti = jps.etsiReitti(0, 0, this.ruudukko.getLeveys(), this.ruudukko.getKorkeus());
        assertNull(reitti);
    }

    @Test
    public void yksinkertaisenRuudukonReittiOnOptimi() {
        /*
          | @ * - - - - |
          | - * - - - - |
          | @ * - - - - |
          | - @ - - - @ |
          |Reitin pituus on 7.414213562373095, solmuja 4.
        */
        for (int y = 0; y < ruudukko.getKorkeus() - 1; ++y) {
            ruudukko.asetaEste(true, 1, y);
        }
        Koordinaatti[] reitti = jps.etsiReitti(0, 0, this.ruudukko.getLeveys() - 1, this.ruudukko.getKorkeus() - 1);
        assertNotNull(reitti);
        Reitintutkija rt = new Reitintutkija(ruudukko, reitti);
        rt.salliKolot(true);
        assertEquals(7.4142, rt.getReitinPituus(), 0.0001);
        assertEquals(4, rt.getReitissaSolmuja());
        assertEquals(false, rt.onkoReitissaOngelmia());
    }

    @Test
    public void hienommanRuudukonReittiOnOptimi() {
        /*
          | @ * - @ - - |
          | - * @ * - - |
          | @ * @ * - @ |
          | - @ - * - @ |
          |Reitin pituus on 11.071067811865476, solmuja 8.
        */
        for (int y = 0; y < ruudukko.getKorkeus() - 1; ++y) {
            ruudukko.asetaEste(true, 1, y);
        }
        for (int y = 1; y < ruudukko.getKorkeus(); ++y) {
            ruudukko.asetaEste(true, 3, y);
        }
        Koordinaatti[] reitti = jps.etsiReitti(0, 0, this.ruudukko.getLeveys() - 1, this.ruudukko.getKorkeus() - 1);
        assertNotNull(reitti);
        Reitintutkija rt = new Reitintutkija(ruudukko, reitti);
        rt.salliKolot(true);
        assertEquals(11.0710, rt.getReitinPituus(), 0.0001);
        assertEquals(8, rt.getReitissaSolmuja());
        assertEquals(false, rt.onkoReitissaOngelmia());
    }

    @Test
    public void oksiIlmanVanhempaaKaikkiNaapuritMukana() {
        Koordinaatti vanhempi = null;
        Koordinaatti solmu = new Koordinaatti(1, 1);
        Koordinaatti[] oksitut = jps.oksi(solmu, vanhempi);
        Koordinaatti[] odotetut = {
            new Koordinaatti(0, 0),
            new Koordinaatti(0, 1),
            new Koordinaatti(0, 2),
            new Koordinaatti(1, 0),
            new Koordinaatti(1, 2),
            new Koordinaatti(2, 0),
            new Koordinaatti(2, 1),
            new Koordinaatti(2, 2)
        };
        assertArrayEquals(odotetut, oksitut);
        assertEquals(false, jps.pakotettujaNaapureita(solmu, new Koordinaatti(0, 0)));
        assertEquals(false, jps.pakotettujaNaapureita(solmu, null));
    }

    @Test
    public void oksiVaakasuoranLuonnollisetNaapuritMukana() {
        Koordinaatti vanhempi = new Koordinaatti(0, 0);
        Koordinaatti solmu = new Koordinaatti(1, 0);
        Koordinaatti[] oksitut = jps.oksi(solmu, vanhempi);
        Koordinaatti[] odotetut = {
            null,
            null,
            null,
            null,
            null,
            null,
            new Koordinaatti(2, 0),
            null
        };
        assertArrayEquals(odotetut, oksitut);
        assertEquals(false, jps.pakotettujaNaapureita(solmu, new Koordinaatti(1, 0)));
    }

    @Test
    public void oksiPystysuoranLuonnollisetNaapuritMukana() {
        Koordinaatti vanhempi = new Koordinaatti(0, 0);
        Koordinaatti solmu = new Koordinaatti(0, 1);
        Koordinaatti[] oksitut = jps.oksi(solmu, vanhempi);
        Koordinaatti[] odotetut = {
            null,
            null,
            null,
            null,
            new Koordinaatti(0, 2),
            null,
            null,
            null
        };
        assertArrayEquals(odotetut, oksitut);
        assertEquals(false, jps.pakotettujaNaapureita(solmu, new Koordinaatti(0, 1)));
    }

    @Test
    public void oksiDiagonaalinLuonnollisetNaapuritMukana() {
        Koordinaatti vanhempi = new Koordinaatti(0, 0);
        Koordinaatti solmu = new Koordinaatti(1, 1);
        Koordinaatti[] oksitut = jps.oksi(solmu, vanhempi);
        Koordinaatti[] odotetut = {
            null,
            null,
            null,
            null,
            new Koordinaatti(1, 2),
            null,
            new Koordinaatti(2, 1),
            new Koordinaatti(2, 2)
        };
        assertArrayEquals(odotetut, oksitut);
        assertEquals(false, jps.pakotettujaNaapureita(solmu, new Koordinaatti(1, 1)));
    }

    @Test
    public void oksiKaanteisDiagonaalinLuonnollisetNaapuritMukana() {
        Koordinaatti vanhempi = new Koordinaatti(2, 2);
        Koordinaatti solmu = new Koordinaatti(1, 1);
        Koordinaatti[] oksitut = jps.oksi(solmu, vanhempi);
        Koordinaatti[] odotetut = {
            new Koordinaatti(0, 0),
            new Koordinaatti(0, 1),
            null,
            new Koordinaatti(1, 0),
            null,
            null,
            null,
            null
        };
        assertArrayEquals(odotetut, oksitut);
        assertEquals(false, jps.pakotettujaNaapureita(solmu, new Koordinaatti(-1, -1)));
    }

    @Test
    public void oksiVaakasuoranPakotteetMukana() {
        Koordinaatti vanhempi = new Koordinaatti(0, 1);
        Koordinaatti solmu = new Koordinaatti(1, 1);
        ruudukko.asetaEste(true, 1, 0);
        ruudukko.asetaEste(true, 1, 2);
        Koordinaatti[] oksitut = jps.oksi(solmu, vanhempi);
        Koordinaatti[] odotetut = {
            null,
            null,
            null,
            null,
            null,
            new Koordinaatti(2, 0),
            new Koordinaatti(2, 1),
            new Koordinaatti(2, 2)
        };
        assertArrayEquals(odotetut, oksitut);
        assertEquals(true, jps.pakotettujaNaapureita(solmu, new Koordinaatti(1, 0)));
    }

    @Test
    public void oksiPystysuoranPakotteetMukana() {
        Koordinaatti vanhempi = new Koordinaatti(1, 0);
        Koordinaatti solmu = new Koordinaatti(1, 1);
        ruudukko.asetaEste(true, 0, 1);
        ruudukko.asetaEste(true, 2, 1);
        Koordinaatti[] oksitut = jps.oksi(solmu, vanhempi);
        Koordinaatti[] odotetut = {
            null,
            null,
            new Koordinaatti(0, 2),
            null,
            new Koordinaatti(1, 2),
            null,
            null,
            new Koordinaatti(2, 2)
        };
        assertArrayEquals(odotetut, oksitut);
        assertEquals(true, jps.pakotettujaNaapureita(solmu, new Koordinaatti(0, 1)));
    }

    @Test
    public void oksiDiagonaalinPakotteetMukana() {
        Koordinaatti vanhempi = new Koordinaatti(0, 0);
        Koordinaatti solmu = new Koordinaatti(1, 1);
        ruudukko.asetaEste(true, 1, 0);
        ruudukko.asetaEste(true, 0, 1);
        Koordinaatti[] oksitut = jps.oksi(solmu, vanhempi);
        Koordinaatti[] odotetut = {
            null,
            null,
            new Koordinaatti(0, 2),
            null,
            new Koordinaatti(1, 2),
            new Koordinaatti(2, 0),
            new Koordinaatti(2, 1),
            new Koordinaatti(2, 2)
        };
        assertArrayEquals(odotetut, oksitut);
        assertEquals(true, jps.pakotettujaNaapureita(solmu, new Koordinaatti(1, 1)));
    }

    @Test
    public void oksiDiagonaalinPakotteetMukana2() {
        Koordinaatti vanhempi = new Koordinaatti(0, 0);
        Koordinaatti solmu = new Koordinaatti(1, 1);
        ruudukko.asetaEste(true, 0, 1);
        Koordinaatti[] oksitut = jps.oksi(solmu, vanhempi);
        Koordinaatti[] odotetut = {
            null,
            null,
            new Koordinaatti(0, 2),
            null,
            new Koordinaatti(1, 2),
            null,
            new Koordinaatti(2, 1),
            new Koordinaatti(2, 2)
        };
        assertArrayEquals(odotetut, oksitut);
        assertEquals(true, jps.pakotettujaNaapureita(solmu, new Koordinaatti(1, 1)));
    }

    @Test
    public void oksiDiagonaalinPakotteetMukana3() {
        Koordinaatti vanhempi = new Koordinaatti(0, 0);
        Koordinaatti solmu = new Koordinaatti(1, 1);
        ruudukko.asetaEste(true, 1, 0);
        Koordinaatti[] oksitut = jps.oksi(solmu, vanhempi);
        Koordinaatti[] odotetut = {
            null,
            null,
            null,
            null,
            new Koordinaatti(1, 2),
            new Koordinaatti(2, 0),
            new Koordinaatti(2, 1),
            new Koordinaatti(2, 2)
        };
        assertArrayEquals(odotetut, oksitut);
        assertEquals(true, jps.pakotettujaNaapureita(solmu, new Koordinaatti(1, 1)));
    }

    @Test
    public void oksiKaanteisDiagonaalinPakotteetMukana() {
        Koordinaatti vanhempi = new Koordinaatti(2, 2);
        Koordinaatti solmu = new Koordinaatti(1, 1);
        ruudukko.asetaEste(true, 1, 2);
        ruudukko.asetaEste(true, 2, 1);
        Koordinaatti[] oksitut = jps.oksi(solmu, vanhempi);
        Koordinaatti[] odotetut = {
            new Koordinaatti(0, 0),
            new Koordinaatti(0, 1),
            new Koordinaatti(0, 2),
            new Koordinaatti(1, 0),
            null,
            new Koordinaatti(2, 0),
            null,
            null
        };
        assertArrayEquals(odotetut, oksitut);
        assertEquals(true, jps.pakotettujaNaapureita(solmu, new Koordinaatti(-1, -1)));
    }

    @Test
    public void oksiKaanteisDiagonaalinPakotteetMukana2() {
        Koordinaatti vanhempi = new Koordinaatti(2, 2);
        Koordinaatti solmu = new Koordinaatti(1, 1);
        ruudukko.asetaEste(true, 2, 1);
        Koordinaatti[] oksitut = jps.oksi(solmu, vanhempi);
        Koordinaatti[] odotetut = {
            new Koordinaatti(0, 0),
            new Koordinaatti(0, 1),
            null,
            new Koordinaatti(1, 0),
            null,
            new Koordinaatti(2, 0),
            null,
            null
        };
        assertArrayEquals(odotetut, oksitut);
        assertEquals(true, jps.pakotettujaNaapureita(solmu, new Koordinaatti(-1, -1)));
    }

    @Test
    public void oksiKaanteisDiagonaalinPakotteetMukana3() {
        Koordinaatti vanhempi = new Koordinaatti(2, 2);
        Koordinaatti solmu = new Koordinaatti(1, 1);
        ruudukko.asetaEste(true, 1, 2);
        Koordinaatti[] oksitut = jps.oksi(solmu, vanhempi);
        Koordinaatti[] odotetut = {
            new Koordinaatti(0, 0),
            new Koordinaatti(0, 1),
            new Koordinaatti(0, 2),
            new Koordinaatti(1, 0),
            null,
            null,
            null,
            null
        };
        assertArrayEquals(odotetut, oksitut);
        assertEquals(true, jps.pakotettujaNaapureita(solmu, new Koordinaatti(-1, -1)));
    }
}
