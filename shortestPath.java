////////////////////
// Dijkstra's Algorithm & Forwarding Table
// Vincent Dufour
// Using Dijkstra's Algorithm to set up a forwarding table based on user-input for # of nodes & weights

import java.io.*;
import java.util.Scanner;


// Driver method
public class shortestPath {
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        int numOfNodes = 0;

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

        // reading from topo.txt
        try
        {
            FileReader topoFile = new FileReader("topo.txt");
            int i;

            while ((i = topoFile.read()) != -1)
            {
                System.out.print((char) i);
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