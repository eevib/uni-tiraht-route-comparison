
import org.meklu.routecomparison.domain.Pari;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PariTest {
    private static final int A = 3;
    private static final int B = 4;
    private Pari<Integer, Integer> pari;

    @Before
    public void setUp() {
        pari = new Pari<>(A, B);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void komponentitAlustuvatOikein() {
        assertEquals(A, (int) pari.getA());
        assertEquals(B, (int) pari.getB());
    }

    @Test
    public void komponentitAsettuvatOikein() {
        int a = 5;
        pari.setA(a);
        assertEquals(a, (int) pari.getA());
        assertEquals(B, (int) pari.getB());
        int b = 7;
        pari.setB(b);
        assertEquals(a, (int) pari.getA());
        assertEquals(b, (int) pari.getB());
    }

    @Test
    public void merkkijonoEsitysToimii() {
        assertEquals("Pari{" + A + "," + B + "}", pari.toString());
    }

    @Test
    public void yhtalaisetYhtalaisia() {
        assertEquals(true, pari.equals(pari));
        Pari<Integer, Integer> toinen = new Pari(A, B);
        assertEquals(true, pari.equals(toinen));
    }

    @Test
    public void eriavatEriavia() {
        Pari<Integer, Integer> toinen = new Pari(A, B + 1);
        assertEquals(false, pari.equals(toinen));
        toinen.setA(A + 1);
        assertEquals(false, pari.equals(toinen));
        toinen.setB(B);
        assertEquals(false, pari.equals(toinen));
    }

    @Test
    public void nullitEriavia() {
        assertEquals(false, pari.equals(null));
    }

    @Test
    public void eriluokkaisetEriavia() {
        assertEquals(false, pari.equals(new Integer(3)));
    }
}
