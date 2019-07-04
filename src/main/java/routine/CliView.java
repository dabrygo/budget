package routine;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import common.CliMenu;
import common.CliOption;

public class CliView {
    final static class AddTask implements CliOption<Routine> {
        private final Routine mRoutine;
        private final Scanner mScanner;

        public AddTask(final Routine routine, final Scanner scanner) {
            mRoutine = routine;
            mScanner = scanner;
        }

        @Override
        public Routine execute() {
            System.out.print("Enter a start time\n>> ");
            final String timeString = mScanner.nextLine();
            final Time start = Time.Standard.from(timeString);

            System.out.print("Enter a description\n>> ");
            final String description = mScanner.nextLine();
            // TODO Check if comma in description

            System.out.print("Enter task duration (in minutes)\n>> ");
            final String durationString = mScanner.nextLine();
            final Integer durationMinutes = Integer.parseInt(durationString);

            final Task task = new Task.Duration(start, description, durationMinutes);
            mRoutine.add(task);
            return mRoutine;
        }

        @Override
        public String description() {
            return "Add task";
        }
    }

    final static class ViewTasks implements CliOption<Routine> {
        private final Routine mRoutine;

        public ViewTasks(final Routine routine) {
            mRoutine = routine;
        }

        @Override
        public Routine execute() {
            System.out.println(mRoutine);
            return mRoutine;
        }

        @Override
        public String description() {
            return "View tasks";
        }
    }

    final static class SaveTasks implements CliOption<Routine> {
        private final Routine mRoutine;
        private final Scanner mScanner;

        public SaveTasks(final Routine routine, final Scanner scanner) {
            mRoutine = routine;
            mScanner = scanner;
        }

        @Override
        public Routine execute() throws IOException {
            final RoutineFile file = new RoutineFile.Csv();
            System.out.print("Enter filename\n>> ");
            final String input = mScanner.nextLine();
            final Path path = Paths.get("saves", input);
            file.write(path, mRoutine);
            return mRoutine;
        }

        @Override
        public String description() {
            return "Save tasks";
        }
    }

    final static class LoadTasks implements CliOption<Routine> {
        private final Scanner mScanner;

        public LoadTasks(final Scanner scanner) {
            mScanner = scanner;
        }

        @Override
        public Routine execute() throws IOException {
            final RoutineFile file = new RoutineFile.Csv();
            System.out.print("Enter filename\n>> ");
            final String input = mScanner.nextLine();
            final Path path = Paths.get("saves", input);
            return file.read(path);
        }

        @Override
        public String description() {
            return "Load tasks";
        }
    }

    final static class Quit implements CliOption<Routine> {
        @Override
        public Routine execute() throws IOException {
            System.exit(0);
            return null;
        }

        @Override
        public String description() {
            return "Quit";
        }
    }

    public static void main(final String[] args) throws Exception {
        Routine routine = new Routine.Default();
        try (final Scanner scanner = new Scanner(System.in)) {
            int option;
            do {
                final List<CliOption<Routine>> options = new ArrayList<>();
                options.add(new AddTask(routine, scanner));
                options.add(new ViewTasks(routine));
                options.add(new SaveTasks(routine, scanner));
                options.add(new LoadTasks(scanner));
                options.add(new Quit());
                final CliMenu<Routine> menu = new CliMenu.RoutineMenu(options);
                System.out.println(menu.menu());
                option = Integer.parseInt(scanner.nextLine());
                scanner.reset();
                routine = menu.choose(option - 1);
            } while (true);
        }
    }
}
