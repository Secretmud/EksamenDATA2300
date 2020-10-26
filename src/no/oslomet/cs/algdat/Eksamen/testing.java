package no.oslomet.cs.algdat.Eksamen;

import java.util.Comparator;

public class testing {
    public static void main(String[] args) {
        int antallFeil = 0;

        EksamenSBinTre<Integer> tre =
                new EksamenSBinTre<>(Comparator.naturalOrder());

        String s;

        int[] a = {6, 3, 9, 1, 5, 7, 10, 2, 4, 8, 11, 6, 8};
        for (int verdi : a) tre.leggInn(verdi);
        boolean fjernet = tre.fjern(12);
        s = tre.toStringPostOrder();
        System.out.println(s);
        System.out.println(tre.antall());
        System.out.println(a.length);
    }
}
