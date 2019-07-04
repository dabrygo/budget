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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                    final Time time = time(timeString);
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

        private Time time(final String line) throws IOException {
            final Pattern pattern = Pattern.compile("(\\d+):(\\d+) (\\w{2})");
            final Matcher matcher = pattern.matcher(line);
            final boolean found = matcher.find();
            if (!found) {
                throw new IOException("Could not parse " + line);
            }
            final int hour = Integer.parseInt(matcher.group(1));
            final int minute = Integer.parseInt(matcher.group(2));

            final String readState = matcher.group(3);
            final Time.Standard.State state;
            if (readState.equals("AM")) {
                state = Time.Standard.State.AM;
            } else if (readState.equals("PM")) {
                state = Time.Standard.State.PM;
            } else {
                throw new IOException("Could not parse " + readState);
            }
            final Time time = new Time.Standard(hour, minute, state);
            return time;
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
