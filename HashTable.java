
/**
 * HashTable.java
 *
 * @author: Phuong Do
 * @author: Chris Vo
 * @author: Skyler Berry
 * @author: Paolo Pedrigal
 * @author: Arushi Sharma
 * @author: Shohin ...
 * CIS 22C Group Project
 */

import java.util.ArrayList;

public class HashTable<T> {

    private int numElements;
    private ArrayList<List<T>> Table;

    /**
     * Constructor for the hash table. Initializes the Table to be sized according
     * to value passed in as a parameter Inserts size empty Lists into the table.
     * Sets numElements to 0
     *
     * @param size the table size
     */
    public HashTable(int size) {
        Table = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Table.add(i, new List<>());
        }
        numElements = 0;
    }

    /** Accessors */

    /**
     * returns the hash value in the Table for a given Object
     *
     * @return the index in the Table
     */
    private int hash(String key) {
        key = key.toLowerCase(); // to have case sensitivity

        // hash algorithm
        int sum = 0;
        for (int i = 0; i < key.length(); i++) {
            sum += (int) key.charAt(i);
        }

        return sum % Table.size();
    }

    /**
     * counts the number of keys at this index
     *
     * @param index the index in the Table
     * @return the count of keys at this index
     * @throws IndexOutOfBoundsException
     * @precondition 0 <= index < Table.length
     */
    public int countBucket(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= Table.size()) {
            throw new IndexOutOfBoundsException("Error: countBucket(): Index is out of bounds!");
        } else {
            return Table.get(index).getLength();
        }
    }

    /**
     * returns total number of keys in the Table
     *
     * @return total number of keys
     */
    public int getNumElements() {
        return numElements;
    }

    /**
     * Accesses a specified key in the Table
     *
     * @param t the key to search for
     * @return the value to which the specified key is mapped, or null if this table
     * contains no mapping for the key.
     * @throws NullPointerException if the specified key is null
     * @precondition t != null
     */
    public T get(T t, String key) throws NullPointerException {
        if (t == null) {
            throw new NullPointerException("Error: get(). The key is null.");
        } else {
            int bucket = hash(key);
            if (Table.get(bucket).linearSearch(t) == -1) {
                return null;
            } else {
                int pos = Table.get(bucket).linearSearch(t);
                Table.get(bucket).iteratorToIndex(pos);
                return Table.get(bucket).getIterator();
            }
        }
    }

    /**
     * Determines whether a specified key is in the Table
     *
     * @param t the key to search for
     * @return whether the key is in the Table
     * @throws NullPointerException if the specified key is null
     * @precondition t != null
     */
    public boolean contains(T t, String key) throws NullPointerException {
        if (t == null) {
            throw new NullPointerException("Error: contains(). Specified key is null.");
        } else {
            return Table.get(hash(key)).linearSearch(t) != -1;
        }
    }

    public List<T> getBucketList(String key) {
        int bucket = hash(key);
        return Table.get(bucket);
    }

    /** Mutators */

    /**
     * Inserts a new element in the Table at the end of the chain in the bucket to
     * which the key is mapped
     *
     * @param t the key to insert
     * @throws NullPointerException for a null key
     * @precondition t != null
     */
    public void put(T t, String key) throws NullPointerException {
        if (t == null) {
            throw new NullPointerException("Error: put(). Key is null.");
        } else {
            int bucket = hash(key);
            Table.get(bucket).addLast(t);
            numElements++;
        }
    }

    /**
     * removes the key t from the Table calls the hash method on the key to
     * determine correct placement has no effect if t is not in the Table or for a
     * null argument
     *
     * @param t the key to remove
     * @throws NullPointerException if the key is null
     */
    public void remove(T t, String key) throws NullPointerException {
        if (t == null) {
            throw new NullPointerException("Error: remove(). Key is null.");
        } else {
            int bucket = hash(key);
            if (Table.get(bucket).linearSearch(t) == -1) {
                return;
            } else {
                int pos = Table.get(bucket).linearSearch(t);
                Table.get(bucket).iteratorToIndex(pos);
                Table.get(bucket).removeIterator();
                numElements--;
            }
        }
    }

    /**
     * Clears this hash table so that it contains no keys.
     */
    public void clear() {
        for (int i = 0; i < Table.size(); i++) {
            Table.set(i, new List<>());
        }
        numElements = 0;
    }

    /** Additional Methods */

    /**
     * Prints all the keys at a specified bucket in the Table. Each key displayed on
     * its own line, with a blank line separating each key Above the keys, prints
     * the message "Printing bucket #<bucket>:" Note that there is no <> in the
     * output
     *
     * @param bucket the index in the Table
     */
    public void printBucket(int bucket) {
        System.out.println("Printing bucket #" + bucket + ":");
        Table.get(bucket).placeIterator();
        for (int i = 0; i < Table.get(bucket).getLength(); i++) {
            System.out.println(Table.get(bucket).getIterator() + "\n");
            Table.get(bucket).advanceIterator();
        }

    }


    /**
     * Prints the first key at each bucket along with a count of the total keys with
     * the message "+ <count> -1 more at this bucket." Each bucket separated with
     * two blank lines. When the bucket is empty, prints the message "This bucket is
     * empty." followed by two blank lines
     */

    public void printTable() {
        for (int i = 0; i < Table.size(); i++) {
            if (Table.get(i).isEmpty()) {
                System.out.println("This bucket is empty.\n\n");
            } else {
                System.out.print(Table.get(i).getFirst());
                System.out.println(" + " + (Table.get(i).getLength() - 1) + " more at this bucket\n\n");
            }

        }
    }

    /**
     * Starting at the first bucket, and continuing in order until the last bucket,
     * concatenates all elements at all buckets into one String
     */
    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < Table.size(); i++) {
            result += Table.get(i);
        }
        return result;
    }

}