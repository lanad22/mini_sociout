/**
 * List.java
 *
 * @author: Phuong Do
 * @author: Chris Vo
 * @author: Skyler Berry
 * @author: Paolo Pedrigal
 * @author: Arushi Sharma
 * @author: Shohin Abdulkhamidov
 * CIS 22C Group Project
 */

import java.util.NoSuchElementException;

public class List<T> {
    private class Node {
        private T data;
        private Node next;
        private Node prev;

        public Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    private int length;
    private Node first;
    private Node last;
    private Node iterator;

    public List() {
        first = last = null;
        length = 0;
        iterator = null;
    }

    /**
     * Instantiates a new List by copying another List
     *
     * @param original The List to make a copy of
     * @precondition a new List object, which is an identical but separate copy of
     * the List original
     */
    public List(List<T> original) {
        if (original == null) {
            return;
        }
        if (original.isEmpty()) {
            length = 0;
            first = null;
            last = null;
        } else {
            Node temp = original.first;
            while (temp != null) {
                addLast(temp.data);
                temp = temp.next;
            }
        }
        iterator = null;
    }

    /**
     * Removes the first element from the List
     *
     * @throws NoSuchElementException when precondition is violated
     * @precondition !isEmpty()
     * @postcondition first element of the List gets removed
     */
    public void removeFirst() throws NoSuchElementException {
        if (first == null) {
            throw new NoSuchElementException("removeFirst: Cannot remove from an empty list!");
        } else if (length == 1) {
            first = last = null;
            iterator = null;
        } else {
            if (iterator == first) {
                iterator = null;
            }
            first = first.next;
            first.prev = null;
        }
        length--;
    }

    /**
     * Removes the last element from the List
     *
     * @throws NoSuchElementException when precondition is violated
     * @precondition !isEmpty()
     * @postcondition last element of the List gets removed
     */
    public void removeLast() throws NoSuchElementException {
        if (first == null) {
            throw new NoSuchElementException("removeLast: Cannot remove from an empty list!");
        } else if (length == 1) {
            first = last = null;
            iterator = null;
        } else {
            if (iterator == last) {
                iterator = null;
            }
            last = last.prev;
            last.next = null;
        }
        length--;
    }

    /**
     * Adds an element to the front of the List
     *
     * @param data the data to be added
     * @postcondition an element gets added to the front of the List
     */
    public void addFirst(T data) {
        if (first == null) {
            first = last = new Node(data);
        } else {
            Node N = new Node(data);
            N.next = first;
            first.prev = N;
            first = N;
        }
        length++;
    }

    /**
     * Retrieves the first element in the list
     *
     * @return the data contained in the first node
     * @throws NoSuchElementException when precondition is violated
     * @precondition !isEmpty()
     * @postcondition First element in List is returned
     */
    public T getFirst() throws NoSuchElementException {
        if (length == 0) {
            throw new NoSuchElementException("getFirst: List is empty. No data to access!");
        }
        return first.data;
    }

    /**
     * Retrieves the last element in the list
     *
     * @return the data contained in the last node
     * @throws NoSuchElementException when precondition is violated
     * @precondition !isEmpty()
     * @postcondition Last element in List is returned
     */
    public T getLast() throws NoSuchElementException {
        if (length == 0) {
            throw new NoSuchElementException("getLast: List is empty. No data to access!");
        }
        return last.data;
    }

    /**
     * Returns the length of the List
     *
     * @return the length of the List
     * @postcondition The length of the List is returned
     */
    public int getLength() {
        return length;
    }

    /**
     * Returns whether the List is empty
     *
     * @return true if the List is empty, otherwise false
     */
    public boolean isEmpty() {
        return length == 0;
    }

    /**
     * Adds an element to the end of the List
     *
     * @param data the data to be added
     * @postcondition an element gets added to the end of the List
     */
    public void addLast(T data) {
        if (first == null) {
            first = last = new Node(data);
        } else {
            Node N = new Node(data);
            last.next = N;
            N.prev = last;
            last = N;
        }
        length++;
    }

    /**
     * Adds an element to the List right after the iterator
     *
     * @param data the data to be added
     * @throws NullPointerException when precondition is violated
     * @precondition !offEnd()
     * @postcondition an element gets added to the right of the iterator
     */
    public void addIterator(T data) throws NullPointerException {
        if (offEnd()) {
            throw new NullPointerException(
                    "addIterator(): Failed to add node, iterator appears to be off the end of the list.");
        } else if (iterator == last) {
            addLast(data);
        } else {
            Node N = new Node(data);
            N.next = iterator.next;
            N.prev = iterator;
            iterator.next = N;
            N.next.prev = N;
        }
        length++;
    }

    /**
     * Removes the Node that the iterator is currently pointing to
     *
     * @throws NullPointerException when precondition is violated
     * @precondition !offEnd()
     * @postcondition iterator will be pointing to null
     */
    public void removeIterator() throws NullPointerException {
        if (offEnd()) {
            throw new NullPointerException("removeIterator: " + "Iterator is off end. Cannot remove.");
        } else if (iterator == first) {
            removeFirst();
        } else if (iterator == last) {
            removeLast();
        } else {
            iterator.prev.next = iterator.next;
            iterator.next.prev = iterator.prev;
            iterator = null;
            length--;
        }
    }

    /**
     * Sets the iterator to the front of the list
     *
     * @postcondition the iterator is reset to the front of the list
     */
    public void placeIterator() {
        iterator = first;
    }

    /**
     * Returns the data found at the iterator
     *
     * @return the data at the iterator
     * @throws NullPointerException when precondition is violated
     */
    public T getIterator() throws NullPointerException {
        if (iterator == null) {
            throw new NullPointerException("getIterator: Iterator is off the end of the list.");
        }
        return iterator.data;
    }

    /**
     * Returns whether or not the iterator is off the end of the list
     *
     * @return true if the iterator is off the list, otherwise false
     */
    public boolean offEnd() {
        return iterator == null;
    }

    /**
     * Moves the iterator toward the next element in the List
     *
     * @throws NullPointerException when precondition is violated
     * @precondition iterator != null
     */
    public void advanceIterator() throws NullPointerException {
        if (iterator == null) {
            throw new NullPointerException("advanceIterator: Iterator is null and cannot advance.");
        }
        iterator = iterator.next;
    }

    /**
     * Moves the iterator backwards to the previous element in the List
     *
     * @throws NullPointerException when precondition is violated
     * @precondition iterator != null
     */
    public void reverseIterator() throws NullPointerException {
        if (iterator == null) {
            throw new NullPointerException("reverseIterator: Iterator is null and cannot advance.");
        }
        iterator = iterator.prev;
    }

    /**
     * Points the iterator at first and then advances it to the specified index
     *
     * @param index the index where the iterator should be placed
     * @throws IndexOutOfBoundsException when precondition is violated
     * @precondition 0 < index <= length
     */
    public void iteratorToIndex(int index) throws IndexOutOfBoundsException {
        if (index <= 0 || index > length) {
            throw new IndexOutOfBoundsException("iteratorToIndex(): Specified index out of bounds of list!");
        }
        iterator = first;
        for (int i = 1; i < index; i++) {
            iterator = iterator.next;
        }
    }

    /**
     * Searches the List for the specified value using the linear search algorithm
     *
     * @param value the value to search for
     * @return the location of value in the List or -1 to indicate not found Note
     * that if the List is empty we will consider the element to be not
     * found post: position of the iterator remains unchanged
     */
    public int linearSearch(T value) {
        Node temp = first;
        for (int i = 1; i <= length; i++) {
            if (temp.data.equals(value)) {
                return i;
            }
            temp = temp.next;
        }
        return -1;
    }

    /**
     * Determines whether two Lists have the same data in the same order
     *
     * @param o the List to compare to this List
     * @return whether the two Lists are equal
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof List)) {
            return false;
        } else {
            List<T> L = (List<T>) o;
            if (this.length != L.length) {
                return false;
            } else {
                Node temp1 = this.first;
                Node temp2 = L.first;
                while (temp1 != null) { // Lists are same length
                    if (!(temp1.data.equals(temp2.data))) {
                        return false;
                    }
                    temp1 = temp1.next;
                    temp2 = temp2.next;
                }
                return true;
            }
        }
    }

    /**
     * Concatenates and formats all the data in the list
     *
     * @return the concatenated list data
     */
    @Override
    public String toString() {
        String result = "";
        Node temp = first;
        while (temp != null) {
            result += temp.data + "\n";
            temp = temp.next;
        }
        return result;
    }

    /**
     * Prints and enumerates the data in the list
     */
    public void printNumberedList() {
        Node temp = first;
        int listNumber = 1;
        while (temp != null) {
            System.out.println(listNumber + ". " + temp.data);
            temp = temp.next;
            listNumber++;
        }
    }
}