package frame;

import java.util.ArrayList;

import lab.Card;

/**
 * Do NOT change anything in this class!
 * <p>
 * The SortArray class provides simple basic functions, to store a list of
 * cards to track the number of operations.
 * <p>
 * This class contains two members (readingOperations and writingOperations)
 * that act as counters for the number of accesses to the arrays to be sorted.
 * These are used by the JUnit tests to construct the output. The methods
 * provided in this class should be sufficient for you to sort the records of
 * the input files.
 */

public class SortArray {

    private int numberOfItems;

    private ArrayList<Card> listOfItems;

    private int readingOperations;
    private int writingOperations;

    /**
     * @param numberOfItems number of items to hold
     */
    public SortArray(ArrayList<Card> items) {
        numberOfItems = items.size();
        readingOperations = 0;
        writingOperations = 0;
        listOfItems = new ArrayList<Card>(items);
    }

    /**
     * sets the elements at index. if index is >= numberOfItems or less then
     * zero an IndexOutOfBoundException will occur.
     *
     * @param index  the index of the Elements to set
     * @param record a Card object
     */
    public void setElementAt(int index, Card record) {
        this.listOfItems.set(index, record);

        writingOperations++;
    }

    /**
     * Retrieves the information stored at position Index. if index is >=
     * numberOfItems or less then zero an IndexOutOfBoundException will occur.
     *
     * @param index Index defines which elements to retrieve from the SortArray
     * @return Returns a Card object.
     */
    public Card getElementAt(int index) {

        Card result = new Card(this.listOfItems.get(index));
        readingOperations++;
        return result;
    }

    /**
     * @return Returns the number of reading operations.
     */
    public int getReadingOperations() {
        return readingOperations;
    }

    /**
     * @return Returns the number of writing operations.
     */
    public int getWritingOperations() {
        return writingOperations;
    }

    /**
     * @return Returns the numberOfItems.
     */
    public int getNumberOfItems() {
        return numberOfItems;
    }
}
