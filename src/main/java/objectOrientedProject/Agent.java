package objectOrientedProject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class Agent {

    private static final int DEFAULT_MAX_STEPS = 50;

    private Node node;
    private final List<Event> eventList = new ArrayList<>();
    private final Set<Node> visitedNodes = new HashSet<>();

    private final Random random = new Random();
    private final int maxSteps;

    private int steps;
    private int currentCycle;

    public Agent(Node node, Event event, int cycle) {
        this(node, event, cycle, DEFAULT_MAX_STEPS);
    }

    public Agent(Node node, Event event, int cycle, int maxSteps) {
        this.node = Objects.requireNonNull(node, "Node cannot be null");
        Objects.requireNonNull(event, "Event cannot be null");

        if (maxSteps < 0) {
            throw new IllegalArgumentException(
                "Maximum steps cannot be negative"
            );
        }

        this.currentCycle = cycle;
        this.maxSteps = maxSteps;
        this.steps = 0;

        visitedNodes.add(node);

        // Give the agent its initial event.
        eventList.add(event.copyEvent());

        syncEvents();
    }

    /**
     * Synchronizes events between the agent and its current node.
     * The event with the shortest distance takes precedence.
     */
    private void syncEvents() {

        List<Event> nodeEvents = node.getEventList();

        // First synchronize events already present on the node.
        for (Event nodeEvent : new ArrayList<>(nodeEvents)) {

            int index = eventList.indexOf(nodeEvent);

            if (index == -1) {
                // Agent does not know about this event.
                eventList.add(nodeEvent.copyEvent());
            } else {
                // Both know about the event. Keep the shortest distance.
                Event agentEvent = eventList.get(index);

                if (agentEvent.getDistance() < nodeEvent.getDistance()) {
                    nodeEvent.setDistance(agentEvent.getDistance());
                    nodeEvent.setPosition(agentEvent.getPrevPos());

                } else if (nodeEvent.getDistance() < agentEvent.getDistance()) {
                    agentEvent.setDistance(nodeEvent.getDistance());
                    agentEvent.setPosition(nodeEvent.getPrevPos());
                }
            }
        }

        // Then propagate events known by the agent to the node.
        for (Event agentEvent : eventList) {

            if (!nodeEvents.contains(agentEvent)) {
                nodeEvents.add(agentEvent.copyEvent());
            }
        }
    }

    /**
     * Moves the agent to a neighboring node.
     */
    public void moveAgent() {

        if (maxStepsReached()) {
            return;
        }

        List<Node> neighbors = node.getListOfNeighbors();

        // No possible movement.
        if (neighbors.isEmpty()) {
            return;
        }

        List<Node> unvisitedNeighbors = new ArrayList<>();

        for (Node neighbor : neighbors) {
            if (!visitedNodes.contains(neighbor)) {
                unvisitedNeighbors.add(neighbor);
            }
        }

        // Prefer unexplored nodes, otherwise revisit a random neighbor.
        List<Node> possibleMoves = unvisitedNeighbors.isEmpty()
                ? neighbors
                : unvisitedNeighbors;

        // Remember the current position before moving.
        stepEventPositions();

        Node nextNode = possibleMoves.get(
                random.nextInt(possibleMoves.size())
        );

        setNode(nextNode);

        // Increase the distance of every known event.
        stepEvents();

        // Exchange information with the new node.
        syncEvents();

        steps++;
    }

    /**
     * Increments the distance of all events known by the agent.
     */
    private void stepEvents() {

        for (Event event : eventList) {
            event.addStep();
        }
    }

    /**
     * Updates the previous position of all known events.
     */
    private void stepEventPositions() {

        for (Event event : eventList) {
            event.setPosition(node);
        }
    }

    /**
     * Updates the agent's current node and visited-node history.
     */
    private void setNode(Node node) {

        this.node = Objects.requireNonNull(node);
        visitedNodes.add(node);
    }

    public Node getNode() {
        return node;
    }

    public int getCycle() {
        return currentCycle;
    }

    public void setCycle(int cycle) {
        this.currentCycle = cycle;
    }

    public int getSteps() {
        return steps;
    }

    public boolean maxStepsReached() {
        return steps >= maxSteps;
    }

    public List<Event> getEventList() {
        return new ArrayList<>(eventList);
    }
}
