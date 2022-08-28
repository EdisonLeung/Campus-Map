package graph.junitTests;

import graph.*;
import org.junit.*;
import org.junit.Rule;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

/**
 * JUnit tests for Graph.Edge
 */
public final class EdgeTest {
    @Rule public Timeout globalTimeout = Timeout.seconds(10);
    private Graph.Edge<String, String> testEdge;
    private static final String LABEL = "testLabel";
    private static final String DESTINATION = "testDestinationNode";

    @Before
    public void init() {
        testEdge = new Graph.Edge<>(LABEL, DESTINATION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructionNullLabel() {
        new Graph.Edge<String, String>(null, DESTINATION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructionNullDestination() {
        new Graph.Edge<String, String>(LABEL, null);
    }

    @Test
    public void testGetLabel() {
        assertEquals(LABEL, testEdge.getLabel());
    }

    @Test
    public void testGetDestination() {
        assertEquals(DESTINATION, testEdge.getDestination());
    }

    @Test
    public void testToString() {
        assertEquals(DESTINATION + "(" + LABEL + ")", testEdge.toString());
    }

    @Test
    public void testEquals() {
        assertTrue(testEdge.equals(new Graph.Edge<>(LABEL, DESTINATION)));
    }

    @Test
    public void testEqualsDifferentLabel(){
        assertFalse(testEdge.equals(new Graph.Edge<>("badLabel", DESTINATION)));
        assertFalse(testEdge.equals(new Graph.Edge<>(LABEL.toLowerCase(), DESTINATION)));
    }

    @Test
    public void testEqualsDifferentDestination() {
        assertFalse(testEdge.equals(new Graph.Edge<>(LABEL, "badDestination")));
        assertFalse(testEdge.equals(new Graph.Edge<>(LABEL, DESTINATION.toLowerCase())));
    }

    @Test
    public void testHashCode() {
        assertTrue(testEdge.hashCode() == new Graph.Edge<>(LABEL, DESTINATION).hashCode());
        assertFalse(testEdge.hashCode() == new Graph.Edge<>(LABEL, "badDestination").hashCode());
        assertFalse(testEdge.hashCode() == new Graph.Edge<>("badLabel", DESTINATION).hashCode());
    }

    @Test
    public void testHashCodeWithEquals() {
        assertTrue(testEdge.hashCode() == new Graph.Edge<>(LABEL, DESTINATION).hashCode() &&
                testEdge.equals(new Graph.Edge<>(LABEL, DESTINATION)));
    }
}
