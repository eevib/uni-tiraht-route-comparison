
package org.meklu.routecomparison;

import org.meklu.routecomparison.domain.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Ruudukko ruudukko = new Ruudukko(6, 4);
        Heuristiikka heuristiikka = new Heuristiikka();
        AStar astar = new AStar(ruudukko, heuristiikka);
        Koordinaatti[] reitti = astar.etsiReitti(0, 0, 5, 3);
        for (int i = 0; i < reitti.length; ++i) {
            System.out.println(i + ": " + reitti[i].toString());
        }
    }
}
