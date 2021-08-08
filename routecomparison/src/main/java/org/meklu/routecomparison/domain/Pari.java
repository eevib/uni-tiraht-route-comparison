
package org.meklu.routecomparison.domain;

import java.util.Objects;

/** Yksinkertainen yleisluokka pareille
 *
 * @param <A> Parin a-komponentin tyyppi
 * @param <B> Parin b-komponentin tyyppi
 */
public class Pari<A, B> {
    A a;
    B b;

    /** Luo parin
     *
     * @param a Annettava a-komponentti
     * @param b Annettava b-komponentti
     */
    public Pari(A a, B b) {
        this.a = a;
        this.b = b;
    }

    /** Palauttaa a-komponentin
     *
     * @return Parin a-komponentti
     */
    public A getA() {
        return a;
    }

    /** Asettaa a-komponentin
     *
     * @param a Uusi a-komponentti tälle Parille
     */
    public void setA(A a) {
        this.a = a;
    }

    /** Palauttaa b-komponentin
     *
     * @return Parin b-komponentti
     */
    public B getB() {
        return b;
    }

    /** Asettaa b-komponentin
     *
     * @param b Uusi b-komponentti tälle Parille
     */
    public void setB(B b) {
        this.b = b;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.a);
        hash = 61 * hash + Objects.hashCode(this.b);
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
        final Pari<?, ?> other = (Pari<?, ?>) obj;
        if (!Objects.equals(this.a, other.a)) {
            return false;
        }
        if (!Objects.equals(this.b, other.b)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Pari{" + a + "," + b + "}";
    }
}
