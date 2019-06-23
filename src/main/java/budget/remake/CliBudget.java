package budget.remake;

import java.util.Scanner;

public class CliBudget {
    public static void main(final String[] args) {
        final Category budget = new Category.Default("Budget");

        final Category category = budget;
        int option;
        try (final Scanner scanner = new Scanner(System.in)) {
            do {
                System.out.println("1. Add new category");
                System.out.println("2. Add new subcategory");
                System.out.println("3. Display budget");
                System.out.println("4. Exit");
                System.out.print(">>> ");
                option = scanner.nextInt();
                if (option == 1) {
                    System.out.print("New category name: ");
                    final String name = scanner.next();

                    System.out.print("New category weight: ");
                    final int weight = scanner.nextInt();
                    category.subcategorize(weight, name);
                } else if (option == 2) {
                    System.out.print("Existing category name: ");
                    final String name = scanner.next();
                    final Category subcategory = budget.subcategory(name);

                    System.out.print("New subcategory name: ");
                    final String subcategoryName = scanner.next();

                    System.out.print("New subcategory weight: ");
                    final int weight = scanner.nextInt();
                    subcategory.subcategorize(weight, subcategoryName);
                } else if (option == 3) {
                    display("", budget);
                } else {
                    throw new IllegalArgumentException("Unknown option " + option);
                }
            } while (option != 4);
        }
    }

    private static void display(final String indent, final Category budget) {
        for (final String name : budget.subcategories()) {
            final Category subcategory = budget.subcategory(name);
            if (subcategory.weight() == 0) {
                return;
            }
            System.out.println(indent + name + " " + subcategory.weight());
            if (!subcategory.subcategories().isEmpty()) {
                display(indent + "  ", subcategory);
            }
        }
    }
}
