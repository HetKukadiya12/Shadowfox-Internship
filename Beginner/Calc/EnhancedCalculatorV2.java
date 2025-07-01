package Beginner;

import java.util.Scanner;

public class EnhancedCalculatorV2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n--- Enhanced Calculator V2 ---");
            System.out.println("1. Arithmetic | 2. Scientific | 3. Convert | 4. Exit");
            System.out.print("Choose option: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> {
                    System.out.println("1. + 2. - 3. * 4. /");
                    int op = sc.nextInt();
                    System.out.print("Enter two numbers: ");
                    double a = sc.nextDouble(), b = sc.nextDouble();
                    switch (op) {
                        case 1 -> System.out.println("Sum: " + (a + b));
                        case 2 -> System.out.println("Diff: " + (a - b));
                        case 3 -> System.out.println("Prod: " + (a * b));
                        case 4 -> System.out.println(b != 0 ? "Div: " + (a / b) : "Error: Div by 0");
                        default -> System.out.println("Invalid");
                    }
                }
                case 2 -> {
                    System.out.println("1. √x | 2. x^y");
                    int op = sc.nextInt();
                    if (op == 1) {
                        System.out.print("Enter number: ");
                        double x = sc.nextDouble();
                        System.out.println(x >= 0 ? "√" + x + " = " + Math.sqrt(x) : "Error: Negative");
                    } else if (op == 2) {
                        System.out.print("Enter base and exponent: ");
                        double base = sc.nextDouble(), exp = sc.nextDouble();
                        System.out.println("Result: " + Math.pow(base, exp));
                    } else System.out.println("Invalid");
                }
                case 3 -> {
                    System.out.println("1. Temp | 2. Currency");
                    int conv = sc.nextInt();
                    if (conv == 1) {
                        System.out.println("1. C->F | 2. F->C");
                        int op = sc.nextInt();
                        System.out.print("Enter temp: ");
                        double t = sc.nextDouble();
                        System.out.println(op == 1 ? (t * 9 / 5 + 32) + "°F" : (t - 32) * 5 / 9 + "°C");
                    } else if (conv == 2) {
                        double usdToInr = 83.5;
                        System.out.println("1. USD->INR | 2. INR->USD");
                        int op = sc.nextInt();
                        System.out.print("Enter amount: ");
                        double amt = sc.nextDouble();
                        System.out.println(op == 1 ? "₹" + (amt * usdToInr) : "$" + (amt / usdToInr));
                    } else System.out.println("Invalid");
                }
                case 4 -> exit = true;
                default -> System.out.println("Invalid");
            }
        }
        sc.close();
        System.out.println("Calculator Closed.");
    }
}
