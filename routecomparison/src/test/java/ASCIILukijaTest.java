
import org.meklu.routecomparison.domain.Ruudukko;
import org.meklu.routecomparison.util.ASCIILukija;

import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ASCIILukijaTest {
    @Test
    public void nullTyhjalla() {
        String ascii = "";
        Ruudukko ruudukko = new ASCIILukija(new Scanner(ascii)).lue();
        assertEquals(null, ruudukko);
    }

    @Test
    public void nullIlmanOtsaketta() {
        String ascii = (
            "00000\n" +
            "00000\n" +
            "00000\n"
        );
        Ruudukko ruudukko = new ASCIILukija(new Scanner(ascii)).lue();
        assertEquals(null, ruudukko);
    }

    @Test
    public void nullVajaallaOtsakkeella() {
        String ascii = (
            "thrc 0\r\t\n" +
            "00000\n" +
            "00000\n" +
            "00000\n"
        );
        Ruudukko ruudukko = new ASCIILukija(new Scanner(ascii)).lue();
        assertEquals(null, ruudukko);
    }

    @Test
    public void nullTyhjallaOtsakkeella() {
        String ascii = (
            "thrc\n" +
            "00000\n" +
            "00000\n" +
            "00000\n"
        );
        Ruudukko ruudukko = new ASCIILukija(new Scanner(ascii)).lue();
        assertEquals(null, ruudukko);
    }

    @Test
    public void nullKehnollaOtsakkeella() {
        String ascii = (
            "Thrc01\n" +
            "00000\n" +
            "00000\n" +
            "00000\n"
        );
        Ruudukko ruudukko = new ASCIILukija(new Scanner(ascii)).lue();
        assertEquals(null, ruudukko);
    }

    @Test
    public void tyhjaRuudukkoLukeutuuOikein() {
        String ascii = (
            "thrc01\n" +
            "00000\n" +
            "00000\n" +
            "00000\n"
        );
        Ruudukko ruudukko = new ASCIILukija(new Scanner(ascii)).lue();
        assertEquals(5, ruudukko.getLeveys());
        assertEquals(3, ruudukko.getKorkeus());
        for (int y = 0; y < ruudukko.getKorkeus(); ++y) {
            for (int x = 0; x < ruudukko.getLeveys(); ++x) {
                assertEquals(false, ruudukko.ruutuEstynyt(x, y));
            }
        }
    }

    @Test
    public void taysiRuudukkoLukeutuuOikein() {
        String ascii = (
            "thrc\t1\t0\r\n" +
            "0 0 0 0\t0 asdfghjkl :D\r\n" +
            "0 0s0 0 0\n" +
            "0@0 0 0r0\n"
        );
        Ruudukko ruudukko = new ASCIILukija(new Scanner(ascii)).lue();
        assertEquals(5, ruudukko.getLeveys());
        assertEquals(3, ruudukko.getKorkeus());
        for (int y = 0; y < ruudukko.getKorkeus(); ++y) {
            for (int x = 0; x < ruudukko.getLeveys(); ++x) {
                assertEquals(true, ruudukko.ruutuEstynyt(x, y));
            }
        }
    }
    @Test
    public void yksinkertainenRuudukkoLukeutuuOikein() {
        String ascii = (
            "thrcrc\n" +
            "rcccr\n" +
            "crccr\n" +
            "ccrcr\n"
        );
        Ruudukko ruudukko = new ASCIILukija(new Scanner(ascii)).lue();
        assertEquals(5, ruudukko.getLeveys());
        assertEquals(3, ruudukko.getKorkeus());
        for (int y = 0; y < ruudukko.getKorkeus(); ++y) {
            for (int x = 0; x < ruudukko.getLeveys(); ++x) {
                if (x == y || x == ruudukko.getLeveys() - 1) {
                    assertEquals(false, ruudukko.ruutuEstynyt(x, y));
                } else {
                    assertEquals(true, ruudukko.ruutuEstynyt(x, y));
                }
            }
        }
    }
}
