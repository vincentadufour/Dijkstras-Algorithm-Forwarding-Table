////////////////////
// Dijkstra's Algorithm & Forwarding Table
// Vincent Dufour
// Using Dijkstra's Algorithm to set up a forwarding table based on user-input for # of nodes & weights
////////////////////

import java.io.*;
import java.util.*;

// Driver method
public class shortestPath {
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        int numOfNodes = 0;
        String route;
        int lineNumber = 0;

        // receiving number of nodes with user input authentication
        while(numOfNodes < 2)
        {
            System.out.println("Please enter a number of nodes (minimum 2): ");


            if (scanner.hasNextInt())
            {
                numOfNodes = scanner.nextInt();
                
                if (numOfNodes < 2)
                {
                    System.out.println("\nInvalid number of nodes: Must be minimum 2.");
                }
            } else
            {
                System.out.println("\nThat isn't a number. Please enter a number.");
                scanner.next();
            }
        }

        // try matrix creation & shortest path if topo.txt is valid
        try
        {
            BufferedReader topoFile = new BufferedReader(new FileReader("../topo.txt"));

            // create costMatrix
            int[][] costMatrix = new int[numOfNodes][numOfNodes];

            // initialize costMatrix to be 0's along diagonal and MAX_VALUE everywhere else
            for (int i = 0; i < numOfNodes; i++)
            {
                for (int j = 0; j < numOfNodes; j++)
                {
                    if (i == j)
                    {
                        costMatrix[i][j] = 0;
                    } else
                    {
                        costMatrix[i][j] = Integer.MAX_VALUE;
                    }
                }
            }

            // reading from topo.txt and putting into costMatrix
            while (((route = topoFile.readLine()) != null) || lineNumber < numOfNodes )
            {
                lineNumber++;

                // splits by whitespace (tab wasn't working for me)
                String[] nodesAndWeights = route.split("\\s+");

                // source router
                int sourceRouter = Integer.parseInt(nodesAndWeights[0]);

                //destination router
                int destinationRouter = Integer.parseInt(nodesAndWeights[1]);

                // link weight
                int linkWeight = Integer.parseInt(nodesAndWeights[2]);

                // validate source and destination routers
                if (sourceRouter < 0 || sourceRouter > (numOfNodes - 1) || destinationRouter < 0 || destinationRouter > (numOfNodes - 1))               
                {
                    System.out.println("Invalid router on line " + lineNumber + "!");
                    continue;
                }

                // validate link weight
                if (linkWeight <= 0)
                {
                    System.out.println("Invalid link weight on line " + lineNumber + "!");
                    continue;
                }

                // add route to matrix
                costMatrix[sourceRouter][destinationRouter] = linkWeight;
                costMatrix[destinationRouter][sourceRouter] = linkWeight;
                System.out.println("Route added from router " + sourceRouter + " to router " + destinationRouter + " with link weight " + linkWeight + ".");
            }


            // print formatted final cost matrix
            System.out.println("\nFinal Cost Matrix:\n");
            for (int i = 0; i < numOfNodes; i++)
            {
                for (int j = 0; j < numOfNodes; j++)
                {
                    if (costMatrix[i][j] == Integer.MAX_VALUE)
                    {
                        System.out.printf("%6s ", "INF");
                    } else
                    {
                        System.out.printf("%6d ", costMatrix[i][j]);
                    }
                }
                System.out.println();
            }
            System.out.println();

            // Dijkstra's Algorithm Initialization
            Set<Integer> nPrime = new HashSet<>(); // Set N'
            int[] distanceMatrix = new int[numOfNodes];
            int[] predecessorMatrix = new int[numOfNodes];
            Arrays.fill(distanceMatrix, Integer.MAX_VALUE);
            Arrays.fill(predecessorMatrix, -1);

            int source = 0;
            distanceMatrix[source] = 0;

            // Dijkstra's Algorithm Loop
            while (nPrime.size() < numOfNodes)
            {
                // Find the node with the minimum distance not in N'
                int minDistance = Integer.MAX_VALUE;
                int minNode = -1;

                for (int i = 0; i < numOfNodes; i++)
                {
                    if (!nPrime.contains(i) && distanceMatrix[i] < minDistance)
                    {
                        minDistance = distanceMatrix[i];
                        minNode = i;
                    }
                }

                // if minNode is -1, there's no connected node left
                if (minNode == -1)
                {
                    break; 
                }

                nPrime.add(minNode);

                // update distance and predecessor for adjacent nodes
                for (int i = 0; i < numOfNodes; i++)
                {
                    if (!nPrime.contains(i) && costMatrix[minNode][i] != Integer.MAX_VALUE)
                    {
                        int newDistance = distanceMatrix[minNode] + costMatrix[minNode][i];

                        if (newDistance < distanceMatrix[i])
                        {
                            distanceMatrix[i] = newDistance;
                            predecessorMatrix[i] = minNode;
                        }
                    }
                }

                // print algorithm iterations
                System.out.println("\nIteration:");
                System.out.println("N': " + nPrime);
                System.out.println("Distance Vector: " + Arrays.toString(distanceMatrix));
                System.out.println("Predecessor Vector: " + Arrays.toString(predecessorMatrix));
            }

            // Constructing the Forwarding Table
            System.out.println("\nForwarding Table:");
            System.out.printf("%-12s %-20s%n", "Destination", "Link");

            for (int i = 1; i < numOfNodes; i++)
            {
                int destination = i;
                int nextHop = destination;

                // Find the immediate next hop from the source
                while (predecessorMatrix[nextHop] != source && predecessorMatrix[nextHop] != -1)
                {
                    nextHop = predecessorMatrix[nextHop];
                }

                String link = "(V" + source + ", V" + nextHop + ")";
                System.out.printf("%-12s %-20s%n", "V" + destination, link);
            }

            topoFile.close();
        } catch (FileNotFoundException e)
        {
            System.out.println("File not found.");
        } catch (IOException e)
        {
            System.out.println("Error occured.");
        }
    }
}