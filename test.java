import java.util.Scanner;

public class test {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numOfNodes = 0;

        while (numOfNodes < 2) {
            System.out.print("Please enter the number of nodes (minimum 2): ");
            if (scanner.hasNextInt()) {
                numOfNodes = scanner.nextInt();
                if (numOfNodes < 2) {
                    System.out.println("Invalid input. The number of nodes must be 2 or more.");
                }
            } else {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next(); // Clear the invalid input
            }
        }

        System.out.println("You entered a valid number of nodes: " + numOfNodes);

        scanner.close();
    }
}
