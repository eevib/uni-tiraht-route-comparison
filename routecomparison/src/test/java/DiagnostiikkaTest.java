
import org.meklu.routecomparison.domain.Diagnostiikka;
import org.meklu.routecomparison.domain.Koordinaatti;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiagnostiikkaTest {
    private Diagnostiikka diagnostiikka;

    @Before
    public void setUp() {
        this.diagnostiikka = new Diagnostiikka(null);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void ajanottoPositiivista() {
        this.diagnostiikka.aloitaSuoritus();
        this.diagnostiikka.paataSuoritus();
        assertTrue(this.diagnostiikka.getSuoritusaikaNs() > 0);
    }

    @Test
    public void ajanottoEiMeneMiinukselle() {
        this.diagnostiikka.paataSuoritus();
        this.diagnostiikka.aloitaSuoritus();
        assertEquals(0, this.diagnostiikka.getSuoritusaikaNs());
    }

    @Test
    public void ajanottoVaiheittainToimii() {
        this.diagnostiikka.aloitaSuoritus();
        this.diagnostiikka.paataSuoritus();
        long vanha = this.diagnostiikka.getSuoritusaikaNs();
        this.diagnostiikka.aloitaSuoritus();
        this.diagnostiikka.paataSuoritus();
        assertTrue(this.diagnostiikka.getSuoritusaikaNs() > vanha);
    }

    @Test
    public void syklejaAluksiNolla() {
        assertEquals(0, this.diagnostiikka.getSyklejaSuoritettu());
    }

    @Test
    public void syklitLisaantyvat() {
        this.diagnostiikka.suoritaSykli();
        assertEquals(1, this.diagnostiikka.getSyklejaSuoritettu());
        for (int i = 0; i < 10; ++i) {
            this.diagnostiikka.suoritaSykli();
        }
        assertEquals(11, this.diagnostiikka.getSyklejaSuoritettu());
    }

    @Test
    public void ruutujaAluksiNolla() {
        assertEquals(0, this.diagnostiikka.getSyklejaSuoritettu());
    }

    @Test
    public void ruudutLisaantyvat() {
        Koordinaatti k = new Koordinaatti(0, 0);
        this.diagnostiikka.kayRuudussa(k);
        assertEquals(1, this.diagnostiikka.getRuuduissaKayty());
        for (int i = 0; i < 10; ++i) {
            this.diagnostiikka.kayRuudussa(k);
        }
        assertEquals(11, this.diagnostiikka.getRuuduissaKayty());
    }
}
