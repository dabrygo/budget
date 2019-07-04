package routine;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import common.CliMenu;

public class CliView {
    public static void main(final String[] args) throws IOException {
        final Routine routine = new Routine.Default();
        final CliMenu menu = new CliMenu.Default("Add task", "View tasks", "Save tasks");
        try (final Scanner scanner = new Scanner(System.in)) {
            int option;
            do {
                System.out.println(menu.menu());
                option = Integer.parseInt(scanner.nextLine());
                scanner.reset();
                if (option == 1) {
                    System.out.print("Enter a start time\n>> ");
                    final String timeString = scanner.nextLine();
                    final Time start = Time.Standard.from(timeString);

                    System.out.print("Enter a description\n>> ");
                    final String description = scanner.nextLine();

                    System.out.print("Enter task duration (in minutes)\n>> ");
                    final String durationString = scanner.nextLine();
                    final Integer durationMinutes = Integer.parseInt(durationString);

                    final Task task = new Task.Duration(start, description, durationMinutes);
                    routine.add(task);
                } else if (option == 2) {
                    System.out.println(routine);
                } else if (option == 3) {
                    System.out.print("Enter filename\n>> ");
                    final String input = scanner.nextLine();
                    final Path path = Paths.get("saves", input);
                    routine.save(path);
                }
            } while (option != menu.quit());
        }
    }
}
