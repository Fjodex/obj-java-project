package objectOrientedProject;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;
class RequestTest {

    @Test
    @DisplayName("Request should initialize correctly")
    void shouldInitializeCorrectly() {
        Node node = new Node(0, 0);
        Event event = new Event(123, node, 0);

        Request request = new Request(node, event, 10);

        assertSame(node, request.getNode());
        assertEquals(10, request.getCycle());
        assertEquals(0, request.getSteps());

        // Constructor automatically enqueues the request.
        assertEquals(1, node.getQueue().size());
    }

    @Test
    @DisplayName("Request should discover an event at its current node")
    void shouldDiscoverEventAtCurrentNode() {
        Node node = new Node(0, 0);
        Event event = new Event(123, node, 0);

        Request request = new Request(node, event, 0);

        node.addEvent(event.copyEvent());

        request.moveRequest();

        assertTrue(request.checkId());
        assertTrue(request.hasReceivedMessage());
        assertTrue(request.isComplete());
        assertNotNull(request.getFoundEvent());
    }

    @Test
    @DisplayName("Request should move to a neighboring node")
    void shouldMoveToNeighbor() {
        Node start = new Node(0, 0);
        Node destination = new Node(10, 10);

        start.addNeighbor(destination);

        Request request = new Request(
            start,
            new Event(123, start, 0),
            0
        );

        request.moveRequest();

        assertSame(destination, request.getNode());
        assertEquals(1, request.getSteps());
    }

    @Test
    @DisplayName("Request should discover an event at a neighboring node")
    void shouldDiscoverNeighborEvent() {
        Node start = new Node(0, 0);
        Node destination = new Node(10, 10);

        start.addNeighbor(destination);

        Event event = new Event(123, destination, 0);

        Request request = new Request(start, event, 0);

        destination.addEvent(event.copyEvent());

        request.moveRequest();

        assertSame(destination, request.getNode());
        assertFalse(request.hasReceivedMessage());

        request.moveRequest();

        assertTrue(request.hasReceivedMessage());
        assertEquals(0, request.getFoundEvent().getDistance());
    }

    @Test
    @DisplayName("Request should return along its recorded path")
    void shouldReturnToStartingNode() {
        Node start = new Node(0, 0);
        Node destination = new Node(10, 10);

        start.addNeighbor(destination);

        Event event = new Event(123, destination, 0);

        Request request = new Request(start, event, 0);

        destination.addEvent(event.copyEvent());

        // Move to destination.
        request.moveRequest();

        // Discover event.
        request.moveRequest();

        assertTrue(request.hasReceivedMessage());

        // Return to starting node.
        request.moveRequest();

        assertSame(start, request.getNode());

        // Finish request.
        request.moveRequest();

        assertTrue(request.isComplete());
    }

    @Test
    @DisplayName("Request should record its movement path")
    void shouldRecordPath() {
        Node start = new Node(0, 0);
        Node destination = new Node(10, 10);

        start.addNeighbor(destination);

        Request request = new Request(
            start,
            new Event(123, destination, 0),
            0
        );

        request.moveRequest();

        List<Node> path = request.getEventPath();

        assertEquals(2, path.size());
        assertSame(start, path.get(0));
        assertSame(destination, path.get(1));
    }

    @Test
    @DisplayName("Request should handle nodes without neighbors")
    void shouldHandleNoNeighbors() {
        Node node = new Node(0, 0);

        Request request = new Request(
            node,
            new Event(123, node, 0),
            0
        );

        request.moveRequest();

        assertTrue(request.isComplete());
        assertSame(node, request.getNode());
    }

    @Test
    @DisplayName("Completed request should not move again")
    void completedRequestShouldNotMove() {
        Node node = new Node(0, 0);
        Event event = new Event(123, node, 0);

        Request request = new Request(node, event, 0);

        node.addEvent(event.copyEvent());

        request.moveRequest();

        int previousSteps = request.getSteps();

        request.moveRequest();

        assertEquals(previousSteps, request.getSteps());
        assertTrue(request.isComplete());
    }

    @Test
    @DisplayName("Request should update its cycle")
    void shouldUpdateCycle() {
        Node node = new Node(0, 0);

        Request request = new Request(
            node,
            new Event(123, node, 0),
            10
        );

        request.setCycle(20);

        assertEquals(20, request.getCycle());
    }
}
