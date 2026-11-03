package objectOrientedProject;

import java.util.Objects;

/**
 * Represents an event generated at a node in the simulation.
 *
 * Events are identified by their unique ID and contain information
 * about their distance and previous known position.
 */
public class Event {

    private final int id;
    private int distance;
    private Node previousPosition;

    /**
     * Creates a new event.
     *
     * @param id unique event identifier
     * @param node node where the event was generated
     * @param distance distance from the event's origin
     */
    public Event(int id, Node node, int distance) {

        if (distance < 0) {
            throw new IllegalArgumentException(
                "Event distance cannot be negative"
            );
        }

        this.id = id;
        this.distance = distance;
        this.previousPosition = Objects.requireNonNull(
            node, "Event position cannot be null"
        );
    }

    public int getId() {
        return id;
    }

    public int getDistance() {
        return distance;
    }

    public Node getPrevPos() {
        return previousPosition;
    }

    /**
     * Updates the event's distance.
     *
     * @param distance new distance
     */
    public void setDistance(int distance) {

        if (distance < 0) {
            throw new IllegalArgumentException(
                "Event distance cannot be negative"
            );
        }

        this.distance = distance;
    }

    /**
     * Increments the event's distance by one.
     */
    public void addStep() {
        distance = Math.incrementExact(distance);
    }

    /**
     * Updates the event's previous position.
     *
     * @param position previous node
     */
    public void setPosition(Node position) {
        this.previousPosition = Objects.requireNonNull(
            position, "Event position cannot be null"
        );
    }

    /**
     * Creates an independent copy of this event.
     *
     * @return copied event
     */
    public Event copyEvent() {
        return new Event(id, previousPosition, distance);
    }

    /**
     * Events are considered equal if they share the same ID.
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj instanceof Event) {
            Event event = (Event) obj;
            return id == event.id;
        }
        return false;
    }

    /**
     * Hash code is based on the event ID.
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", distance=" + distance +
                ", previousPosition=(" +
                previousPosition.getX() + ", " +
                previousPosition.getY() + ")" +
                '}';
    }
}
