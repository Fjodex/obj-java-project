package objectOrientedProject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class NodeQueueTest {

    private Node node;
    private NodeQueue queue;

    @BeforeEach
    void setUp() {
        node = new Node(10, 30);
        queue = node.getQueue();
    }

    @Test
    @DisplayName("New queue should be empty")
    void newQueueShouldBeEmpty() {
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }

    @Test
    @DisplayName("Adding an agent should increase queue size")
    void addAgentShouldIncreaseSize() {
        Agent agent = createAgent(1);

        queue.add(agent);

        assertFalse(queue.isEmpty());
        assertEquals(1, queue.size());
        assertEquals(0, queue.nextInLine());
    }

    @Test
    @DisplayName("Adding a request should increase queue size")
    void addRequestShouldIncreaseSize() {
        Request request = createRequest(2);

        // Request constructor automatically adds itself.
        assertEquals(1, queue.size());
        assertEquals(1, queue.nextInLine());
    }

    @Test
    @DisplayName("Queue should preserve FIFO ordering")
    void queueShouldPreserveFifoOrdering() {
        Agent agent1 = createAgent(1);
        Agent agent2 = createAgent(2);

        Request request1 = createRequest(3);
        Request request2 = createRequest(4);

        queue.add(agent1);
        // The requests were already added by their constructors.
        queue.add(agent2);

        // Current order: request1, request2, agent1, agent2
        assertEquals(1, queue.nextInLine());

        assertSame(request1, queue.removeRequest());
        assertSame(request2, queue.removeRequest());
        assertSame(agent1, queue.removeAgent());
        assertSame(agent2, queue.removeAgent());

        assertTrue(queue.isEmpty());
    }

    @Test
    @DisplayName("Queue should reject removing the wrong entity type")
    void shouldRejectWrongEntityType() {
        Request request = createRequest(5);

        assertThrows(
            IllegalStateException.class,
            () -> queue.removeAgent()
        );

        assertEquals(1, queue.size());
        assertSame(request, queue.removeRequest());
    }

    @Test
    @DisplayName("Removing from an empty queue should throw an exception")
    void removingFromEmptyQueueShouldThrow() {
        assertThrows(
            NoSuchElementException.class,
            () -> queue.removeAgent()
        );

        assertThrows(
            NoSuchElementException.class,
            () -> queue.removeRequest()
        );
    }

    @Test
    @DisplayName("Reading the next entity from an empty queue should throw")
    void readingEmptyQueueShouldThrow() {
        assertThrows(
            NoSuchElementException.class,
            () -> queue.nextInLine()
        );

        assertThrows(
            NoSuchElementException.class,
            () -> queue.getCycle()
        );
    }

    @Test
    @DisplayName("Queue should update cycles for all entities")
    void shouldUpdateAllCycles() {
        Agent agent = createAgent(6);
        Request request = createRequest(7);

        queue.add(agent);

        queue.updateCycle(100);

        assertEquals(100, agent.getCycle());
        assertEquals(100, request.getCycle());
    }

    private Agent createAgent(int id) {
        return new Agent(
            node,
            new Event(id, node, 0),
            1
        );
    }

    private Request createRequest(int id) {
        return new Request(
            node,
            new Event(id, node, 0),
            1
        );
    }
}
