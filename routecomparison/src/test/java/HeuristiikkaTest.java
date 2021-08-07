
import org.meklu.routecomparison.domain.Heuristiikka;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HeuristiikkaTest {
    private Heuristiikka heuristiikka;

    @Before
    public void setUp() {
        this.heuristiikka = new Heuristiikka();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void pieniYhteisetaisyysOnOikein() {
        assertEquals(6.2426, heuristiikka.lyhinMahdollinenEtaisyys(0, 0, 5, 3), 0.0001);
    }

    @Test
    public void nollaErotusTuottaaNollan() {
        assertEquals(0.0000, heuristiikka.lyhinMahdollinenEtaisyys(5, 3, 5, 3), 0.0001);
    }

    @Test
    public void pelkkaKaanteisDiagonaaliOikein() {
        assertEquals(4.2426, heuristiikka.lyhinMahdollinenEtaisyys(3, 3, 0, 0), 0.0001);
    }

    @Test
    public void pelkkaKaanteisSuoraOikein() {
        assertEquals(5.0000, heuristiikka.lyhinMahdollinenEtaisyys(0, 5, 0, 0), 0.0001);
    }
}
