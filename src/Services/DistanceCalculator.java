package Services;

import java.util.Arrays;

public class DistanceCalculator {
    public static int calculate(String fingerPrint1, String fingerPrint2) {
        int[][]distancesTable = new int[fingerPrint1.length() + 1][fingerPrint2.length() + 1];

        for (int i = 0; i <= fingerPrint1.length(); i++) {
            for (int j = 0; j <= fingerPrint2.length(); j++) {
                if (i == 0) {
                    distancesTable[i][j]= j;
                }
                else if (j == 0) {
                    distancesTable[i][j]= i;
                }
                else {
                    distancesTable[i][j]= min(distancesTable[i - 1][j - 1]
                                    + costOfSubstitution(fingerPrint1.charAt(i - 1), fingerPrint2.charAt(j - 1)),
                            distancesTable[i - 1][j]+ 1,
                            distancesTable[i][j - 1]+ 1);
                }
            }
        }

        return distancesTable[fingerPrint1.length()][fingerPrint2.length()];
    }

    public static int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }

    public static int min(int... numbers) {
        return Arrays.stream(numbers)
                .min().orElse(Integer.MAX_VALUE);
    }

}
