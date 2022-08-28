package graph.junitTests;
import org.junit.*;
import static org.junit.Assert.*;
import graph.*;
import org.junit.rules.Timeout;


public final class GraphTest {
    @Rule public Timeout globalTimeout = Timeout.seconds(10);
    private static final String NODE_A = "a";
    private static final String NODE_B = "b";
    private static final String NODE_C = "c";
    private static final String NODE_D = "d";
    private static final String EDGE_AB = "ab";
    private static final String EDGE_BA = "ba";
    private static final String EDGE_AA = "aa";
    private static final String EDGE_BC = "bc";

    // convenient way to create an empty graph
    private Graph<String, String> graphEmpty() {
        return new Graph<>();
    }
    // convenient way to create a graph with one node
    private Graph<String, String> graphOneNode(String node) {
        Graph<String, String> graph = new Graph<>();
        graph.addNode(node);
        return graph;
    }

    // convenient way to create a graph with multiple nodes
    private Graph<String, String> graphMultipleNodes(String node1, String node2, String node3) {
        Graph<String, String> graph = new Graph<>();
        graph.addNode(node1);
        graph.addNode(node2);
        graph.addNode(node3);
        return graph;
    }

    @Test
    public void testIsEmpty() {
        assertTrue(graphEmpty().isEmpty());
    }

    @Test
    public void testIsEmptyContainsNodes () {
        assertFalse(graphOneNode(NODE_A).isEmpty());
    }

    @Test
    public void testContainsNode() {
        assertTrue(graphOneNode(NODE_A).containsNode(NODE_A));
    }

    @Test
    public void testContainsMultipleNodes() {
        Graph<String, String> graph = graphMultipleNodes(NODE_A, NODE_B, NODE_C);
        assertTrue(graph.containsNode(NODE_A));
        assertTrue(graph.containsNode(NODE_B));
        assertTrue(graph.containsNode(NODE_C));
    }

    @Test
    public void testContainsNodeNotFound() {
        assertFalse(graphOneNode(NODE_A).containsNode(NODE_B));
    }

    @Test
    public void testContainsNodeNotFoundMultiple() {
        assertFalse(graphMultipleNodes(NODE_A, NODE_B, NODE_C).containsNode(NODE_D));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsNodeNull() {
        graphEmpty().containsNode(null);
    }

    @Test
    public void testContainsEdge() {
        Graph<String, String> graph = graphMultipleNodes(NODE_A, NODE_B, NODE_C);
        graph.addEdge(NODE_A, EDGE_AB, NODE_B);
        graph.addEdge(NODE_A, EDGE_AA, NODE_A);

        assertTrue(graph.containsEdge(NODE_A, EDGE_AB, NODE_B));
        assertTrue(graph.containsEdge(NODE_A, EDGE_AA, NODE_A));
        assertFalse(graph.containsEdge(NODE_A, EDGE_BC, NODE_C));
        assertFalse(graph.containsEdge(NODE_B, EDGE_AB, NODE_B));
        assertFalse(graph.containsEdge(NODE_A, EDGE_BC, NODE_A));
        assertFalse(graph.containsEdge(NODE_A, EDGE_AB, NODE_C));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsEdgeNullEdgeLabel() {
        Graph<String, String> graph = graphOneNode(NODE_A);
        graph.containsEdge(NODE_A, null, NODE_B);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsEdgeNullDestination() {
        graphOneNode(NODE_A).containsEdge(NODE_A, EDGE_AB, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsEdgeNullNode() {
        Graph<String, String> graph = graphMultipleNodes(NODE_A, NODE_B, NODE_C);
        graph.addEdge(NODE_A, EDGE_AB, NODE_B);
        graph.containsEdge(null, EDGE_AB, NODE_B);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsEdgeNodeFromNotFound() {
        Graph<String, String> graph = graphOneNode(NODE_A);
        graph.addEdge(NODE_A, EDGE_AA, NODE_A);
        graph.containsEdge(NODE_B, EDGE_AA, NODE_A);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsEdgeNodeToNotFound() {
        Graph<String, String> graph = graphOneNode(NODE_A);
        graph.addEdge(NODE_A, EDGE_AA, NODE_A);
        graph.containsEdge(NODE_A, EDGE_AA, NODE_B);
    }

    @Test
    public void testToStringEmpty() {
        assertEquals("Graph contains: 0 nodes, 0 edges", graphEmpty().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNodeNull() {
        Graph<String, String> graph = graphEmpty();
        graph.addNode(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNodeGraphAlreadyContains() {
        Graph<String, String> graph = graphOneNode(NODE_A);
        graph.addNode(NODE_A);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEdgeNullNodeFrom() {
        graphMultipleNodes(NODE_A, NODE_B, NODE_C).addEdge(null, EDGE_AB, NODE_B);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEdgeNullNodeTo() {
        graphMultipleNodes(NODE_A, NODE_B, NODE_C).addEdge(NODE_A, EDGE_AB, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEdgeNullEdge() {
        graphMultipleNodes(NODE_A, NODE_B, NODE_C).addEdge(NODE_A, null, NODE_B);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEdgeGraphContainsEdge() {
        Graph<String, String> graph = graphMultipleNodes(NODE_A, NODE_B, NODE_C);
        graph.addEdge(NODE_A, EDGE_AB, NODE_B);

        graph.addEdge(NODE_A, EDGE_AB, NODE_B);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveEdgeGetChildren() {
        Graph<String, String> graph = graphMultipleNodes(NODE_A, NODE_B, NODE_C);
        graph.addEdge(NODE_A, EDGE_AB, NODE_B);
        graph.getChildren(NODE_A).remove(NODE_A);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddEdgeGetChildren(){
        Graph<String, String> graph = graphMultipleNodes(NODE_A, NODE_B, NODE_C);
        graph.getChildren(NODE_A).add(new Graph.Edge<>(EDGE_AB, NODE_B));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddNodeWithGetNodes() {
        Graph<String, String> graph = graphMultipleNodes(NODE_A, NODE_B, NODE_C);
        graph.getNodes().add(NODE_D);
    }


    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveNodeWithGetNodes() {
        Graph<String, String> graph = graphMultipleNodes(NODE_A, NODE_B, NODE_C);
        graph.getNodes().remove(NODE_A);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetChildrenNull() {
        Graph<String, String> graph = graphEmpty();
        graph.getChildren(NODE_A);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetChildrenDoesNotContainNode(){
        Graph<String, String> graph = graphOneNode(NODE_A);
        graph.getChildren(NODE_B);
    }

    @Test
    public void testToStringMultipleNodes() {
        Graph<String, String> graph = graphMultipleNodes(NODE_A, NODE_B, NODE_C);
        assertEquals("Graph contains: 3 nodes, 0 edges", graph.toString());
    }

    @Test
    public void testToStringNodesAndEdgesSimple() {
        Graph<String, String> graph = graphOneNode(NODE_A);
        graph.addEdge(NODE_A, EDGE_AA, NODE_A);
        assertEquals("Graph contains: 1 nodes, 1 edges", graph.toString());
    }

    @Test
    public void testToStringNodesAndEdgesComplex() {
        Graph<String, String> graph = graphMultipleNodes(NODE_A, NODE_B, NODE_C);
        graph.addEdge(NODE_A, EDGE_AB, NODE_B);
        graph.addEdge(NODE_B, EDGE_BA, NODE_A);
        graph.addEdge(NODE_B, EDGE_BC, NODE_C);
        assertEquals("Graph contains: 3 nodes, 3 edges", graph.toString());
    }
}
