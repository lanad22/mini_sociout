/**
 * Graph.java
 *
 * @author: Phuong Do
 * @author: Chris Vo
 * @author: Skyler Berry
 * @author: Paolo Pedrigal
 * @author: Arushi Sharma
 * @author: Shohin Abdulkhamidov
 * CIS 22C Group Project
 */

import java.util.ArrayList;

public class Graph {
    private int vertices;
    private int edges;
    private ArrayList<List<Integer>> adj;
    private ArrayList<Character> color;
    private ArrayList<Integer> distance;
    private ArrayList<Integer> parent;

    /**Constructors*/

    /**
     * initializes an empty graph, with n vertices
     * and 0 edges
     *
     * @param n the number of vertices in the graph
     */
    public Graph(int n) {
        this.vertices = n;
        edges = 0;

        distance = new ArrayList<>(n);
        parent = new ArrayList<>(n);
        color = new ArrayList<>(n);
        adj = new ArrayList<>(n);

        for (int i = 0; i <= n; i++) {
            color.add('W');
            distance.add(-1);
            parent.add(null);
            adj.add(new List<Integer>());
        }
    }

    /*** Accessors ***/

    /**
     * Returns the number of edges in the graph
     *
     * @return the number of edges
     */
    public int getNumEdges() {
        return edges;
    }

    /**
     * Returns the number of vertices in the graph
     *
     * @return the number of vertices
     */
    public int getNumVertices() {
        return vertices;
    }

    public List<Integer> getAdj(int x) {
        return adj.get(x);
    }

    /**
     * returns whether the graph is empty (no edges)
     *
     * @return whether the graph is empty
     */
    public boolean isEmpty() {
        return edges == 0;
    }

    /**
     * Returns the value of the distance[v]
     *
     * @param v a vertex in the graph
     * @return the distance of vertex v
     * @throws IndexOutOfBoundsException when
     *                                   the precondition is violated
     * @precondition 0 <= v < vertices
     */
    public Integer getDistance(Integer v) throws IndexOutOfBoundsException {
        if (v < 0 || v >= vertices) {
            throw new IndexOutOfBoundsException("getDistance(): Distance cannot be less than 1.");
        }
        return distance.get(v);
    }

    /**
     * Returns the value of the parent[v]
     *
     * @param v a vertex in the graph
     * @return the parent of vertex v
     * @throws IndexOutOfBoundsException when
     *                                   the precondition is violated
     * @precondition v < vertices
     */
    public Integer getParent(Integer v) throws IndexOutOfBoundsException {
        if (v >= vertices) {
            throw new IndexOutOfBoundsException("getParent(): cannot get parent of an out of bounds vertex."); //revise
        }
        return parent.get(v);
    }

    /**
     * Returns the value of the color[v]
     *
     * @param v a vertex in the graph
     * @return the color of vertex v
     * @throws IndexOutOfBoundsException when
     *                                   the precondition is violated
     * @precondition 0 <= v < vertices
     */
    public Character getColor(Integer v) throws IndexOutOfBoundsException {
        if (v < 0 || v >= vertices) {
            throw new IndexOutOfBoundsException("getColor(): Cannot get color of an out of bounds vertex.");
        }
        return color.get(v);
    }

    /*** Mutators ***/

    /**
     * Inserts vertex v into the adjacency list of vertex u
     * (i.e. inserts v into the list at index u)
     *
     * @throws IndexOutOfBoundsException when the precondition
     *                                   is violated
     * @precondition, 0 <= u, v < vertices
     */
    public void addDirectedEdge(Integer u, Integer v) throws IndexOutOfBoundsException {
        if (u < 0 && v >= vertices) {
            throw new IndexOutOfBoundsException("addDirectedEdge(): Cannot insert out of bounds of adjacency list.");
        }
        adj.get(u).addLast(v);
        edges++;
    }

    /**
     * Inserts vertex v into the adjacency list of vertex u
     * (i.e. inserts v into the list at index u)
     * and inserts u into the adjacent vertex list of v
     *
     * @precondition, 0 <= u, v < vertices
     */
    public void addUndirectedEdge(Integer u, Integer v) {
        if (u < 0 && v >= vertices) {
            throw new IndexOutOfBoundsException("addUndirectedEdge(): Cannot insert out of bounds of adjacency list.");
        }
        adj.get(u).addLast(v);
        adj.get(v).addLast(u);
        edges++;

    }

    /*** Additional Operations ***/

    /**
     * Creates a String representation of the Graph
     * Prints the adjacency list of each vertex in the graph,
     * vertex: <space separated list of adjacent vertices>
     */
    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < adj.size(); i++) {
            result += "vertex: " + adj.get(i) + "\n";
        }
        return result;
    }

    /**
     * Prints the current values in the parallel ArrayLists
     * after executing BFS. First prints the heading:
     * v <tab> c <tab> p <tab> d
     * Then, prints out this information for each vertex in the graph
     * Note that this method is intended purely to help you debug your code
     */
    public void printBFS() {
        System.out.println("v\tc\tp\td");
        for (int i = 0; i <= vertices; i++)
            System.out.println(i + "\t" + color.get(i) + "\t" + parent.get(i) + "\t" + distance.get(i));
    }

    /**
     * Performs breath first search on this Graph give a source vertex
     *
     * @param source
     * @throws IllegalStateException     if the graph is empty
     * @throws IndexOutOfBoundsException when the source vertex
     *                                   is not a vertex in the graph
     * @precondition graph must not be empty
     * @precondition source is a vertex in the graph
     */

    public void BFS(Integer source) throws IllegalStateException, IndexOutOfBoundsException {
        if (isEmpty()) {
            throw new IllegalStateException("BFS(): Graph is empty.");
        } else if (source > vertices || source < 0) {
            throw new IndexOutOfBoundsException("BFS(): Source is not a vertex in the graph");
        } else {
            for (int i = 0; i <= vertices; i++) {
                color.set(i, 'W');
                distance.set(i, -1);
                parent.set(i, null);
            }
            color.set(source, 'G');
            distance.set(source, 0);

            Queue<Integer> q = new Queue<>();
            q.enqueue(source);

            while (!q.isEmpty()) {
                int x = q.getFront();
                q.dequeue();
                adj.get(x).placeIterator();
                while (!adj.get(x).offEnd()) {
                    int y = adj.get(x).getIterator();
                    if (color.get(y) == 'W') {
                        color.set(y, 'G');
                        distance.set(y, distance.get(x) + 1);
                        parent.set(y, x);
                        q.enqueue(y);
                    }
                    adj.get(x).advanceIterator();
                }
                color.set(x, 'B');
            }
        }
    }
}