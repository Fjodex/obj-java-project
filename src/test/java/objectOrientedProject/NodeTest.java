package objectOrientedProject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    @Test
    @DisplayName("Node should retain its coordinates")
    void shouldInitializeCoordinates() {
        Node node = new Node(10, 30);

        assertEquals(10, node.getX());
        assertEquals(30, node.getY());
    }

    @Test
    @DisplayName("Node should initialize with empty collections")
    void shouldInitializeEmpty() {
        Node node = new Node(10, 30);

        assertTrue(node.getListOfNeighbors().isEmpty());
        assertTrue(node.getEventList().isEmpty());
        assertTrue(node.getQueue().isEmpty());
    }

    @Test
    @DisplayName("Node should add neighboring nodes")
    void shouldAddNeighbor() {
        Node first = new Node(0, 0);
        Node second = new Node(10, 10);

        first.addNeighbor(second);

        assertTrue(first.getListOfNeighbors().contains(second));
        assertEquals(1, first.getListOfNeighbors().size());
    }

    @Test
    @DisplayName("Node should reject duplicate neighbors")
    void shouldRejectDuplicateNeighbors() {
        Node first = new Node(0, 0);
        Node second = new Node(10, 10);

        first.addNeighbor(second);
        first.addNeighbor(second);

        assertEquals(1, first.getListOfNeighbors().size());
    }

    @Test
    @DisplayName("Node should reject itself as a neighbor")
    void shouldRejectSelfAsNeighbor() {
        Node node = new Node(0, 0);

        node.addNeighbor(node);

        assertTrue(node.getListOfNeighbors().isEmpty());
    }

    @Test
    @DisplayName("Neighbor collection should be unmodifiable")
    void neighborsShouldBeUnmodifiable() {
        Node node = new Node(0, 0);

        assertThrows(
            UnsupportedOperationException.class,
            () -> node.getListOfNeighbors().add(new Node(1, 1))
        );
    }

    @Test
    @DisplayName("Node should add events without duplicates")
    void shouldAddEventsWithoutDuplicates() {
        Node node = new Node(0, 0);
        Event event = new Event(123, node, 0);

        node.addEvent(event);
        node.addEvent(event.copyEvent());

        assertEquals(1, node.getEventList().size());
        assertTrue(node.getEventList().contains(event));
    }

    @Test
    @DisplayName("Node should reject null events")
    void shouldRejectNullEvent() {
        Node node = new Node(0, 0);

        assertThrows(
            NullPointerException.class,
            () -> node.addEvent(null)
        );
    }
}
