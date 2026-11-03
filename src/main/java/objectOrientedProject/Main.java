package objectOrientedProject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Entry point for the LilleputtLand simulation.
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        // Validate command-line arguments.
        if (args.length == 0) {
            throw new IllegalArgumentException(
                "Missing input file. Usage: java lilleputtLand.Main <filename>"
            );
        }

        Environment environment;

        // Initialize the simulation environment.
        try (Scanner scanner = new Scanner(new File(args[0]))
                .useDelimiter("[\\s,]+")) {

            environment = new Environment(scanner);
        }

        // Run the simulation.
        runSimulation(environment);
    }

    /**
     * Executes the simulation for the configured number of cycles.
     *
     * @param environment simulation environment
     */
    private static void runSimulation(Environment environment) {

        while (environment.getCycle() < environment.getNumberOfCycles()) {

            int currentCycle = environment.getCycle();

            // Generate requests at regular intervals.
            if (currentCycle > 0 &&
                    currentCycle % environment.getRequestInterval() == 0) {

                createRequests(environment, currentCycle);
            }

            // Update all nodes.
            for (Node node : environment.getNodes()) {

                int generatedEventId = node.updateNode(currentCycle);

                environment.setEventID(generatedEventId);
            }

            environment.plusCycle();
        }
    }

    /**
     * Creates requests at the designated request nodes.
     *
     * @param environment simulation environment
     * @param cycle current simulation cycle
     */
    private static void createRequests(
            Environment environment, int cycle) {

        int eventId = environment.getRandomId();

        // No events available to search for.
        if (eventId == 0) {
            return;
        }

        for (Node node : environment.getRequestNodes()) {

            Event targetEvent = new Event(eventId, node, 0);

            new Request(node, targetEvent, cycle);

            node.getQueue().updateCycle(cycle);
        }
    }
}
