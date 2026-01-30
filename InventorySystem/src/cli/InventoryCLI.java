package cli;

import dao.UserDAO;
import dao.ItemDAO;
import dao.CategoryDAO;
import model.User;
import model.Item;
import model.Category;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class InventoryCLI {
    private static Scanner sc = new Scanner(System.in);
    private static UserDAO userDAO = new UserDAO();
    private static ItemDAO itemDAO = new ItemDAO();
    private static CategoryDAO categoryDAO = new CategoryDAO();
    private static User loggedInUser;

    public static void start() {
        System.out.println("=== Welcome to Inventory System ===");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.print("Choose option: ");
        int choice = readInt();

        try {
            if (choice == 1) {
                register();
                if (!login()) {
                    System.out.println("Login failed. Exiting...");
                    return;
                }
            } else if (choice == 2) {
                if (!login()) {
                    System.out.println("Login failed. Exiting...");
                    return;
                }
            } else {
                System.out.println("Invalid choice. Exiting...");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Welcome, " + loggedInUser.getFullName() + " (" + loggedInUser.getRole() + ")");
        mainMenu();
    }

    // ---------------- Register ----------------
    private static void register() throws Exception {
        System.out.println("=== Register New User ===");
        System.out.print("Full name: ");
        String fullName = sc.nextLine();
        System.out.print("Username: ");
        String uname = sc.nextLine();
        System.out.print("Password: ");
        String pwd = sc.nextLine();
        System.out.print("Role (storekeeper/customer): ");
        String role = sc.nextLine();

        User u = new User();
        u.setFullName(fullName);
        u.setUsername(uname);
        u.setPassword(pwd);
        u.setRole(role);

        userDAO.addUser(u);
        System.out.println("User registered successfully. Please login.");
    }

    // ---------------- Login ----------------
    private static boolean login() {
        System.out.println("=== Login ===");
        System.out.print("Username: ");
        String uname = sc.nextLine();
        System.out.print("Password: ");
        String pwd = sc.nextLine();

        try {
            loggedInUser = userDAO.authenticate(uname, pwd);
            return loggedInUser != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------------- Main Menu ----------------
    private static void mainMenu() {
        while (true) {
            System.out.println("\n=== Main Menu ===");
            switch (loggedInUser.getRole().toLowerCase()) {
                case "admin" -> {
                    System.out.println("1. Manage Categories");
                    System.out.println("2. View All Items");
                    System.out.println("3. Add Item");
                    System.out.println("4. Manage Users");
                    System.out.println("9. Logout");
                }
                case "storekeeper" -> {
                    System.out.println("1. Manage Categories");
                    System.out.println("2. View My Items");
                    System.out.println("3. Add Item");
                    System.out.println("4. Delete Item");
                    System.out.println("9. Logout");
                }
                case "customer" -> {
                    System.out.println("1. View Categories");
                    System.out.println("2. View Items");
                    System.out.println("9. Logout");
                }
            }

            int choice = readInt();

            try {
                switch (loggedInUser.getRole().toLowerCase()) {
                    case "admin" -> {
                        if (choice == 1) categoryMenu();
                        else if (choice == 2) itemDAO.getAllItems().forEach(System.out::println);
                        else if (choice == 3) addItem();
                        else if (choice == 4) manageUsers();
                        else if (choice == 9) { System.out.println("Logged out."); return; }
                    }
                    case "storekeeper" -> {
                        if (choice == 1) categoryMenu();
                        else if (choice == 2) itemDAO.getItemsByUser(loggedInUser.getUserId()).forEach(System.out::println);
                        else if (choice == 3) addItem();
                        else if(choice ==4) {
                            System.out.println("Enter Item ID: ");
                            int id = readInt();
                            itemDAO.deleteItem(id);
                        }
                        else if (choice == 9) { System.out.println("Logged out."); return;}
                    }
                    case "customer" -> {
                        if (choice == 1) categoryDAO.getAllCategories().forEach(System.out::println);
                        else if (choice == 2) itemDAO.getAllItems().forEach(System.out::println);
                        else if (choice == 9) { System.out.println("Logged out."); return; }
                    }
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    // ---------------- Category Menu ----------------
    private static void categoryMenu() {
        System.out.println("\n--- Category Menu ---");
        System.out.println("1. Add Category");
        System.out.println("2. View Categories");
        System.out.println("3. Delete Category");
        System.out.print("Choose option: ");
        int choice = readInt();

        try {
            switch (choice) {
                case 1 -> {
                    System.out.print("Enter category name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter description: ");
                    String desc = sc.nextLine();
                    Category c = new Category(0, name, desc);
                    categoryDAO.addCategory(c);
                    System.out.println("Category added.");
                }
                case 2 -> categoryDAO.getAllCategories().forEach(System.out::println);
                case 3 -> {
                    System.out.print("Enter category ID to delete: ");
                    int id = readInt();
                    boolean ok = categoryDAO.deleteCategory(id);
                    System.out.println(ok ? "Category deleted." : "Category not found.");
                }
                default -> System.out.println("Invalid choice.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- Add Item ----------------
    private static void addItem() throws Exception {
        System.out.print("Enter item code: ");
        String code = sc.nextLine();
        System.out.print("Enter item name: ");
        String name = sc.nextLine();
        System.out.print("Enter category ID: ");
        int catId = readInt();
        System.out.print("Enter unit: ");
        String unit = sc.nextLine();
        System.out.print("Enter purchase price: ");
        double pp = readDouble();
        System.out.print("Enter sale price: ");
        double sp = readDouble();
        System.out.print("Enter quantity: ");
        double qty = readDouble();
        java.sql.Date expiry = readDateOrNull();

        Item item = new Item();
        item.setItemCode(code);
        item.setItemName(name);
        item.setCategoryId(catId);
        item.setUnit(unit);
        item.setUserId(loggedInUser.getUserId());
        item.setPurchasePrice(pp);
        item.setSalePrice(sp);
        item.setQuantity(qty);
       item.setExpiryDate(expiry);

        itemDAO.addItem(item, loggedInUser.getUserId());
        System.out.println("Item added successfully.");
    }

    // ---------------- Manage Users (Admin Only) ----------------
    private static void manageUsers() {
        System.out.println("=== User Management (Admin) ===");
        System.out.println("Feature placeholder: list/add/delete users.");
        // Extend with UserDAO methods as needed
    }

    // ---------------- Helpers ----------------
    private static int readInt() {
        while (true) {
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (Exception e) { System.out.print("Enter a valid integer: "); }
        }
    }
    private static double readDouble() {
        while (true) {
            try { return Double.parseDouble(sc.nextLine().trim()); }
            catch (Exception e) { System.out.print("Enter a valid number: "); }
        }
    }
    private static java.sql.Date readDateOrNull() {
        System.out.print("Enter date (yyyy-MM-dd) or leave blank: ");
        String input = sc.nextLine().trim();
        if (input.isEmpty()) return null;
        try {
            return java.sql.Date.valueOf(input); // expects yyyy-MM-dd
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid format. Please use yyyy-MM-dd.");
            return readDateOrNull(); // retry
        }
    }

}