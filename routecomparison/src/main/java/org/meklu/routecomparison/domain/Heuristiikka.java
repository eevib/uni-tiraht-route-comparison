
package org.meklu.routecomparison.domain;

/** Toteuttaa oktiiliheuristiikan
 *
 * Ruudukossa voi liikkua kahdeksaan eri suuntaan, joista puolet ovat
 * diagonaalisia.
 *
 * Koska pidämme sivuttais- tai pystysuunnan siirron painona lukua 1,
 * diagonaalisen siirron paino on noin sqrt(2), koska Pythagoraan lauseen mukaan
 * hypotenuusa c mittaantuu kaneetteihin a ja b (tässä tapauksessa molempien
 * arvo on 1) seuraavasti:
 *    c^2 = a^2 + b^2
 * eli:
 *    c = sqrt(a^2 + b^2)
 *    c = sqrt(1^2 + b^2)
 *    c = sqrt(1 + 1)
 *    c = sqrt(2)
 *
 * Koska emme voi liikkua mielivaltaisesti ruudukossa, emme voi käyttää suoraan
 * pisteiden A ja B geometrista etäisyyttä kaikista osuvimpana heuristiikkana.
 *
 * Lyhin mahdollinen etäisyys pisteiden A ja B välillä koostuu siis korkeintaan
 * yhdestä diagonaalisesta ja yhdestä suorasta siirtokokonaisuudesta. Alla
 * esimerkki läpikäydyistä ruuduista etäisyydellä (dx, dy) = (5,3), polkuna
 * A-2-3-4-5-B.
 *
 *   _0_1_2_3_4_5_
 * 0| A - - - - - |
 * 1| - 2 - - - - |
 * 2| - - 3 - - - |
 * 3| - - - 4 5 B |
 *
 * Esimerkissä laskemme siis yhteen geometrisen etäisyyden A-4 ja yksiulotteisen
 * etäisyyden 4-B.
 *
 * Yksiulotteinen etäisyys 4-B voidaan laskea ottamalla x- ja y-koordinaattien
 * erotuksen absoluuttinen arvo, eli abs(dx - dy), eli abs(5 - 3) = 2.
 *
 * Geometrinen etäisyys A-4 voidaan laskea ottamalla x- ja y-koordinaattien
 * erotuksista pienempi, eli min(5, 3) = 3 ja sijoittamalla se Pythagoraan
 * lauseen muuttujiin a ja b.
 *    c = sqrt(3^2 + 3^2)
 *    c = sqrt(9 + 9)
 *    c = sqrt(18)
 *    c = 4.2426...
 *
 * Lopullinen oktiilinen minimietäisyys näiden kahden pisteen välillä on siis
 * edellisten kohtien perusteella seuraava:
 *    dmin = min(dx, dy)
 *    d_o = abs(dx - dy) + sqrt(dmin^2 + dmin^2)
 *    d_o = 2 + sqrt(18)
 *    d_o = 6.2426...
 *
 * Vertailuksi annettakoon myös suora geometrinen etäisyys:
 *    d_g = sqrt(dx^2 + dy^2)
 *    d_g = sqrt(5^2 + 3^2)
 *    d_g = sqrt(25 + 9)
 *    d_g = sqrt(34)
 *    d_g = 5.8309...
 *
 * Annetaan vielä myös ns. taksimiehen etäisyys, jos ruudukossa voisi liikkua
 * vain sivuttais- ja pystysuunnassa:
 *    d_t = dx + dy
 *    d_t = 5 + 3
 *    d_t = 8
 */
public class Heuristiikka {
    public double lyhinMahdollinenEtaisyys(int ax, int ay, int bx, int by) {
        int dx = Math.abs(bx - ax);
        int dy = Math.abs(by - ay);
        int dsuora = Math.abs(dx - dy);
        int dmin = Math.min(dx, dy);
        double ddiag = Math.sqrt(dmin * dmin + dmin * dmin);
        return dsuora + ddiag;
    }
}
