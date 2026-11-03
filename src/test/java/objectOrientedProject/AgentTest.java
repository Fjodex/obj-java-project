package objectOrientedProject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;
class AgentTest {

    @Test
    @DisplayName("Agent should initialize at the correct node")
    void shouldInitializeCorrectly() {
        Node node = new Node(0, 0);
        Event event = new Event(123, node, 0);

        Agent agent = new Agent(node, event, 10);

        assertSame(node, agent.getNode());
        assertEquals(10, agent.getCycle());
        assertEquals(0, agent.getSteps());
        assertFalse(agent.maxStepsReached());
    }

    @Test
    @DisplayName("Agent should receive its initial event")
    void shouldReceiveInitialEvent() {
        Node node = new Node(0, 0);
        Event event = new Event(123, node, 0);

        Agent agent = new Agent(node, event, 0);

        assertTrue(agent.getEventList().contains(event));
        assertTrue(node.getEventList().contains(event));
    }

    @Test
    @DisplayName("Agent should move to an unvisited neighbor")
    void shouldMoveToNeighbor() {
        Node start = new Node(0, 0);
        Node destination = new Node(10, 10);

        start.addNeighbor(destination);

        Agent agent = new Agent(
            start,
            new Event(123, start, 0),
            0
        );

        agent.moveAgent();

        assertSame(destination, agent.getNode());
        assertEquals(1, agent.getSteps());
    }

    @Test
    @DisplayName("Agent should propagate events to neighboring nodes")
    void shouldPropagateEvents() {
        Node start = new Node(0, 0);
        Node destination = new Node(10, 10);

        start.addNeighbor(destination);

        Event event = new Event(123, start, 0);

        Agent agent = new Agent(start, event, 0);

        agent.moveAgent();

        assertTrue(destination.getEventList().contains(event));

        Event propagatedEvent = destination.getEventList().get(0);

        assertEquals(1, propagatedEvent.getDistance());
    }

    @Test
    @DisplayName("Agent should respect its maximum step limit")
    void shouldRespectMaximumSteps() {
        Node start = new Node(0, 0);
        Node destination = new Node(10, 10);

        start.addNeighbor(destination);
        destination.addNeighbor(start);

        Agent agent = new Agent(
            start,
            new Event(123, start, 0),
            0,
            1
        );

        agent.moveAgent();

        assertEquals(1, agent.getSteps());
        assertTrue(agent.maxStepsReached());

        Node currentNode = agent.getNode();

        agent.moveAgent();

        assertSame(currentNode, agent.getNode());
        assertEquals(1, agent.getSteps());
    }

    @Test
    @DisplayName("Agent should handle nodes without neighbors")
    void shouldHandleNoNeighbors() {
        Node node = new Node(0, 0);

        Agent agent = new Agent(
            node,
            new Event(123, node, 0),
            0
        );

        assertDoesNotThrow(agent::moveAgent);

        assertSame(node, agent.getNode());
        assertEquals(0, agent.getSteps());
    }

    @Test
    @DisplayName("Agent cycle should be updateable")
    void shouldUpdateCycle() {
        Node node = new Node(0, 0);

        Agent agent = new Agent(
            node,
            new Event(123, node, 0),
            10
        );

        agent.setCycle(20);

        assertEquals(20, agent.getCycle());
    }
}
