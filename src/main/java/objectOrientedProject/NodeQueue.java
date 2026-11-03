package objectOrientedProject;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * FIFO queue for agents and requests waiting at a node.
 *
 * Agents and requests share the same queue, preserving
 * the order in which they were added.
 */
public class NodeQueue {

    private static final int AGENT = 0;
    private static final int REQUEST = 1;

    private final Deque<QueueEntry> queue;

    public NodeQueue() {
        queue = new ArrayDeque<>();
    }

    /**
     * Adds an agent to the end of the queue.
     */
    public void add(Agent agent) {
        queue.addLast(new QueueEntry(
            Objects.requireNonNull(agent, "Agent cannot be null")
        ));
    }

    /**
     * Adds a request to the end of the queue.
     */
    public void add(Request request) {
        queue.addLast(new QueueEntry(
            Objects.requireNonNull(request, "Request cannot be null")
        ));
    }

    /**
     * Checks whether the queue is empty.
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * Returns the type of the next entry.
     *
     * @return 0 for Agent, 1 for Request
     */
    public int nextInLine() {
        return getFirstEntry().getType();
    }

    /**
     * Returns the cycle of the next queued entity.
     */
    public int getCycle() {
        return getFirstEntry().getCycle();
    }

    /**
     * Removes and returns the next agent.
     *
     * @throws IllegalStateException if the next entry is not an agent
     */
    public Agent removeAgent() {

        QueueEntry entry = getFirstEntry();

        if (entry.getType() != AGENT) {
            throw new IllegalStateException(
                "Next queue entry is not an Agent"
            );
        }

        queue.removeFirst();

        return entry.getAgent();
    }

    /**
     * Removes and returns the next request.
     *
     * @throws IllegalStateException if the next entry is not a request
     */
    public Request removeRequest() {

        QueueEntry entry = getFirstEntry();

        if (entry.getType() != REQUEST) {
            throw new IllegalStateException(
                "Next queue entry is not a Request"
            );
        }

        queue.removeFirst();

        return entry.getRequest();
    }

    /**
     * Updates the cycle of every queued entity.
     *
     * This preserves the original scheduling behaviour.
     */
    public void updateCycle(int cycle) {

        for (QueueEntry entry : queue) {
            entry.setCycle(cycle);
        }
    }

    /**
     * Returns the number of queued entities.
     */
    public int size() {
        return queue.size();
    }

    /**
     * Retrieves the first queue entry.
     */
    private QueueEntry getFirstEntry() {

        QueueEntry entry = queue.peekFirst();

        if (entry == null) {
            throw new NoSuchElementException(
                "Queue is empty"
            );
        }

        return entry;
    }

    /**
     * Internal representation of a queued entity.
     *
     * Each entry contains exactly one Agent or Request.
     */
    private static class QueueEntry {

        private final Agent agent;
        private final Request request;

        QueueEntry(Agent agent) {
            this.agent = agent;
            this.request = null;
        }

        QueueEntry(Request request) {
            this.agent = null;
            this.request = request;
        }

        int getType() {
            return agent != null ? AGENT : REQUEST;
        }

        int getCycle() {
            return agent != null
                ? agent.getCycle()
                : request.getCycle();
        }

        void setCycle(int cycle) {
            if (agent != null) {
                agent.setCycle(cycle);
            } else {
                request.setCycle(cycle);
            }
        }

        Agent getAgent() {
            return agent;
        }

        Request getRequest() {
            return request;
        }
    }
}
