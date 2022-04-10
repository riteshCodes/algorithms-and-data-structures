package lab;


import java.util.Random;

import frame.ListNode;
import frame.TableEntry;

public class HashTable {

    protected int capacity;
    protected int hash_a;
    protected int hash_b;
    protected LinkedList[] entryLists;

    private int count; // count of entries

    /**
     * The constructor
     *
     * @param initialCapacity represents the initial size of the Hash Table. The
     *                        <p>
     *                        Hash-Table itself should be implemented in the array entryLists.
     *                        When the load factor exceeds 75%, the capacity of the Hash-Table should be increased
     *                        as described in the method rehash below.
     */
    public HashTable(int initialCapacity) {
        this.capacity = initialCapacity;
        while (!isPrime(capacity)) { // get the first occurrence of a prime number
            this.capacity++;
        }
        entryLists = new LinkedList[this.capacity];
        Random random = new Random();
        // randomly generated hash_a and hash_b
        this.hash_a = random.nextInt(this.capacity);
        this.hash_b = random.nextInt(this.capacity);

        count = 0;
    }

    /**
     * Returns if the given number (n) is prime.
     *
     * @param n
     * @return true if the given number is prime, else false
     */
    private static boolean isPrime(int n) {
        int divider = 0; // to store the number of dividers.
        for (int i = n; i > 0; i--) {
            if (n % i == 0) divider++;
        }
        return divider == 2; // prime number divisible by 1 and the number itself.(count = 2)
    }

    /**
     * Search for an TableEntry with the given key. If no such TableEntry is found,
     * return null.
     */
    public TableEntry find(String key) {
        LinkedList pointer = this.entryLists[hash(key)]; // pointer set to the key-position in the Hashtable
        if (pointer == null) return null; // case: no TableEntry with such hash-key is found

        // when the TableEntry is found, with unique hash key(index) generated from the given key
        ListNode y = pointer.head(); // y as pointer to the head of the TableEntry
        while (y != pointer.nil() && y.entry().getKey() != key) y = y.next();

        if (y != pointer.nil() && y.entry().getKey() == key) return y.entry();
        else return null;
    }

    /**
     * Insert the given entry in the hash table. If there exists already an entry
     * with this key, this entry should be overwritten. After inserting a new
     * element, it might be necessary to increase the capacity of the hash table.
     */
    public void insert(TableEntry entry) {

        if (find(entry.getKey()) == null) { // no such TableEntry with entry-key exists

            if (this.entryLists[hash(entry.getKey())] == null) { // TableEntry is not yet initialised
                LinkedList x = new LinkedList();
                x.append(entry); // append to the recently created LinkedList
                this.entryLists[this.hash(entry.getKey())] = x; // LinkedList is added with the unique hash-key(index) in the hash-table
            } else {
                // case: hash-key (index) in the hash-table exist with some values
                this.entryLists[this.hash(entry.getKey())].append(entry); // append the new entry to the present LinkedList
            }
        } else { // the given entry already exist in the hash-table
            delete(entry.getKey()); // delete the entry
            if (this.entryLists[hash(entry.getKey())] != null)
                this.entryLists[hash(entry.getKey())].append(entry); // TableEntry with other elements already exist and new entry is appended
            else {    // initialise with new LinkedList
                LinkedList x = new LinkedList();
                x.append(entry);
                this.entryLists[this.hash(entry.getKey())] = x;
            }
        }
        count += 1; // added entry
        // After insertion, check if the increment in the capacity of hash-table is required
        if (count >= 0.75 * this.capacity) rehash();
    }

    /**
     * Delete the TableEntry with the given key from the hash table and return the
     * entry. Return null if key was not found.
     */
    public TableEntry delete(String key) {
        if (find(key) == null) return null; // if TableEntry not found
        else {
            LinkedList x = entryLists[hash(key)];
            ListNode pointer = x.head();
            while (pointer != x.nil()) {
                if (pointer.entry().getKey().equals(key)) {
                    x.delete(pointer); // delete the TableEntry with the given key from hash-table
                    count -= 1; // size is decreased by 1
                    return pointer.entry();
                }
                pointer = pointer.next();
            }
            return null;
        }
    }

    /**
     * The hash function used in the hash table.
     */
    public int hash(String x) {
        int sum = 0;

        for (int i = 0; i < x.length(); i++) {
            int value = (int) x.charAt(i);
            sum += ((Math.pow(3, i) + i) % (Math.pow(2, 16)) * value) % (Math.pow(2, 16));
        }

        int h_a_b = ((this.hash_a * sum + this.hash_b) % this.entryLists.length) % this.entryLists.length;

        if (h_a_b < 0) h_a_b = h_a_b * (-1); // absolute value

        return h_a_b;
    }

    /**
     * Returns the number of TableEntries in the hash table.
     */
    public int size() {
        return count;
    }

    /**
     * Increase the capacity of the hash table and reorder all entries.
     */
    private void rehash() {

        LinkedList[] oldLists = this.entryLists;

        int value = 5 * this.capacity; // increment
        while (!isPrime(value)) {
            value++;
        }
        this.capacity = value; // capacity changed to the new prime value

        entryLists = new LinkedList[this.capacity]; // entryLists modified with new capacity value

        // randomly generated hash_a and hash_b
        hash_a = new Random().nextInt(value);
        hash_b = new Random().nextInt(value);

        // copy old values from the old TableEntry
        for (int i = 0; i < oldLists.length; i++)
            if (oldLists[i] != null) {
                ListNode x = oldLists[i].head();
                while (x != oldLists[i].nil()) {
                    insert(x.entry()); // insert entries
                    x = x.next();
                }
            }
    }

    /**
     * Return the current "quality" of the hash table. The quality is measured by
     * calculating the maximum number of collisions between entries in the table
     * (i.e., the longest linked list in the hash table).
     */
    public int quality() {
        int max = 0;
        for (int i = 0; i < this.entryLists.length; i++) {
            if (entryLists[i] != null)
                max = (entryLists[i].length() > max) ? entryLists[i].length() : max; // compare the max collisions
        }
        return max;

    }

    public int getHash_a() {
        return hash_a;
    }

    public int getHash_b() {
        return hash_b;
    }

    public int getCapacity() {
        return capacity;
    }

    public LinkedList[] getEntryLists() {
        return entryLists;
    }

}
