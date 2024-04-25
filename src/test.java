////////////////////
// Dijkstra's Algorithm & Forwarding Table
// Vincent Dufour
// Using Dijkstra's Algorithm to set up a forwarding table based on user-input for # of nodes & weights
////////////////////

import java.io.*;
import java.util.*;

public class test {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numOfNodes = 0;
        String route;
        int lineNumber = 0;

        // Getting number of nodes with user input authentication
        while (numOfNodes < 2) {
            System.out.println("Please enter a number of nodes (minimum 2): ");

            if (scanner.hasNextInt()) {
                numOfNodes = scanner.nextInt();

                if (numOfNodes < 2) {
                    System.out.println("\nInvalid number of nodes: Must be minimum 2.");
                }
            } else {
                System.out.println("\nThat isn't a number. Please enter a number.");
                scanner.next();
            }
        }

        try {
            BufferedReader topoFile = new BufferedReader(new FileReader("../topo.txt"));

            // Create cost matrix
            int[][] costMatrix = new int[numOfNodes][numOfNodes];

            // Initialize cost matrix with infinity except for diagonal
            for (int i = 0; i < numOfNodes; i++) {
                for (int j = 0; j < numOfNodes; j++) {
                    if (i == j) {
                        costMatrix[i][j] = 0;
                    } else {
                        costMatrix[i][j] = Integer.MAX_VALUE;
                    }
                }
            }

            // Reading from topo.txt and adding to cost matrix
            while ((route = topoFile.readLine()) != null) {
                lineNumber++;

                // Splitting by whitespace (tabs)
                String[] nodesAndWeights = route.split("\\s+");

                // Parse the nodes and weights
                int sourceRouter = Integer.parseInt(nodesAndWeights[0]);
                int destinationRouter = Integer.parseInt(nodesAndWeights[1]);
                int linkWeight = Integer.parseInt(nodesAndWeights[2]);

                // Validate nodes and link weight
                if (sourceRouter < 0 || sourceRouter >= numOfNodes || destinationRouter < 0 || destinationRouter >= numOfNodes || linkWeight <= 0) {
                    System.out.println("Invalid input on line " + lineNumber + ".");
                    continue;
                }

                costMatrix[sourceRouter][destinationRouter] = linkWeight;
                costMatrix[destinationRouter][sourceRouter] = linkWeight;
                System.out.println("Added route: " + sourceRouter + " to " + destinationRouter + " with link weight " + linkWeight);
            }

            // Print cost matrix for verification
            System.out.println("\nFinal Cost Matrix:");
            for (int i = 0; i < numOfNodes; i++) {
                for (int j = 0; j < numOfNodes; j++) {
                    System.out.printf("%6s ", costMatrix[i][j] == Integer.MAX_VALUE ? "INF" : Integer.toString(costMatrix[i][j]));
                }
                System.out.println();
            }

            // Initialize Dijkstra's Algorithm
            Set<Integer> nPrime = new HashSet<>(); // Set N'
            int[] distanceMatrix = new int[numOfNodes];
            int[] predecessorMatrix = new int[numOfNodes];
            Arrays.fill(distanceMatrix, Integer.MAX_VALUE);
            Arrays.fill(predecessorMatrix, -1);

            int source = 0; // Assuming source is router 0
            distanceMatrix[source] = 0; // Distance from source to source is 0

            // Dijkstra's Algorithm main loop
            while (nPrime.size() < numOfNodes) {
                // Find the node with the minimum distance not in N'
                int minDistance = Integer.MAX_VALUE;
                int minNode = -1;

                for (int i = 0; i < numOfNodes; i++) {
                    if (!nPrime.contains(i) && distanceMatrix[i] < minDistance) {
                        minDistance = distanceMatrix[i];
                        minNode = i;
                    }
                }

                // Add minNode to N'
                if (minNode == -1) {
                    break; // If minNode is -1, there's no connected node left
                }

                nPrime.add(minNode);

                // Update distance and predecessor for adjacent nodes
                for (int i = 0; i < numOfNodes; i++) {
                    if (!nPrime.contains(i) && costMatrix[minNode][i] != Integer.MAX_VALUE) {
                        int newDistance = distanceMatrix[minNode] + costMatrix[minNode][i];
                        if (newDistance < distanceMatrix[i]) {
                            distanceMatrix[i] = newDistance;
                            predecessorMatrix[i] = minNode;
                        }
                    }
                }
            }

            // Constructing the Forwarding Table
            System.out.println("\nForwarding Table:");
            System.out.printf("%-12s %-20s%n", "Destination", "Link");

            for (int i = 1; i < numOfNodes; i++) {
                int destination = i;
                int nextHop = destination;

                // Find the immediate next hop from the source
                while (predecessorMatrix[nextHop] != source && predecessorMatrix[nextHop] != -1) {
                    nextHop = predecessorMatrix[nextHop];
                }

                String link = "(V" + source + ", V" + nextHop + ")";
                System.out.printf("%-12s %-20s%n", "V" + destination, link);
            }

            topoFile.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        } catch (IOException e) {
            System.out.println("Error occurred.");
        }
    }
}
