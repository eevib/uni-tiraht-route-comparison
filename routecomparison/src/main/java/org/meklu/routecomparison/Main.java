
package org.meklu.routecomparison;

import java.util.LinkedList;
import org.meklu.routecomparison.domain.*;
import org.meklu.routecomparison.util.*;

public class Main {
    private static long benchInner(int toistot, Reitinhakija rh, Koordinaatti lahto, Koordinaatti maali) {
        toistot = Math.max(toistot, 1);
        long yhteisaika = 0L;
        for (int i = 0; i < toistot; ++i) {
            rh.etsiReitti(lahto.getX(), lahto.getY(), maali.getX(), maali.getY());
            yhteisaika += rh.getDiagnostiikka().getSuoritusaikaNs();
        }
        return yhteisaika;
    }
    private static double nsToMs(long ns) {
        return (double) ns / 1000000.0;
    }
    private static void vertailuTuloste(Reitinhakija rhA, long suoritusaikaA, Reitinhakija rhB, long suoritusaikaB) {
        Reitinhakija nopeampi;
        long nopeampiAika;
        Reitinhakija hitaampi;
        long hitaampiAika;
        if (suoritusaikaA < suoritusaikaB) {
            nopeampi = rhA;
            nopeampiAika = suoritusaikaA;
            hitaampi = rhB;
            hitaampiAika = suoritusaikaB;
        } else {
            hitaampi = rhA;
            hitaampiAika = suoritusaikaA;
            nopeampi = rhB;
            nopeampiAika = suoritusaikaB;
        }
        if (suoritusaikaA == suoritusaikaB) {
            System.out.println(rhA.nimi() + " ja " + rhB.nimi() + " olivat yhtä nopeita");
        }
        System.out.println(nopeampi.nimi() + " oli " + ((double) hitaampiAika / (double) nopeampiAika) + " kertaa niin nopea kuin " + hitaampi.nimi());
    }
    public static void main(String[] args) throws Exception {
        System.out.print("Nykyinen hakemisto: ");
        System.out.println(new java.io.File(".").getCanonicalPath());

        if (0 == args.length) {
            args = new String[] { "./thrc/plort.thrc", "-random", "-bench" };
        }

        int benchN = -1;
        boolean bench = false;
        boolean randomBench = false;
        LinkedList<String> oikeatTiedostot = new LinkedList<>();
        for (String tiedostoNimi : args) {
            if (!tiedostoNimi.startsWith("-")) {
                oikeatTiedostot.add(tiedostoNimi);
            } else if (tiedostoNimi.startsWith("-random")) {
                randomBench = true;
            } else if (tiedostoNimi.startsWith("-bench")) {
                bench = true;
                try {
                    benchN = Integer.parseInt(tiedostoNimi.substring("-bench".length()));
                } catch (Exception e) {
                }
            }
        }

        Heuristiikka heuristiikka = new Heuristiikka();
        Reitinhakija[] reitinhakijat = {
            new AStar(heuristiikka),
            new JPS(heuristiikka)
        };
        if (bench) {
            System.out.println("Suoritetaan suorituskykyvertailua");
            System.out.println();
            final int N = benchN <= 0 ? 100 : benchN;
            final int LEVEYS = 512;
            final int KORKEUS = 256;
            Ruudukko ruudukko;
            SatunnaisRuudukko satunnaisruudukko = new SatunnaisRuudukko(LEVEYS, KORKEUS);
            Koordinaatti[] tayttoAsteet = {
                new Koordinaatti(0, 1),
                new Koordinaatti(1, 32),
                new Koordinaatti(1, 16),
                new Koordinaatti(1, 8),
                new Koordinaatti(2, 8),
                new Koordinaatti(3, 8)
            };
            if (!randomBench) {
                tayttoAsteet = new Koordinaatti[0];
            }
            for (String oikeaTiedosto : oikeatTiedostot) {
                ruudukko = new ASCIILukija(new java.util.Scanner(new java.io.File(oikeaTiedosto))).lue();
                if (null == ruudukko) {
                    continue;
                }
                System.out.println("n = " + N + ", koko = " + ruudukko.getLeveys() + "x" + ruudukko.getKorkeus() + ", tiedosto = `" + oikeaTiedosto + "'");
                Koordinaatti lahto = new Koordinaatti(0, 0);
                Koordinaatti maali = new Koordinaatti(ruudukko.getLeveys() - 1, ruudukko.getKorkeus() - 1);
                long[] suoritusajat = new long[reitinhakijat.length];
                for (int i = 0; i < reitinhakijat.length; ++i) {
                    Reitinhakija rh = reitinhakijat[i];
                    rh.asetaRuudukko(ruudukko);
                    suoritusajat[i] = benchInner(N, rh, lahto, maali);
                    System.out.println("   " + rh.nimi() + ": yht. " + nsToMs(suoritusajat[i]) + " ms, ka " + nsToMs(suoritusajat[i] / (long) N) + " ms");
                }
                System.out.print("   ");
                vertailuTuloste(reitinhakijat[0], suoritusajat[0], reitinhakijat[1], suoritusajat[1]);
            }
            for (Koordinaatti tayttoAste : tayttoAsteet) {
                System.out.println("n = " + N + ", koko = " + LEVEYS + "x" + KORKEUS + ", satunnaisruudukon täyttöaste = " + tayttoAste.getX() + "/" + tayttoAste.getY());
                do {
                    ruudukko = satunnaisruudukko.generoi(tayttoAste.getX(), tayttoAste.getY());
                    reitinhakijat[0].asetaRuudukko(ruudukko);
                } while (null == reitinhakijat[0].etsiReitti(0, 0, LEVEYS - 1, KORKEUS - 1));
                Koordinaatti lahto = new Koordinaatti(0, 0);
                Koordinaatti maali = new Koordinaatti(LEVEYS - 1, KORKEUS - 1);
                long[] suoritusajat = new long[reitinhakijat.length];
                for (int i = 0; i < reitinhakijat.length; ++i) {
                    Reitinhakija rh = reitinhakijat[i];
                    rh.asetaRuudukko(ruudukko);
                    suoritusajat[i] = benchInner(N, rh, lahto, maali);
                    System.out.println("   " + rh.nimi() + ": yht. " + nsToMs(suoritusajat[i]) + " ms, ka " + nsToMs(suoritusajat[i] / (long) N) + " ms");
                }
                System.out.print("   ");
                vertailuTuloste(reitinhakijat[0], suoritusajat[0], reitinhakijat[1], suoritusajat[1]);
            }
            return;
        }

        for (String tiedostoNimi : args) {
            ASCIILukija lukija;
            SatunnaisRuudukko satunnaisruudukko;
            Ruudukko ruudukko = null;
            boolean satunnainen = false;
            try {
                if (tiedostoNimi.startsWith("-random")) {
                    satunnaisruudukko = new SatunnaisRuudukko(32, 16);
                    ruudukko = satunnaisruudukko.generoi(1, 16);
                    satunnainen = true;
                } else if (tiedostoNimi.startsWith("-")) {
                    // nop
                } else {
                    lukija = new ASCIILukija(new java.util.Scanner(new java.io.File(tiedostoNimi)));
                    ruudukko = lukija.lue();
                }
            } catch (Exception e) {
            }
            if (null == ruudukko) {
                if (satunnainen) {
                    System.out.println("Satunnaista ruudukkoa luotaessa sattui virhe");
                } else {
                    System.out.println("Lukiessa ruudukkoa tiedostosta `" + tiedostoNimi + "' sattui virhe.");
                }
                System.out.println();
                continue;
            } else {
                if (satunnainen) {
                    System.out.println("Luotiin satunnaisruudukko");
                } else {
                    System.out.println("Luettiin ruudukko tiedostosta `" + tiedostoNimi + "'.");
                }
                System.out.println();
            }

            String[] otsikot = {
                "=== A*  ===",
                "=== JPS ==="
            };
            long[] suoritusajat = new long[reitinhakijat.length];
            for (int i = 0; i < reitinhakijat.length; ++i) {
                String otsikko = otsikot[i];
                Reitinhakija rh = reitinhakijat[i];
                rh.asetaRuudukko(ruudukko);
                System.out.println(otsikko);
                long t = benchInner(1, rh, new Koordinaatti(0, 0), new Koordinaatti(ruudukko.getLeveys() - 1, ruudukko.getKorkeus() - 1));
                suoritusajat[i] = t;
                System.out.println(rh.getDiagnostiikka());
                rh.getDiagnostiikka().getReitintutkija().tulostaTekstina();
            }
            System.out.println("=== TULOS ===");
            vertailuTuloste(reitinhakijat[0], suoritusajat[0], reitinhakijat[1], suoritusajat[1]);
            System.out.println("");
        }
    }
}
