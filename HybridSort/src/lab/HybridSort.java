package lab;


import frame.SortArray;

public class HybridSort {

    // Global Variable to store the array for the repetitive use in the methods
    private SortArray array = null;

    /**
     * Sort the given array using a hybrid method of Quick Sort and Insertion Sort.
     *
     * @param array The array to sort.
     * @param k     Parameter k when we switch from Quick Sort to Insertion Sort: If the size of the subset which should be sorted is less than k, use Insertion Sort,
     *              otherwise keep on using Quick Sort.
     */
    public void sort(SortArray array, int k) {
        assert (k >= 0);

        // Value stored in the global variable for further use.
        this.array = array;

        int first = 0;
        int last = array.getNumberOfItems();
        hybridQuickSort(first, last, k);
    }

    /**
     * InsertionSort Algorithm, to sort the array from position first to last
     *
     * @param first position
     * @param last  position
     */
    private void insertionSort(int first, int last) {

        for (int j = first + 1; j <= last - 1; j++) {
            Card key = array.getElementAt(j);
            int i = j - 1;
            Card comparedCard = array.getElementAt(i);
            while (i >= first && comparedCard.compareTo(key) > 0) {
                array.setElementAt(i + 1, comparedCard);
                i = i - 1;
            }
            array.setElementAt(i + 1, key);
        }
    }

    /**
     * Partition algorithm to divide the arrays into two halves.
     * The one half containing the smaller or equal values and the other half containing greater values than the pivot.
     *
     * @param first
     * @param last
     * @param pivotIndex, index of present pivot at the moment
     * @return the current index of the new pivot element
     */
    private int partition(int first, int last, int pivotIndex) {

        Card pivot = array.getElementAt(pivotIndex);
        // Override effect: getPivot(first, last)
        swap(pivotIndex, first); // Swap the pivot element always at the first position

        int i = last;
        for (int j = last - 1; j >= first + 1; j--) {
            if (array.getElementAt(j).compareTo(pivot) > 0) {
                i--;
                swap(i, j);
            }
        }
        // swap places for the pivot element
        array.setElementAt(first, array.getElementAt(i - 1));
        array.setElementAt(i - 1, pivot);

        return i - 1; // current index of new pivot

    }

    /**
     * Swap the array elements at positions i and j
     *
     * @param i
     * @param j
     */
    private void swap(int i, int j) {
        Card temp = array.getElementAt(i);
        array.setElementAt(i, array.getElementAt(j));
        array.setElementAt(j, temp);

    }

    /**
     * HybridSort, switch the implementation of either insertion-sort or quick-sort algorithm based on the value of k und the subset of the array.
     *
     * @param first
     * @param last
     * @param k
     */
    private void hybridQuickSort(int first, int last, int k) {
        if (first >= last) return; // Recursion termination

        if ((last - first) > k) {  // Case: subset size is greater than k
            int partitionIndex = partition(first, last, getPivotIndex(first, last - 1)); // last is the total number of items
            hybridQuickSort(first, partitionIndex, k);
            hybridQuickSort(partitionIndex + 1, last, k);
        } else {
            insertionSort(first, last);
        }

    }

    /**
     * Method to get the index of the pivot element. Method to override
     *
     * @param first
     * @param last
     * @return the pivot element's index. Default case: first
     */
    protected int getPivotIndex(int first, int last) {
        // Default case: Pivot must be the first element of the given array to be sorted
        return first;
    }
}
