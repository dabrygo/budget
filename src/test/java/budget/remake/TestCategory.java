package budget.remake;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TestCategory {

    @Nested
    final class TestDefault {
        @Test
        void name() {
            final Category category = new Category.Default("Discretionary");
            assertEquals("Discretionary", category.name());
        }

        @Test
        void unallocated() {
            final Category.Default category = new Category.Default("Giving", 12);
            final double unallocated = category.unallocated();
            assertEquals(12, unallocated, 1e-8);
        }

        @Test
        void subcategories() {
            final Category.Default category = new Category.Default("Giving", 12);
            category.subcategorize(10, "Tithe");
            final Category subcategory = category.subcategory("Tithe");
            assertEquals(10, subcategory.weight());
        }

        @Test
        void unallocatedSubcategory() {
            final Category.Default category = new Category.Default("Giving", 12);
            category.subcategorize(10, "Tithe");
            final String name = unallocatedName();
            assertEquals(2, category.subcategory(name).weight());
        }

        private String unallocatedName() {
            final Category unallocated = Category.unallocated(0.0);
            final String name = unallocated.name();
            return name;
        }
    }

}
