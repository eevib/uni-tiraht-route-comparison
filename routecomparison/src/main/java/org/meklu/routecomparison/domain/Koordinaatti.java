
package org.meklu.routecomparison.domain;

import java.util.Objects;

/** Kaksiulotteinen kokonaislukukoordinaatti.
 *
 * Kätevä ruudukon kanssa leikkiessä. Pohjautuu luokkaan Pari, mutta antaa
 * kätevämmät nimet komponenteilleen, x ja y.
 */
public class Koordinaatti {
    private Pari<Integer, Integer> pari;

    /** Luo uuden koordinaatin
     *
     * @param x Tämän olion x-koordinaatti
     * @param y Tämän olion y-koordinaatti
     */
    public Koordinaatti(int x, int y) {
        this.pari = new Pari<>(x, y);
    }

    /** Palauttaa x-koordinaatin
     *
     * @return Tämän olion x-koordinaatti
     */
    public int getX() {
        return this.pari.getA();
    }

    /** Asettaa x-koordinaatin
     *
     * @param x Tämän olion uusi x-koordinaatti
     */
    public void setX(int x) {
        this.pari.setA(x);
    }

    /** Palauttaa y-koordinaatin
     *
     * @return Tämän olion y-koordinaatti
     */
    public int getY() {
        return this.pari.getB();
    }

    /** Asettaa y-koordinaatin
     *
     * @param y Tämän olion uusi y-koordinaatti
     */
    public void setY(int y) {
        this.pari.setB(y);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + Objects.hashCode(this.pari);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Koordinaatti other = (Koordinaatti) obj;
        if (!Objects.equals(this.pari, other.pari)) {
            return false;
        }
        return true;
    }

    /** Palauttaa koordinaatin merkkijonona muodossa "(x,y)".
     *
     * @return Koordinaatin merkkijonoesitys
     */
    @Override
    public String toString() {
        return "(" + pari.getA() + "," + pari.getB() + ")";
    }
}
