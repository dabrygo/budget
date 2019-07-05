package common;

import java.util.List;

import budget.remake.Budget;
import routine.Routine;

public interface CliMenu<T> {

    String menu();

    T choose(int option) throws Exception;

    int quit();

    public class BudgetMenu implements CliMenu<Budget> {
        private final List<CliOption<Budget>> mOptions;

        public BudgetMenu(final List<CliOption<Budget>> options) {
            mOptions = options;
        }

        @Override
        public String menu() {
            final StringBuilder builder = new StringBuilder();
            for (int i = 0; i < mOptions.size(); i++) {
                final CliOption<?> option = mOptions.get(i);
                final String description = option.description();
                final String line = line(i + 1, description);
                builder.append(line);
            }
            builder.append(">>> ");
            return builder.toString();
        }

        private String line(final int i, final String option) {
            return String.format("%d. %s%n", i, option);
        }

        @Override
        public int quit() {
            return mOptions.size() + 1;
        }

        @Override
        public Budget choose(final int index) throws Exception {
            final CliOption<Budget> option = mOptions.get(index);
            return option.execute();
        }
    }

    final class RoutineMenu implements CliMenu<Routine> {
        private final List<CliOption<Routine>> mOptions;

        public RoutineMenu(final List<CliOption<Routine>> options) {
            mOptions = options;
        }

        @Override
        public String menu() {
            final StringBuilder builder = new StringBuilder();
            for (int i = 0; i < mOptions.size(); i++) {
                final CliOption<?> option = mOptions.get(i);
                final String description = option.description();
                final String line = line(i + 1, description);
                builder.append(line);
            }
            builder.append(">>> ");
            return builder.toString();
        }

        private String line(final int i, final String option) {
            return String.format("%d. %s%n", i, option);
        }

        @Override
        public int quit() {
            return mOptions.size() + 1;
        }

        @Override
        public Routine choose(final int index) throws Exception {
            final CliOption<Routine> option = mOptions.get(index);
            return option.execute();
        }
    }
}
