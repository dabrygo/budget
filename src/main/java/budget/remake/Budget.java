package budget.remake;

import java.util.ArrayList;
import java.util.List;

public interface Budget {
    List<Category> categories();

    void add(Category category);

    final class Default implements Budget {
        private final List<Category> mCategories;

        public Default() {
            this(new ArrayList<>());
        }

        public Default(final List<Category> categories) {
            mCategories = categories;
        }

        @Override
        public List<Category> categories() {
            return mCategories;
        }

        @Override
        public void add(final Category category) {
            mCategories.add(category);
        }

    }
}
