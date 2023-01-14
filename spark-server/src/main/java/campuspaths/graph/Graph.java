package campuspaths.graph;
import java.util.*;

/**
 * Represents a Directed Labeled Graph. The graph contains a collection of 'nodes' (vertices) and
 * 'edges'. Each edge connects 2 nodes in a one-way fashion.
 *
 * The children of a node are the nodes to which there is an edge from the first node
 * The parent of a node are the nodes in which there is an edge that points to that node
 *
 * Graph takes 2 generic types (K, V). K represents the nodes in the graph. V represents the edge
 * data/edge label of the graph
 */
public class Graph <K, V>{
    /**
     * Holds all the Nodes and Edges
     */
    private final Map<K, Set<Edge<V, K>>> graph;

    /**
     * tracks number of edges part of graph
     */
    private int numEdges;

    /**
     * turn complex part of checkRep() on or off (for debug purposes ONLY)
     */
    private static final boolean DEBUG = false;
    // Abstract Function:
    //      An Map representation of a graph where:
    //          graph.keySet() == the nodes in the graph
    //          graph.get(n) == the set of edges in the graph
    //          associated with n, where n a node in graph.keySet();
    //      Example:
    //          If no nodes, then graph = {}
    //          If nodes but no edges, then graph = {a=[]} where a is the node
    //          If nodes with edges, then graph = {a=[AB]} where a is the node and AB is the edge
    //              associated with the node
    // Representation Invariant:
    //     graph != null &&
    //     No node in graph is null &&
    //     No Edge in graph is null &&
    //     this.keySet() must contain all the destinations of the edges
    //     No edge coming from the same node and pointing to the same node have the same edge label
    /**
     * @spec.effects Constructs a new empty graph (Generic Type K represents the nodes while V
     * represents the edge data)
     */
    public Graph() {
        graph = new HashMap<>();
        checkRep();
    }

    /**
     * Throws an exception if the representation variant is violated
     */
    private void checkRep() {
        assert (graph != null);

        if (DEBUG) {
            for (K node : graph.keySet()) {
                assert (node != null);
                assert (graph.get(node) != null);
                for (Edge<V, K> edge : graph.get(node)) {
                    assert (edge != null);
                    assert (graph.containsKey(edge.getDestination()));
                    for (Edge<V, K> edge1 : graph.get(node)) {
                        assert (edge == edge1 || !edge.equals(edge1));
                    }
                }
            }
        }
    }
    /**
     * returns whether the graph contains a node or not
     *
     * @param node the node to check in graph
     * @return true if graph contains node and false otherwise
     * @throws IllegalArgumentException if node == null
     */
    public boolean containsNode(K node) {
        if (node == null) {
            throw new IllegalArgumentException("given node is null");
        }
        return graph.containsKey(node);
    }

    /**
     * returns if a node in the graph contains a certain edge
     *
     * @param node the node to check for the edge
     * @param edgeLabel the edge with the label to search for in the node
     * @param nodeTo the destination of the edge to search for
     * @return true if the node contains the edge with the given edgeLabel and false otherwise
     * @throws IllegalArgumentException if edgeLabel == null, node == null,
     * !this.containsNode(node), or !this.contains(nodeTo)
     */
    public boolean containsEdge(K node, V edgeLabel, K nodeTo) {
        if (edgeLabel == null || node == null || nodeTo == null) {
            throw new IllegalArgumentException("Given param(s) are null");
        } else if (!this.containsNode(node) || !this.containsNode(nodeTo)) {
            throw new IllegalArgumentException("graph does not contain given node(s)");
        }
        return graph.get(node).contains(new Edge<V, K>(edgeLabel, nodeTo));
    }
    /**
     * returns whether the graph is empty
     *
     * @return true if no nodes in graph, false otherwise
     */
    public boolean isEmpty() {
        return graph.isEmpty();
    }

    /**
     * Adds a Node with no edges to the graph
     *
     * @param node the node to be added to the graph
     * @spec.effects adds the node to the graph
     * @throws IllegalArgumentException if node == null, this.containsNode(node)
     */
    public void addNode(K node) {
        checkRep();
        if (node == null) {
            throw new IllegalArgumentException("given node is null");
        } else if (this.containsNode(node)) {
            throw new IllegalArgumentException("graph already contains node");
        }
        graph.put(node, new HashSet<>());
        checkRep();
    }

    /**
     * Adds an edge to the given node on the graph
     *
     * @param nodeFrom the node to add the edge to
     * @param edgeLabel the label of the edge to be added
     * @param nodeTo the node to which the edge points to
     * @spec.effects creates an edge with the given edgeLabel and add it to the graph. Edge points
     * from given nodeFrom to given nodeTo.
     * @throws IllegalArgumentException nodeFrom == null, nodeTo == null, edgeLabel == null,
     * !this.contains(nodeFrom), !this.contains(nodeTo),
     * or this.containsEdge(nodeFrom, edgeLabel, nodeTo)
     */
    public void addEdge(K nodeFrom, V edgeLabel, K nodeTo) {
        checkRep();
        if (nodeFrom == null || edgeLabel == null || nodeTo == null) {
            throw new IllegalArgumentException("Given param(s) are null");
        } else if (!this.containsNode(nodeFrom) || !this.containsNode(nodeTo)) {
          throw new IllegalArgumentException("Graph does not contain given node(s)");
        } else if (this.containsEdge(nodeFrom, edgeLabel, nodeTo)) {
            throw new IllegalArgumentException("Graph already contains given edge");
        }

        graph.get(nodeFrom).add(new Edge<V, K>(edgeLabel, nodeTo));
        numEdges++;
        checkRep();
    }

    /**
     * returns all the nodes of the graph
     *
     * @return the nodes of this graph
     */
    public Set<K> getNodes() {
        return Collections.unmodifiableSet(graph.keySet());
    }

    /**
     * returns the set of edges associated with a node
     *
     * @param node the node to get the children from
     * @return a set of the children nodes
     * @throws IllegalArgumentException if node == null or !this.containsNode(node)
     */
    public Set<Edge<V, K>> getChildren(K node){
        if (node == null) {
            throw new IllegalArgumentException("given node is null");
        } else if (!this.containsNode(node)) {
            throw new IllegalArgumentException("graph does not contain given node");
        }
        return Collections.unmodifiableSet(graph.get(node));
    }

    /**
     * Returns a String representation of this Graph.
     *
     * @return a String representation of this graph that displays the number of nodes and edges
     */
    public String toString() {
        return "Graph contains: " + graph.size() + " nodes, " + numEdges + " edges";
    }

    /**
     * Represents a Labeled Edge in a Directed Labeled Graph. <b> Edge </b> includes a label for
     * the edge and a Node to which the labeled edge points to.
     */
    public static class Edge <V, K>{
        /**
         * edge label for this edge
         */
        private final V label;
        /**
         * destination for this edge
         */
        private final K  dest;
        // Abstract Function:
        // AF(this) = Edge e such that
        //      e.label = this.label
        //      e.destination = this.dest
        // Representation Invariant:
        //      this.label != null &&
        //      this.dest != null

        /**
         * @param label the label for the LabeledEdge
         * @param dest the node for which this Edge points to
         * @throws IllegalArgumentException if label == null or dest == null
         * @spec.effects constructs a new Edge with 'label' that points to 'dest'
         */
        public Edge(V label, K dest) {
            if (label == null || dest == null) {
                throw new IllegalArgumentException("label or dest is null");
            }

            this.label = label;
            this.dest = dest;
            checkRep();
        }

        /**
         * Throws an exception if the representation variant is violated
         */
        private void checkRep(){
            assert (label != null);
            assert (dest != null);
        }
        /**
         * returns the label of this Edge
         *
         * @return the label associated with this Edge
         */
        public V getLabel() {
            return label;
        }


        /**
         * returns the Destination of this Edge
         *
         * @return the destination Node that this Edge points to
         */
        public K getDestination() {
            return dest;
        }

        /**
         * returns the String representation of this
         *
         * @return a String representing this. Returned String will be in this format:
         * "destination(label)"
         */
        public String toString() {
            return dest + "(" + label + ")";
        }

        /**
         * Standard equality operation.
         *
         * @param obj the edge to be compared for equality
         * @return true if and only if 'obj' is an instance of Edge
         * @spec.requires obj != null
         */
        @Override
        public boolean equals(Object obj) {
            if (! (obj instanceof Graph.Edge<?, ?>)) {
                return false;
            }
            Edge<?, ?> e = (Edge<?, ?>) obj;
            return this.label.equals(e.getLabel()) && this.dest.equals(e.getDestination());
        }

        /**
         * Standard hashCode function
         *
         * @return an int that all objects equal to this will also return
         */
        @Override
        public int hashCode() {
            return this.label.hashCode() + 31 * this.dest.hashCode();
        }

//        @Override
//        public int compareTo(Edge<T, E> other) {
//            return this.label.compareTo(other.label);
//        }
    }

}
