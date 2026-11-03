package objectOrientedProject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    private Node node;

    @BeforeEach
    void setUp() {
        node = new Node(10, 20);
    }

    @Test
    @DisplayName("Event should retain its initial values")
    void shouldInitializeCorrectly() {
        Event event = new Event(123, node, 5);

        assertEquals(123, event.getId());
        assertEquals(5, event.getDistance());
        assertSame(node, event.getPrevPos());
    }

    @Test
    @DisplayName("Adding a step should increase distance")
    void shouldIncreaseDistance() {
        Event event = new Event(123, node, 0);

        event.addStep();

        assertEquals(1, event.getDistance());
    }

    @Test
    @DisplayName("Distance should be updateable")
    void shouldUpdateDistance() {
        Event event = new Event(123, node, 0);

        event.setDistance(10);

        assertEquals(10, event.getDistance());
    }

    @Test
    @DisplayName("Negative distance should be rejected")
    void shouldRejectNegativeDistance() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Event(123, node, -1)
        );

        Event event = new Event(123, node, 0);

        assertThrows(
            IllegalArgumentException.class,
            () -> event.setDistance(-1)
        );
    }

    @Test
    @DisplayName("Events with identical IDs should be equal")
    void eventsWithSameIdShouldBeEqual() {
        Event first = new Event(123, node, 0);
        Event second = new Event(123, node, 10);

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    @DisplayName("Events with different IDs should not be equal")
    void eventsWithDifferentIdsShouldNotBeEqual() {
        Event first = new Event(123, node, 0);
        Event second = new Event(456, node, 0);

        assertNotEquals(first, second);
    }

    @Test
    @DisplayName("Copy should have independent event state")
    void copyShouldBeIndependent() {
        Event original = new Event(123, node, 5);

        Event copy = original.copyEvent();

        assertEquals(original, copy);
        assertNotSame(original, copy);

        copy.addStep();

        assertEquals(5, original.getDistance());
        assertEquals(6, copy.getDistance());
    }

    @Test
    @DisplayName("Previous position should be updateable")
    void shouldUpdatePosition() {
        Event event = new Event(123, node, 0);
        Node newNode = new Node(20, 30);

        event.setPosition(newNode);

        assertSame(newNode, event.getPrevPos());
    }

    @Test
    @DisplayName("Null position should be rejected")
    void shouldRejectNullPosition() {
        assertThrows(
            NullPointerException.class,
            () -> new Event(123, null, 0)
        );
    }
}
