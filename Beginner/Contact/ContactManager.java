package Beginner;
import java.util.ArrayList;
import java.util.Scanner;

public class ContactManager {
    private ArrayList<Contact> contacts;
    private Scanner scanner;

    public ContactManager() {
        contacts = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    public void start() {
        boolean exit = false;
        while (!exit) {
            showMenu();
            int choice = readInt();

            switch (choice) {
                case 1 -> addContact();
                case 2 -> viewContacts();
                case 3 -> updateContact();
                case 4 -> deleteContact();
                case 5 -> {
                    System.out.println("Exiting... Thank you!");
                    exit = true;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void showMenu() {
        System.out.println("\n--- Contact Management System ---");
        System.out.println("1. Add Contact");
        System.out.println("2. View Contacts");
        System.out.println("3. Update Contact");
        System.out.println("4. Delete Contact");
        System.out.println("5. Exit");
        System.out.print("Select an option: ");
    }

    private void addContact() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter phone: ");
        String phone = scanner.nextLine().trim();

        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();

        Contact contact = new Contact(name, phone, email);
        contacts.add(contact);
        System.out.println("Contact added successfully.");
    }

    private void viewContacts() {
        if (contacts.isEmpty()) {
            System.out.println("No contacts found.");
            return;
        }

        System.out.println("\n--- Contact List ---");
        for (int i = 0; i < contacts.size(); i++) {
            System.out.println((i + 1) + ". " + contacts.get(i));
        }
    }

    private void updateContact() {
        if (contacts.isEmpty()) {
            System.out.println("No contacts to update.");
            return;
        }

        viewContacts();
        System.out.print("Enter contact number to update: ");
        int index = readInt() - 1;

        if (index < 0 || index >= contacts.size()) {
            System.out.println("Invalid contact number.");
            return;
        }

        Contact contact = contacts.get(index);

        System.out.print("Enter new name (leave blank to keep \"" + contact.getName() + "\"): ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) contact.setName(name);

        System.out.print("Enter new phone (leave blank to keep \"" + contact.getPhone() + "\"): ");
        String phone = scanner.nextLine().trim();
        if (!phone.isEmpty()) contact.setPhone(phone);

        System.out.print("Enter new email (leave blank to keep \"" + contact.getEmail() + "\"): ");
        String email = scanner.nextLine().trim();
        if (!email.isEmpty()) contact.setEmail(email);

        System.out.println("Contact updated successfully.");
    }

    private void deleteContact() {
        if (contacts.isEmpty()) {
            System.out.println("No contacts to delete.");
            return;
        }

        viewContacts();
        System.out.print("Enter contact number to delete: ");
        int index = readInt() - 1;

        if (index < 0 || index >= contacts.size()) {
            System.out.println("Invalid contact number.");
            return;
        }

        contacts.remove(index);
        System.out.println("Contact deleted successfully.");
    }

    private int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Enter a number: ");
            }
        }
    }

    public static void main(String[] args) {
        ContactManager app = new ContactManager();
        app.start();
    }
}
