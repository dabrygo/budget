package common;

import java.util.Arrays;
import java.util.List;

public interface CliMenu {
    String menu();

    int quit();

    final class Default implements CliMenu {
        private final List<String> mOptions;

        public Default(final String... options) {
            this(Arrays.asList(options));
        }

        public Default(final List<String> options) {
            mOptions = options;
        }

        @Override
        public String menu() {
            final StringBuilder builder = new StringBuilder();
            for (int i = 0; i < mOptions.size(); i++) {
                final String option = mOptions.get(i);
                final String line = line(i + 1, option);
                builder.append(line);
            }
            final String quitLine = line(quit(), "Quit");
            builder.append(quitLine);
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
    }
}
