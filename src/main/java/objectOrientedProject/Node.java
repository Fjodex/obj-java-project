package objectOrientedProject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a node in the simulation network.
 *
 * Each node has coordinates, neighboring nodes, events and a queue
 * containing agents and requests.
 */
public class Node {

    private static final AtomicInteger NEXT_EVENT_ID =
            new AtomicInteger(1);

    private static final int EVENT_CREATION_PROBABILITY = 1000;

    private final int x;
    private final int y;

    private final List<Node> listOfNeighbors;
    private final NodeQueue nodeQueue;

    private final List<Event> eventList;
    private final List<Integer> eventSource;

    private final Random random = new Random();

    public Node(int x, int y) {
        this.x = x;
        this.y = y;

        this.listOfNeighbors = new ArrayList<>();
        this.nodeQueue = new NodeQueue();

        this.eventList = new ArrayList<>();
        this.eventSource = new ArrayList<>();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Adds a neighboring node.
     *
     * Prevents self-connections and duplicate neighbors.
     */
    public void addNeighbor(Node node) {

        Objects.requireNonNull(node, "Neighbor cannot be null");

        if (node == this) {
            return;
        }

        if (!listOfNeighbors.contains(node)) {
            listOfNeighbors.add(node);
        }
    }

    public NodeQueue getQueue() {
        return nodeQueue;
    }

    /**
     * Returns an unmodifiable view of neighboring nodes.
     */
    public List<Node> getListOfNeighbors() {
        return Collections.unmodifiableList(listOfNeighbors);
    }

    /**
     * Returns the events known by this node.
     *
     * The returned list remains mutable for compatibility with
     * the current Agent synchronization implementation.
     */
    public List<Event> getEventList() {
        return eventList;
    }

    /**
     * Returns the event source information.
     *
     * Data format:
     * [eventId, cycle, x, y]
     *
     * Repeated for every generated event.
     */
    public List<Integer> getEventSource() {
        return Collections.unmodifiableList(eventSource);
    }

    /**
     * Generates a new event and optionally creates an agent
     * responsible for distributing the event.
     *
     * @param cycle current simulation cycle
     * @return generated event ID
     */
    private int createEvent(int cycle) {

        int id = generateEventId();

        Event event = new Event(id, this, 0);

        eventList.add(event);

        // Store event metadata.
        eventSource.add(id);
        eventSource.add(cycle);
        eventSource.add(x);
        eventSource.add(y);

        // 50% probability of creating an agent.
        if (random.nextBoolean()) {
            createAgent(event, cycle);
        }

        return id;
    }

    /**
     * Generates a globally unique event ID within this JVM.
     */
    private int generateEventId() {

        int id = NEXT_EVENT_ID.getAndIncrement();

        if (id <= 0) {
            throw new IllegalStateException(
                "Event ID limit exceeded"
            );
        }

        return id;
    }

    /**
     * Adds an event if it is not already present.
     */
    public void addEvent(Event event) {

        Objects.requireNonNull(event, "Event cannot be null");

        if (!eventList.contains(event)) {
            eventList.add(event);
        }
    }

    /**
     * Creates an agent and adds it to this node's queue.
     */
    private void createAgent(Event event, int cycle) {

        nodeQueue.add(new Agent(this, event, cycle));
    }

    /**
     * Processes the next agent or request in the queue.
     */
    private void moveAgentRequest(int cycle) {

        if (nodeQueue.isEmpty()) {
            return;
        }

        // Avoid processing an agent or request more than once
        // during the same simulation cycle.
        if (nodeQueue.getCycle() == cycle) {
            return;
        }

        if (nodeQueue.nextInLine() == 0) {

            Agent agent = nodeQueue.removeAgent();

            agent.moveAgent();

            agent.getNode().getQueue().add(agent);

        } else if (nodeQueue.nextInLine() == 1) {

            Request request = nodeQueue.removeRequest();

            request.moveRequest();

            request.getNode().getQueue().add(request);
        }
    }

    /**
     * Updates the node during a simulation cycle.
     *
     * @param cycle current simulation cycle
     * @return generated event ID, or zero if no event was generated
     */
    public int updateNode(int cycle) {

        if (random.nextInt(EVENT_CREATION_PROBABILITY) == 0) {
            return createEvent(cycle);
        }

        moveAgentRequest(cycle);

        return 0;
    }
}
