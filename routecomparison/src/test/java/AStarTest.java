
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
        Reitintutkija rt = new Reitintutkija(ruudukko, reitti);
        assertEquals(rt.getReitinPituus(), heuristiikka.lyhinMahdollinenEtaisyys(0, 0, this.ruudukko.getLeveys() - 1, this.ruudukko.getKorkeus() - 1), 0.0001);
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
}
