
import org.meklu.routecomparison.domain.Ruudukko;
import org.meklu.routecomparison.util.SatunnaisRuudukko;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SatunnaisRuudukkoTest {
    private static final int LEVEYS = 32;
    private static final int KORKEUS = 16;
    private SatunnaisRuudukko satunnaisruudukko;

    @Before
    public void setUp() {
        this.satunnaisruudukko = new SatunnaisRuudukko(LEVEYS, KORKEUS);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void ruudukotOikeanKokoisia() {
        Ruudukko ruudukko = this.satunnaisruudukko.generoi(0, 1);
        assertEquals(LEVEYS, ruudukko.getLeveys());
        assertEquals(KORKEUS, ruudukko.getKorkeus());
    }

    @Test
    public void nollatayttoinenRuudukkoOnTyhja() {
        Ruudukko ruudukko = this.satunnaisruudukko.generoi(0, 1);
        for (int y = 0; y < KORKEUS; ++y) {
            for (int x = 0; x < LEVEYS; ++x) {
                assertEquals(false, ruudukko.ruutuEstynyt(x, y));
            }
        }
    }

    @Test
    public void taystayttoinenRuudukkoPelkkiaEsteita() {
        Ruudukko ruudukko = this.satunnaisruudukko.generoi(1, 1);
        for (int y = 0; y < KORKEUS; ++y) {
            for (int x = 0; x < LEVEYS; ++x) {
                assertEquals(true, ruudukko.ruutuEstynyt(x, y));
            }
        }
    }
}
