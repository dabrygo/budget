package budget.remake;

/**
 * Legal tender to be stored or traded for services.
 *
 * @author dbgod
 *
 */
interface Money {

    /**
     * @return This money in terms of currency units.
     */
    long cents();

    /**
     * Accrete this money with some other money.
     *
     * @param money
     *            Quantity to add to this money.
     * @return Total of this money with other money.
     */
    Money add(Money money);

    /**
     * Remove quantity from this money.
     *
     * @param money
     *            Quantity to remove.
     * @return
     */
    Money subtract(Money money);

    /**
     * @return Displays money in $X.YY format, where X is the number of dollars and
     *         YY is the number of cents.
     */
    String standardFormat();

    final class Default implements Money {
        private final long mCents;

        public Default(final long cents) {
            mCents = cents;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public long cents() {
            return mCents;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Money add(final Money money) {
            final long cents = money.cents();
            final long sum = mCents + cents;
            return new Money.Default(sum);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Money subtract(final Money money) {
            final long cents = money.cents();
            final long difference = mCents - cents;
            return new Money.Default(difference);
        }

        @Override
        public String standardFormat() {
            final long cents = mCents % 100;
            final long dollars = (mCents - cents) / 100;
            return String.format("$%d.%02d", dollars, cents);
        }

        @Override
        public int hashCode() {
            return ((Long) mCents).hashCode();
        }

        @Override
        public boolean equals(final Object obj) {
            if (!(obj instanceof Money.Default)) {
                return false;
            }
            final Money.Default that = (Money.Default) obj;
            return mCents == that.mCents;
        }
    }

    /**
     * @param cents
     *            Currency units in this {@link Money}.
     * @return Instance of {@link Money} for testing.
     */
    static Money fake(final long cents) {
        return new Money.Default(cents);
    }

    /**
     * @param cents
     *            Currency units in this {@link Money}.
     * @return Instance of {@link Money} from its currency units.
     */
    static Money cents(final long cents) {
        return new Money.Default(cents);
    }

    /**
     * @param dollars
     *            TODO
     * @param cents
     *            Currency units in this {@link Money}.
     * @return Instance of {@link Money} from its currency units.
     */
    static Money dollars(final long dollars, final long cents) {
        if (cents < 0) {
            throw new IllegalArgumentException("Expected at least 0 cents but got " + cents);
        }
        if (cents >= 100) {
            throw new IllegalArgumentException("Expected fewer than 100 cents but got " + cents);
        }
        final long maxDollars = (Long.MAX_VALUE / 100) - 100;
        if (dollars >= maxDollars) {
            throw new IllegalStateException(dollars + " exceeds maximum value of ");
        }
        final long dollarsInCents = dollars * 100;
        return new Money.Default(dollarsInCents + cents);
    }
}
