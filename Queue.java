/**
 * Queue.java
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

public class Queue<T extends Comparable<T>> {

    private class Node {

        private Node next;
        private T data;

        private Node(T data) {
            this.next = null;
            this.data = data;
        }
    }

    private int length;
    private Node front;
    private Node end;

    /**** CONSTRUCTOR ****/

    /**
     * Instantiates a Queue object with default values
     *
     * @postcondition creates a new Queue object and assigns default values
     */

    public Queue() {
        length = 0;
        front = end = null;
    }

    /**
     * Creates a deep copy of a given Queue object
     *
     * @param o Queue object that deep copy is copying off of
     * @postcondition a deep copy of a Queue object is created
     */

    public Queue(Queue<T> o) {
        if (o == null) {
            return;
        } else if (o.length == 0) {
            this.length = 0;
            this.front = this.end = null;
        } else {
            Node temp = o.front;
            while (temp != null) {
                this.enqueue(temp.data);
                temp = temp.next;
            }
        }
    }

    /**** ACCESSORS ****/

    /**
     * Gets data stored at the front node of Queue
     *
     * @return data stored at the front Node
     * @throws NoSuchElementException when queue is empty
     * @precondition !isEmpty()
     */

    public T getFront() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("Error: getFront." + " Can't get data from an empty queue!");
        }
        return front.data;
    }

    /**
     * Gets the length of a Queue
     *
     * @return length of a Queue, where length >= 0
     */

    public int getLength() {
        return length;
    }

    /**
     * Determines whether Queue object is empty
     *
     * @return true/false if the queue is empty or not
     */

    public boolean isEmpty() {
        return length == 0;
    }

    /**
     * Determines whether data is sorted in ascending order by calling its recursive
     * helper method isSorted() Note: when length == 0 data is (trivially) sorted
     *
     * @return whether the data is sorted
     */

    public boolean isSorted() {
        if (length <= 1) {
            return true;
        }
        return isSorted(front);
    }

    /**
     * Helper method to isSorted Recursively determines whether data is sorted
     *
     * @param node the current node
     * @return whether the data is sorted
     */
    private boolean isSorted(Node node) {
        if (node.next == end) { // base case
            if (node.data.compareTo(node.next.data) <= 0) {
                return true;
            } else {
                return false;
            }
        } else { // recursive step
            if (node.data.compareTo(node.next.data) <= 0) {
                return isSorted(node.next);
            } else {
                return false;
            }
        }

    }

    /**
     * Uses the iterative linear search algorithm to locate a specific element and
     * return its position
     *
     * @param element the value to search for
     * @return the location of value from 1 to length Note that in the case
     * length==0 the element is considered not found
     */
    public int linearSearch(T element) {
        Node temp = front;
        for (int i = 1; i <= length; i++) {
            if (temp.data.equals(element)) {
                return i;
            }
            temp = temp.next;
        }
        return -1;
    }

    /**
     * Returns the location from 1 to length where value is located by calling the
     * private helper method binarySearch
     *
     * @param value the value to search for
     * @return the location where value is stored from 1 to length, or -1 to
     * indicate not found
     * @throws IllegalStateException when the precondition is violated.
     * @precondition isSorted()
     */

    public int binarySearch(T value) throws IllegalStateException {
        if (!isSorted()) {
            throw new IllegalStateException("Error: binarySearch. This queue is not initially sorted.");
        }
        return binarySearch(1, length, value);
    }

    /**
     * Searches for the specified value in by implementing the recursive
     * binarySearch algorithm
     *
     * @param low   the lowest bounds of the search
     * @param high  the highest bounds of the search
     * @param value the value to search for
     * @return the location at which value is located from 1 to length or -1 to
     * indicate not found
     */

    private int binarySearch(int low, int high, T value) {
        if (high < low) {
            return -1;
        }
        int mid = low + (high - low) / 2;
        Node temp = front;
        for (int i = 1; i < mid; i++) {
            temp = temp.next;
        } // temp should be pointing to mid

        if (temp.data.equals(value)) {
            return mid;
        } else if (temp.data.compareTo(value) > 0) {
            return binarySearch(low, mid - 1, value);
        } else {
            return binarySearch(mid + 1, high, value);
        }
    }

    /**** MUTATORS ****/

    /**
     * Adds an element to the end of the Queue
     *
     * @param data - data stored in Node to be added
     * @postcondition appends a Node to the end of a queue
     */

    public void enqueue(T data) {
        if (length == 0) {
            front = end = new Node(data);
        } else {
            Node N = new Node(data);
            end.next = N;
            end = N;
        }
        length++;
    }

    /**
     * Removes node at the front of the Queue
     *
     * @throws NoSuchElementException when queue is empty
     * @precondition !isEmpty()
     * @postcondition first element of queue is removed
     */

    public void dequeue() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("Error: dequeue. " + "Cannot remove from an empty queue.");
        } else if (length == 1) {
            front = end = null;
        } else {
            front = front.next;
        }
        length--;
    }

    /**** ADDITIONAL OPERATIONS ****/

    /**
     * Checks whether Queue objects have the same data in the same order
     *
     * @param o Queue to be compared to this Queue
     * @return true/false whether Queue objects are equal to each other
     */

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Queue)) {
            return false;
        } else {
            Queue<T> Q = (Queue<T>) o;
            if (this.length != Q.length) {
                return false;
            } else {
                Node temp1 = Q.front;
                Node temp2 = this.front;
                while (temp1 != null) {
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
     * Stores data in Queue into a string
     *
     * @return string that contains data of Queue for display
     */

    @Override
    public String toString() {
        String result = "";
        Node temp = this.front;
        while (temp != null) {
            result += temp.data + " ";
            temp = temp.next;
        }
        return result + "\n";
    }

    /**
     * Prints in reverse order to the console, followed by a new line by calling the
     * recursive helper method printReverse
     */

    public void printReverse() {
        printReverse(front);
        System.out.println();
    }

    /**
     * Recursively prints to the console the data in reverse order (no loops)
     *
     * @param node the current node
     */

    private void printReverse(Node node) {
        if (node == null) {
            return;
        }
        if (node == end) { // base case
            System.out.print(node.data);
        } else { // recursive step
            printReverse(node.next);
            System.out.print(" " + node.data);
        }

    }

}
