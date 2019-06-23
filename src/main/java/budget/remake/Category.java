package budget.remake;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

interface Category {
    String name();

    double weight();

    void subcategorize(double weight, String subcategory);

    Set<String> subcategories();

    Category subcategory(String name);

    final class Default implements Category {
        private final String mName;
        private final double mWeight;
        private final Map<String, Category> mSubcategories;

        public Default(final String name) {
            this(name, 100, new TreeMap<>());
        }

        public Default(final String name, final double weight) {
            this(name, weight, new TreeMap<>());
        }

        public Default(final String name, final double weight, final Map<String, Category> subcategories) {
            mName = name;
            mWeight = weight;
            mSubcategories = subcategories;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String name() {
            return mName;
        }

        @Override
        public void subcategorize(final double weight, final String subcategoryName) {
            final double unallocated = unallocated();
            final double difference = unallocated - weight;
            if (difference < 0) {
                throw new IllegalArgumentException("Weight " + weight + " exceeds remaining " + unallocated);
            }
            final Category subcategory = new Category.Default(subcategoryName, weight);
            if (mSubcategories.containsKey(subcategoryName)) {
                throw new IllegalArgumentException("Category " + mName + " already has subcategory " + subcategoryName);
            }
            mSubcategories.put(subcategoryName, subcategory);

            final Category unallocatedCategory = Category.unallocated(difference);
            final String unallocatedName = unallocatedCategory.name();
            if ((difference == 0) && mSubcategories.containsKey(unallocatedName)) {
                mSubcategories.remove(unallocatedName);
            }
            mSubcategories.put(unallocatedName, unallocatedCategory);
        }

        double unallocated() {
            final Category unallocatedCategory = Category.unallocated(0.0);
            final String unallocatedName = unallocatedCategory.name();
            if (!mSubcategories.containsKey(unallocatedName)) {
                return mWeight;
            }
            return mSubcategories.get(unallocatedName).weight();
        }

        @Override
        public double weight() {
            return mWeight;
        }

        @Override
        public Set<String> subcategories() {
            return mSubcategories.keySet();
        }

        @Override
        public Category subcategory(final String name) {
            if (!mSubcategories.containsKey(name)) {
                throw new IllegalArgumentException("Could not find " + name + " in category " + mName);
            }
            return mSubcategories.get(name);
        }
    }

    static Category unallocated(final double weight) {
        return new Category.Default("Unallocated", weight);
    }
}
