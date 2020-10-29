package no.oslomet.cs.algdat.Eksamen;

import java.util.Comparator;

public class testing {
    public static void main(String[] args) {
        int antallFeil = 0;

        EksamenSBinTre<Integer> tre =
                new EksamenSBinTre<>(Comparator.naturalOrder());

        String s;

        int[] b = {9, 1, 2, 3, 4, 5, 6, 7, 8, 1,10 ,1, 1, 4, 1, 3, 1, 2, 1, 1};
        for (int verdi : b) tre.leggInn(verdi);
        System.out.println(tre.toStringPostOrder());
        tre.fjernAlle(1);
        System.out.println(tre.toStringPostOrder());
    }
}
