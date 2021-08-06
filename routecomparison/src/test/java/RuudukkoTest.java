
import org.meklu.routecomparison.domain.Ruudukko;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RuudukkoTest {
    private static final int LEVEYS = 5;
    private static final int KORKEUS = 3;
    private Ruudukko ruudukko;

    @Before
    public void setUp() throws Exception {
        this.ruudukko = new Ruudukko(LEVEYS, KORKEUS);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void ruudukonKokoTasmaa() {
        assertEquals(LEVEYS, ruudukko.getLeveys());
        assertEquals(KORKEUS, ruudukko.getKorkeus());
    }

    @Test
    public void ruudukkoOnAluksiEsteeton() {
        for (int y = 0; y < KORKEUS; ++y) {
            for (int x = 0; x < LEVEYS; ++x) {
                assertEquals(false, ruudukko.ruutuEstynyt(x, y));
            }
        }
    }

    @Test
    public void ruudukonUlkopuolellaOlevatRuudutEsteellisia() {
        assertEquals(true, ruudukko.ruutuEstynyt(-1, -1));
        assertEquals(true, ruudukko.ruutuEstynyt(-1, KORKEUS));
        assertEquals(true, ruudukko.ruutuEstynyt(LEVEYS, -1));
        assertEquals(true, ruudukko.ruutuEstynyt(LEVEYS, KORKEUS));
    }

    @Test
    public void esteAsettuuOikein() {
        ruudukko.asetaEste(true, 0, 1);
        assertEquals(true, ruudukko.ruutuEstynyt(0, 1));
        assertEquals(false, ruudukko.ruutuEstynyt(1, 0));
    }

    @Test
    public void epakelpoaRuudukkoaEiLuoda() {
        try {
            ruudukko = new Ruudukko(0, 0);
            assertEquals(true, false);
        } catch (Exception e) {
            assertEquals(true, true);
        }
    }

    @Test
    public void epakelpoEsteEiKoskeRuudukkoon() {
        ruudukko.asetaEste(true, -1, -1);
        ruudukko.asetaEste(true, -1, KORKEUS);
        ruudukko.asetaEste(true, LEVEYS, -1);
        ruudukko.asetaEste(true, LEVEYS, KORKEUS);

        for (int y = 0; y < KORKEUS; ++y) {
            for (int x = 0; x < LEVEYS; ++x) {
                assertEquals(false, ruudukko.ruutuEstynyt(x, y));
            }
        }
    }
}
