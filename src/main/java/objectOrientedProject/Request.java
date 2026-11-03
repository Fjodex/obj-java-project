package objectOrientedProject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Represents a request searching for a specific event in the network.
 *
 * A request explores neighboring nodes until it finds its target event.
 * Once found, it returns to its starting node using its recorded path.
 */
public class Request {

    private final Event targetEvent;
    private final List<Node> eventPath;
    private final Random random = new Random();

    private Node node;
    private int cycle;
    private int steps;
    private int pathIndex;

    private boolean messageReceived;
    private boolean complete;

    private Event foundEvent;

    /**
     * Creates a request and adds it to the starting node's queue.
     *
     * @param node starting node
     * @param event event to search for
     * @param cycle current simulation cycle
     */
    public Request(Node node, Event event, int cycle) {

        this.node = Objects.requireNonNull(
            node, "Starting node cannot be null"
        );

        this.targetEvent = Objects.requireNonNull(
            event, "Target event cannot be null"
        ).copyEvent();

        this.cycle = cycle;
        this.steps = 0;

        this.eventPath = new ArrayList<>();

        // Record the starting position.
        eventPath.add(node);
        pathIndex = 0;

        node.getQueue().add(this);
    }

    /**
     * Checks whether the current node contains the requested event.
     */
    public boolean checkId() {
        return node.getEventList().contains(targetEvent);
    }

    /**
     * Processes one movement step.
     *
     * During the search phase, the request explores the network.
     * Once the event is found, it returns along its recorded path.
     */
    public void moveRequest() {

        if (complete) {
            return;
        }

        if (messageReceived) {
            returnToStart();
            return;
        }

        searchForEvent();
    }

    /**
     * Searches the current node for the requested event.
     */
    private void searchForEvent() {

        if (checkId()) {
            discoverEvent();
            return;
        }

        List<Node> neighbors = node.getListOfNeighbors();

        if (neighbors.isEmpty()) {
            // No possible movement.
            complete = true;
            return;
        }

        // Prefer nodes that have not yet been visited.
        List<Node> unvisitedNeighbors = new ArrayList<>();

        for (Node neighbor : neighbors) {

            if (!eventPath.contains(neighbor)) {
                unvisitedNeighbors.add(neighbor);
            }
        }

        List<Node> possibleMoves = unvisitedNeighbors.isEmpty()
                ? neighbors
                : unvisitedNeighbors;

        Node nextNode = possibleMoves.get(
            random.nextInt(possibleMoves.size())
        );

        setNode(nextNode);
    }

    /**
     * Handles discovery of the requested event.
     */
    private void discoverEvent() {

        int eventIndex = node.getEventList().indexOf(targetEvent);

        foundEvent = node.getEventList()
                .get(eventIndex)
                .copyEvent();

        messageReceived = true;

        // If the request started here, it is already home.
        if (pathIndex == 0) {
            complete = true;
        }
    }

    /**
     * Returns the request to its starting node by following
     * the recorded path in reverse order.
     */
    private void returnToStart() {

        if (pathIndex > 0) {

            pathIndex--;

            node = eventPath.get(pathIndex);

            steps++;

        } else {
            complete = true;
        }
    }

    /**
     * Moves the request to a new node during exploration.
     */
    private void setNode(Node node) {

        this.node = Objects.requireNonNull(node);

        eventPath.add(node);
        pathIndex = eventPath.size() - 1;

        steps++;
    }

    public Node getNode() {
        return node;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public int getSteps() {
        return steps;
    }

    public boolean isComplete() {
        return complete;
    }

    public boolean hasReceivedMessage() {
        return messageReceived;
    }

    /**
     * Returns a copy of the discovered event, or null if not found.
     */
    public Event getFoundEvent() {
        return foundEvent == null ? null : foundEvent.copyEvent();
    }

    /**
     * Returns a copy of the request's recorded path.
     */
    public List<Node> getEventPath() {
        return new ArrayList<>(eventPath);
    }
}
