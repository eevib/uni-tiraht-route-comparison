
package org.meklu.routecomparison.domain;

import java.util.Objects;

public class Koordinaatti {
    private Pari<Integer, Integer> pari;

    public Koordinaatti(int a, int b) {
        this.pari = new Pari<>(a, b);
    }

    public int getX() {
        return this.pari.getA();
    }

    public void setX(int x) {
        this.pari.setA(x);
    }

    public int getY() {
        return this.pari.getB();
    }

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

    @Override
    public String toString() {
        return "(" + pari.getA() + "," + pari.getB() + ")";
    }
}
