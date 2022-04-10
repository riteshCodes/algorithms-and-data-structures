package lab;


import java.util.Random;

/**
 * Use a random pivot within Quick Sort.
 */
public class HybridSortRandomPivot extends HybridSort {

    @Override
    protected int getPivotIndex(int first, int last) {
        Random random = new Random();
        return random.nextInt(last - first + 1) + first; // [first, last]
    }
}
