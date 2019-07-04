package routine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

interface Time extends Comparable<Time> {
    String time();

    default boolean after(final Time time) {
        return compareTo(time) > 0;
    }

    default boolean before(final Time time) {
        return compareTo(time) < 0;
    }

    Time plusMinutes(int minutes);

    public class Fake implements Time {
        private final int mTime;

        public Fake(final int time) {
            mTime = time;
        }

        @Override
        public int compareTo(final Time time) {
            if (!(time instanceof Time.Fake)) {
                throw new IllegalArgumentException(String.format("Cannot compare %s with %s", this, time));
            }
            final Time.Fake that = (Time.Fake) time;
            if (mTime < that.mTime) {
                return -1;
            } else if (mTime > that.mTime) {
                return 1;
            } else {
                return 0;
            }
        }

        @Override
        public String time() {
            return Integer.toString(mTime);
        }

        @Override
        public Time plusMinutes(final int minutes) {
            return new Time.Fake(mTime + minutes);
        }

        @Override
        public boolean before(final Time time) {
            // TODO Auto-generated method stub
            return false;
        }
    }

    final class Military implements Time {
        private final int mHour;
        private final int mMinute;

        public Military(final int hour, final int minute) {
            mHour = hour;
            mMinute = minute;
        }

        public static Time from(final String string) {
            final Pattern pattern = Pattern.compile("([012]?\\d):([0-5]\\d)");
            final Matcher matcher = pattern.matcher(string);
            final boolean found = matcher.find();
            if (!found) {
                throw new IllegalArgumentException("Could not parse time '" + string);
            }
            final String hourString = matcher.group(1);
            final Integer hour = Integer.parseInt(hourString);
            final String minuteString = matcher.group(2);
            final Integer minute = Integer.parseInt(minuteString);
            return new Time.Military(hour, minute);
        }

        @Override
        public int compareTo(final Time time) {
            if (!(time instanceof Time.Military)) {
                throw new IllegalArgumentException(String.format("Cannot compare %s with %s", this, time));
            }
            final Time.Military that = (Time.Military) time;
            if (mHour > that.mHour) {
                return 1;
            } else if (mHour < that.mHour) {
                return -1;
            } else if (mMinute > that.mMinute) {
                return 1;
            } else if (mMinute < that.mMinute) {
                return -1;
            } else {
                return 0;
            }
        }

        @Override
        public String time() {
            return String.format("%02d:%02d", mHour, mMinute);
        }

        @Override
        public Time plusMinutes(final int minutes) {
            final int minuteDifference = mMinute + minutes;
            final int hourDifference = minuteDifference / 60;
            final int newMinute = minuteDifference % 60;
            final int newHour = mHour + hourDifference;
            if ((newHour + hourDifference) > 23) {
                throw new IllegalStateException(String
                        .format("%d minutes past %s crosses days, which is not currently supported", minutes, time()));

            }
            return new Time.Military(newHour, newMinute);
        }

        @Override
        public boolean equals(final Object obj) {
            if (!(obj instanceof Time.Military)) {
                return false;
            }
            final Time.Military that = (Time.Military) obj;
            return (mHour == that.mHour) && (mMinute == that.mMinute);
        }

        @Override
        public String toString() {
            return time();
        }
    }

    final class Standard implements Time {
        public enum State {
            AM, PM
        }

        private final int mHour;
        private final int mMinute;
        private final State mState;

        public Standard(final int hour, final int minute, final State state) {
            mHour = hour;
            mMinute = minute;
            mState = state;
        }

        public static Time from(final String string) {
            final Pattern pattern = Pattern.compile("([01]?\\d):([0-5]\\d)\\s*(AM|PM)?");
            final Matcher matcher = pattern.matcher(string);
            final boolean found = matcher.find();
            if (!found) {
                throw new IllegalArgumentException("Could not parse time '" + string);
            }
            final String hourString = matcher.group(1);
            final Integer hour = Integer.parseInt(hourString);
            final String minuteString = matcher.group(2);
            final Integer minute = Integer.parseInt(minuteString);
            final String stateString = matcher.group(3);
            final State state = state(hour, stateString);
            return new Time.Standard(hour, minute, state);
        }

        private static State state(final int hour, final String stateString) {
            if ((stateString == null)) {
                if ((0 <= hour) && (hour < 12)) {
                    return State.AM;
                } else if ((12 <= hour) && (hour <= 23)) {
                    return State.PM;
                } else {
                    throw new IllegalArgumentException("Cannot find state for hour " + hour);
                }
            } else if (stateString.equals(State.AM.toString())) {
                return State.AM;
            } else if (stateString.equals(State.PM.toString())) {
                return State.PM;
            } else {
                throw new IllegalArgumentException("Could not match " + stateString + " to AM or PM");
            }
        }

        @Override
        public String time() {
            return String.format("%02d:%02d %s", mHour, mMinute, mState.toString());
        }

        @Override
        public boolean equals(final Object obj) {
            if (!(obj instanceof Standard)) {
                return false;
            }
            final Standard that = (Standard) obj;
            return (mHour == that.mHour) && (mMinute == that.mMinute);
        }

        @Override
        public Time plusMinutes(final int minutes) {
            final int minuteDifference = mMinute + minutes;
            final int hourDifference = minuteDifference / 60;
            final int newMinute = minuteDifference % 60;
            final int newHour = mHour + hourDifference;
            final State state;
            if ((newHour + hourDifference) > 23) {
                if (mState.equals(State.PM)) {
                    throw new IllegalStateException(String.format(
                            "%d minutes past %s crosses days, which is not currently supported", minutes, time()));
                } else {
                    state = State.PM;
                }
            } else {
                state = mState;
            }
            return new Time.Standard(newHour, newMinute, state);
        }

        @Override
        public int compareTo(final Time time) {
            if (!(time instanceof Time.Standard)) {
                throw new IllegalArgumentException(String.format("Cannot compare %s with %s", this, time));
            }
            final Time.Standard that = (Time.Standard) time;
            if ((that.mState == State.AM) && (mState == State.PM)) {
                return 1;
            } else if ((mState == State.AM) && (that.mState == State.PM)) {
                return -1;
            } else if (mState == that.mState) {
                if (mHour > that.mHour) {
                    return 1;
                } else if (mHour < that.mHour) {
                    return -1;
                } else if (mMinute > that.mMinute) {
                    return 1;
                } else if (mMinute < that.mMinute) {
                    return -1;
                } else {
                    return 0;
                }
            } else {
                throw new IllegalStateException("Expected states to be AM or PM");
            }
        }

        @Override
        public String toString() {
            return time();
        }
    }
}
