package budget.remake;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

interface BudgetFile {
    Budget read(Path path);

    void write(Path path, Budget budget) throws IOException;

    final class Csv implements BudgetFile {

        @Override
        public Budget read(final Path path) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void write(final Path path, final Budget budget) throws IOException {
            try (final BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()))) {
                writer.write(String.format("category,weight%n"));
                for (final Category category : budget.categories()) {
                    writer.write(String.format("%s,%f%n", category.name(), category.weight()));
                    for (final String subcategoryName : category.subcategories()) {
                        final Category subcategory = category.subcategory(subcategoryName);
                        writer.write(
                                String.format("%s.%s,%f%n", category.name(), subcategoryName, subcategory.weight()));
                    }
                }
            }
        }

    }
}
