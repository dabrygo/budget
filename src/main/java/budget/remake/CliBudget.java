package budget.remake;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import common.CliMenu;
import common.CliOption;

public class CliBudget {

    final static class AddCategory implements CliOption<Budget> {
        private final Budget mBudget;
        private final Scanner mScanner;

        public AddCategory(final Budget budget, final Scanner scanner) {
            mBudget = budget;
            mScanner = scanner;
        }

        @Override
        public Budget execute() throws Exception {
            System.out.print("New category name: ");
            final String name = mScanner.nextLine();

            final String[] categories = name.split("\\.");
            if (categories.length == 1) {
                System.out.print("New category weight: ");
                final int weight = Integer.parseInt(mScanner.nextLine());

                final Category category = new Category.Default(name, weight);
                mBudget.add(category);
                return mBudget;
            } else if (categories.length == 2) {
                final String categoryName = categories[0];
                final Category category = category(categoryName);
                final String subcategoryName = categories[1];

                System.out.print("New category weight: ");
                final int weight = Integer.parseInt(mScanner.nextLine());

                category.subcategorize(weight, subcategoryName);
                return mBudget;
            } else if (categories.length > 2) {
                throw new UnsupportedOperationException("Limited to only one subcategory level");
            }
            return mBudget;
        }

        private Category category(final String name) {
            for (final Category category : mBudget.categories()) {
                if (category.name().equals(name)) {
                    return category;
                }
            }
            throw new IllegalArgumentException("Cannot find category " + name);
        }

        @Override
        public String description() {
            return "Add spending category";
        }
    }

    final static class ViewBudget implements CliOption<Budget> {
        private final Budget mBudget;

        public ViewBudget(final Budget budget) {
            mBudget = budget;
        }

        @Override
        public Budget execute() throws Exception {
            for (final Category category : mBudget.categories()) {
                System.out.println(category.name() + " " + category.weight());
                for (final String subcategoryName : category.subcategories()) {
                    final Category subcategory = category.subcategory(subcategoryName);
                    System.out.println("   " + subcategoryName + " " + subcategory.weight());
                }
            }
            return mBudget;
        }

        @Override
        public String description() {
            return "View budget";
        }
    }

    final static class SaveBudget implements CliOption<Budget> {
        private final Budget mBudget;
        private final Scanner mScanner;

        public SaveBudget(final Budget budget, final Scanner scanner) {
            mBudget = budget;
            mScanner = scanner;
        }

        @Override
        public Budget execute() throws IOException {
            final BudgetFile file = new BudgetFile.Csv();
            System.out.print("Enter filename\n>> ");
            final String input = mScanner.nextLine();
            final Path path = Paths.get("saves", input);
            file.write(path, mBudget);
            return mBudget;
        }

        @Override
        public String description() {
            return "Save budget";
        }
    }

    final static class Quit implements CliOption<Budget> {
        @Override
        public Budget execute() throws IOException {
            System.exit(0);
            return null;
        }

        @Override
        public String description() {
            return "Quit";
        }
    }

    public static void main(final String[] args) throws Exception {
        // final Category budget = new Category.Default("Budget");
        Budget budget = new Budget.Default();

        int option;
        try (final Scanner scanner = new Scanner(System.in)) {
            while (true) {
                final List<CliOption<Budget>> options = new ArrayList<>();
                options.add(new AddCategory(budget, scanner));
                options.add(new ViewBudget(budget));
                options.add(new SaveBudget(budget, scanner));
                // options.add(new LoadTasks(scanner));
                options.add(new Quit());
                final CliMenu<Budget> menu = new CliMenu.BudgetMenu(options);
                System.out.println(menu.menu());
                option = Integer.parseInt(scanner.nextLine());
                scanner.reset();
                budget = menu.choose(option - 1);
            }
        }
    }
}
