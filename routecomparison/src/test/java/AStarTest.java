
import org.meklu.routecomparison.domain.*;
import org.meklu.routecomparison.util.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AStarTest {
    private Ruudukko ruudukko;
    private Heuristiikka heuristiikka;
    private AStar astar;

    @Before
    public void setUp() throws Exception {
        this.ruudukko = new Ruudukko(6, 4);
        this.heuristiikka = new Heuristiikka();
        this.astar = new AStar(this.ruudukko, this.heuristiikka);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void tyhjanRuudukonReittiOptimimittainen() {
        Koordinaatti[] reitti = astar.etsiReitti(0, 0, this.ruudukko.getLeveys() - 1, this.ruudukko.getKorkeus() - 1);
        assertNotNull(reitti);
        assertEquals(6, reitti.length);
        assertEquals(true, new Koordinaatti(0, 0).equals(reitti[0]));
        assertEquals(true, new Koordinaatti(this.ruudukko.getLeveys() - 1, this.ruudukko.getKorkeus() - 1).equals(reitti[reitti.length - 1]));
        Reitintutkija rt = new Reitintutkija(ruudukko, reitti);
        assertEquals(rt.getReitinPituus(), heuristiikka.lyhinMahdollinenEtaisyys(0, 0, this.ruudukko.getLeveys() - 1, this.ruudukko.getKorkeus() - 1), 0.0001);
        assertEquals(false, rt.onkoReitissaOngelmia());
    }

    @Test
    public void nullJosRuudukonUlkopuolinenLahtopiste() {
        Koordinaatti[] reitti = astar.etsiReitti(-1, -1, this.ruudukko.getLeveys() - 1, this.ruudukko.getKorkeus() - 1);
        assertNull(reitti);
    }

    @Test
    public void nullJosRuudukonUlkopuolinenMaalipiste() {
        Koordinaatti[] reitti = astar.etsiReitti(0, 0, this.ruudukko.getLeveys(), this.ruudukko.getKorkeus());
        assertNull(reitti);
    }

    @Test
    public void yksinkertaisenRuudukonReittiOnOptimi() {
        /*
          | @ * - - - - |
          | @ * - - - - |
          | @ * - - - - |
          | - @ @ @ @ @ |
          |Reitin pituus on 7.414213562373095, solmuja 8.
        */
        for (int y = 0; y < ruudukko.getKorkeus() - 1; ++y) {
            ruudukko.asetaEste(true, 1, y);
        }
        Koordinaatti[] reitti = astar.etsiReitti(0, 0, this.ruudukko.getLeveys() - 1, this.ruudukko.getKorkeus() - 1);
        assertNotNull(reitti);
        assertEquals(true, new Koordinaatti(0, 0).equals(reitti[0]));
        assertEquals(true, new Koordinaatti(this.ruudukko.getLeveys() - 1, this.ruudukko.getKorkeus() - 1).equals(reitti[reitti.length - 1]));
        Reitintutkija rt = new Reitintutkija(ruudukko, reitti);
        assertEquals(7.4142, rt.getReitinPituus(), 0.0001);
        assertEquals(8, rt.getReitissaSolmuja());
        assertEquals(false, rt.onkoReitissaOngelmia());
    }

    @Test
    public void hienommanRuudukonReittiOnOptimi() {
        /*
          | @ * - @ - - |
          | @ * @ * @ - |
          | @ * @ * @ - |
          | - @ - * - @ |
          |Reitin pituus on 11.071067811865476, solmuja 10.
        */
        for (int y = 0; y < ruudukko.getKorkeus() - 1; ++y) {
            ruudukko.asetaEste(true, 1, y);
        }
        for (int y = 1; y < ruudukko.getKorkeus(); ++y) {
            ruudukko.asetaEste(true, 3, y);
        }
        Koordinaatti[] reitti = astar.etsiReitti(0, 0, this.ruudukko.getLeveys() - 1, this.ruudukko.getKorkeus() - 1);
        assertNotNull(reitti);
        assertEquals(true, new Koordinaatti(0, 0).equals(reitti[0]));
        assertEquals(true, new Koordinaatti(this.ruudukko.getLeveys() - 1, this.ruudukko.getKorkeus() - 1).equals(reitti[reitti.length - 1]));
        Reitintutkija rt = new Reitintutkija(ruudukko, reitti);
        assertEquals(11.0710, rt.getReitinPituus(), 0.0001);
        assertEquals(10, rt.getReitissaSolmuja());
        assertEquals(false, rt.onkoReitissaOngelmia());
    }

}
