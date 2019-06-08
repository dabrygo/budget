package routine;

import java.util.Scanner;

public class CliView {
    public static void main(final String[] args) {
        final Routine routine = new Routine.Default();
        try (final Scanner scanner = new Scanner(System.in)) {
            for (int i = 0; i < 5; i++) {
                System.out.print("Enter a start time\n>> ");
                final String timeString = input(scanner);
                final Time start = Time.Standard.from(timeString);

                System.out.print("Enter a description\n>> ");
                final String description = input(scanner);

                System.out.print("Enter task duration (in minutes)\n>> ");
                final String durationString = input(scanner);
                final Integer durationMinutes = Integer.parseInt(durationString);

                final Task task = new Task.Duration(start, description, durationMinutes);
                routine.add(task);
                System.out.println(routine);
            }
        }

    }

    private static String input(final Scanner scanner) {
        return scanner.nextLine();
    }
}
