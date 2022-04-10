package lab;


import java.util.ArrayList;

import frame.SortArray;

public class HybridOptimizer {

    /**
     * Find the optimal value k of the HybridSort algorithm for
     * the given data. Note that we assume that the first local minimum is
     * the global minimum.
     *
     * @param testData Data on which the optimal k should be calculated
     * @return the optimal k
     */
    public static int optimize(ArrayList<Card> testData) {

        HybridSort h = new HybridSort();

        // Initialize value sum, for k = 0
        int k = 0;
        SortArray s = new SortArray(testData);
        h.sort(s, k);
        int sum = s.getReadingOperations() + s.getWritingOperations();
        int possibles = Integer.MAX_VALUE;

        while (sum <= possibles && k <= s.getNumberOfItems()) {
            k++;
            s = new SortArray(testData);
            h.sort(s, k);
            sum = s.getReadingOperations() + s.getWritingOperations();
            if (sum < possibles) possibles = sum;
        }

        return k-1;
    }

}
