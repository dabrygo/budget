package routine;

import java.util.ArrayList;
import java.util.List;

public interface Routine {
    void add(Task task);

    final class Default implements Routine {
        private final List<Task> mTasks;

        public Default() {
            this(new ArrayList<Task>());
        }

        public Default(final List<Task> tasks) {
            mTasks = tasks;
        }

        @Override
        public void add(final Task task) {
            final int index = index(task);
            if (index > 0) {
                final int previous = index - 1;
                final Task prevTask = mTasks.get(previous);
                final Time previousEnds = prevTask.finish();
                final Time thisStarts = task.start();
                if (previousEnds.after(thisStarts)) {
                    throw new IllegalArgumentException("Task '" + prevTask.description() + "' occupies "
                            + prevTask.start() + " - " + prevTask.finish() + " but task '" + task.description()
                            + "' occupies " + task.start() + " - " + task.finish());
                }
            }
            mTasks.add(index, task);
        }

        int index(final Task task) {
            final Time start = task.start();
            for (int i = 0; i < mTasks.size(); i++) {
                final Task t = mTasks.get(i);
                final Time tStart = t.start();
                if (start.compareTo(tStart) == 0) {
                    throw new IllegalArgumentException("Task '" + task.description() + "' and task '" + t.description()
                            + "' start at " + start.time());
                } else if (tStart.after(start)) {
                    return i;
                }
            }
            return mTasks.size();
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            for (final Task task : mTasks) {
                final String line = String.format("%s %s (%d min)%n", task.start(), task.description(),
                        task.durationMinutes());
                builder.append(line);
            }
            return builder.toString();
        }

    }
}
