package objectOrientedProject;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

/**
 * The environment class that holds all the nodes, request nodes, Ids of events and the variables such as cycles etc.
 *
 * @author dv18abk
 * @since 06/05-23
 */
public class Environment {
    private final ArrayList<Node> nodes;
    private final ArrayList<Node> requestNodes;
    private final ArrayList<Integer> eventListId;
    private final int numberOfCycles;
    private final int requestInterval;
    private int currentCycle;
    private final int lenBetweenNodes;

    /**
     * Creates the environment from the given content in the scanner and calculates which nodes are neighbors to which.
     *
     * @param sc The scanner containing the file to create an environment from
     */
    public Environment(Scanner sc){
        Random rand = new Random();
        nodes = new ArrayList<>();
        requestNodes = new ArrayList<>();
        eventListId = new ArrayList<>();
        numberOfCycles = 10000;
        requestInterval = 400;
        currentCycle = 0;
        lenBetweenNodes = 15;
        int numOfNodes;

        try{
            numOfNodes = sc.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Input not correct. First row should be number of nodes, every new row should be ");
            System.out.println("'x,y' where x and y is the X respectively Y coordinate. Exiting program...");
            throw new InputMismatchException();
        }

        for (int i = 0; i < numOfNodes; i++){
            try {
                nodes.add(new Node(sc.nextInt(), sc.nextInt()));
            } catch(InputMismatchException e) {
                System.out.println("Input not correct. First row should be number of nodes, every new row should be ");
                System.out.println("'x,y' where x and y is the X respectively Y coordinate. Exiting program...");
                throw new InputMismatchException();
            }
        }

        while(requestNodes.size() != 4){
            int pos = rand.nextInt(nodes.size());
            if(!requestNodes.contains(nodes.get(pos))){
                requestNodes.add(nodes.get(pos));
            }
        }

        linkNeighbors();
    }

    /**
     * Calculates if nodes should count as neighbors and adds these to the node neighbor list.
     */
    private void linkNeighbors(){
        double[] arrX = new double[2];
        double[] arrY = new double[2];
        double ans;

        for(Node linkNode : nodes){
            for(Node checkNode : nodes){
                arrX[0] = linkNode.getX();
                arrX[1] = checkNode.getX();
                arrY[0] = linkNode.getY();
                arrY[1] = checkNode.getY();

                if(arrX[0] < arrX[1]){
                    swap(arrX);
                }
                if(arrY[0] < arrY[1]){
                    swap(arrY);
                }
                ans = Math.sqrt(Math.pow(arrX[0] - arrX[1], 2.0) + Math.pow(arrY[0] - arrY[1], 2.0));
                if(ans > 0 && ans <= lenBetweenNodes){
                    linkNode.addNeighbor(checkNode);
                }
            }
        }
    }

    /**
     * A simple swap function
     *
     * @param arr Array with doubles containing the numbers to be swapped
     */
    private void swap(double[] arr){
        double temp = arr[0];
        arr[0] = arr[1];
        arr[1] = temp;
    }

    /**
     * Returns a list of the request nodes
     *
     * @return A list of the request nodes
     */
    public ArrayList<Node> getRequestNodes(){
        return requestNodes;
    }

    /**
     * Adds an event id to a list for the request nodes to use when creating a request.
     *
     * @param id The id as an integer
     */
    public void setEventID(int id){
        if (id != 0) eventListId.add(id);
    }

    /**
     * Get a list with all the nodes in the environment
     *
     * @return A list of the nodes
     */
    public ArrayList<Node> getNodes(){
        return nodes;
    }

    /**
     * Get the list of the all event id's
     *
     * @return A list of id's
     */
    public ArrayList<Integer> getEventIdList(){
        return eventListId;
    }

    /**
     * Get the current cycle
     *
     * @return The current cycle as an integer
     */
    public int getCycle(){
        return currentCycle;
    }

    /**
     * Get a random id from the list of id's that is stored
     *
     * @return An id as integer
     */
    public int getRandomId() {
        Random random = new Random();
        if (!eventListId.isEmpty()) {
            return eventListId.get(random.nextInt(eventListId.size()));
        } else {
            return 0;
        }
    }

    /**
     * Increases the current cycle by one.
     */
    public void plusCycle(){
        currentCycle++;
    }

    /**
     * Get the number of cycles the program should run for.
     *
     * @return How many cycles the program should run as an integer
     */
    public int getNumberOfCycles(){
        return numberOfCycles;
    }

    /**
     * Get how often requests should be created in the request nodes.
     *
     * @return The interval as an integer
     */
    public int getRequestInterval(){
        return requestInterval;
    }

}
