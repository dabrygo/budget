package routine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public interface RoutineFile {
    Routine read(Path path) throws FileNotFoundException, IOException;

    void write(Path path, Routine routine) throws IOException;

    final class Csv implements RoutineFile {

        @Override
        public Routine read(final Path path) throws FileNotFoundException, IOException {
            final File file = path.toFile();
            final List<Task> tasks = new ArrayList<>();
            try (final FileReader fileReader = new FileReader(file);
                    final BufferedReader reader = new BufferedReader(fileReader)) {
                // FIXME Assumes header
                String line = reader.readLine();
                while ((line = reader.readLine()) != null) {
                    final String[] fields = line.split(",");
                    final String timeString = fields[0];
                    final Time time = Time.Military.from(timeString);
                    System.out.println(time);

                    final String description = fields[1];
                    final String durationString = fields[2];
                    final int duration = Integer.parseInt(durationString);

                    final Task task = new Task.Duration(time, description, duration);
                    tasks.add(task);
                }
            }
            return new Routine.Default(tasks);
        }

        @Override
        public void write(final Path path, final Routine routine) throws IOException {
            final File file = path.toFile();
            try (final FileWriter fileWriter = new FileWriter(file);
                    final BufferedWriter writer = new BufferedWriter(fileWriter)) {
                writer.write(String.format("start_time,description,duration_minutes%n"));
                for (final Task task : routine) {

                    writer.write(String.format("%s,%s,%d%n", task.start().toString(), task.description(),
                            task.durationMinutes()));
                }
            }
        }

    }
}
