package routine;

interface Task {
    Time start();

    Time finish();

    int durationMinutes();

    String description();

    public class Fake implements Task {
        private final int mStart;
        private final int mFinish;
        private final int mDurationMinutes;
        private final String mDescription;

        private Fake(final int start, final int finish, final int durationMinutes, final String description) {
            mStart = start;
            mFinish = finish;
            mDurationMinutes = durationMinutes;
            mDescription = description;
        }

        public static Task duration(final int start, final int durationMinutes, final String description) {
            return new Task.Fake(start, start + durationMinutes, durationMinutes, description);
        }

        @Override
        public Time start() {
            return new Time.Fake(mStart);
        }

        @Override
        public Time finish() {
            return new Time.Fake(mFinish);
        }

        @Override
        public int durationMinutes() {
            return mDurationMinutes;
        }

        @Override
        public String description() {
            return mDescription;
        }

    }

    final class Duration implements Task {
        private final Time mStart;
        private final String mDescription;
        private final int mDurationMinutes;

        public Duration(final Time start, final String description, final int durationMinutes) {
            mStart = start;
            mDescription = description;
            mDurationMinutes = durationMinutes;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Time start() {
            return mStart;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Time finish() {
            return mStart.plusMinutes(mDurationMinutes);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int durationMinutes() {
            return mDurationMinutes;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String description() {
            return mDescription;
        }
    }
}
