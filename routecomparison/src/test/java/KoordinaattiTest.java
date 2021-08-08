
import org.meklu.routecomparison.domain.Koordinaatti;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class KoordinaattiTest {
    Koordinaatti koordinaatti;

    @Before
    public void setUp() {
        this.koordinaatti = new Koordinaatti(111, 55);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void xKoordinaattiKunnossa() {
        assertEquals(111, this.koordinaatti.getX());
        this.koordinaatti.setX(22);
        assertEquals(22, this.koordinaatti.getX());
    }

    @Test
    public void yKoordinaattiKunnossa() {
        assertEquals(55, this.koordinaatti.getY());
        this.koordinaatti.setY(77);
        assertEquals(77, this.koordinaatti.getY());
    }

    @Test
    public void merkkijonoEsitysOnKelpo() {
        assertEquals("(" + this.koordinaatti.getX() + "," + this.koordinaatti.getY() + ")", this.koordinaatti.toString());
    }
}
