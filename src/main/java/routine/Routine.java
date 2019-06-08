package routine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Routine {
    Time start();

    interface Time {
        String time();

        Time plusMinutes(int minutes);

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
                final State state;
                if (stateString == null) {
                    state = State.AM;
                } else if (stateString.equals(State.AM.toString())) {
                    state = State.AM;
                } else if (stateString.equals(State.PM.toString())) {
                    state = State.PM;
                } else {
                    throw new IllegalArgumentException("Could not match " + stateString + " to AM or PM");
                }
                return new Time.Standard(hour, minute, state);
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
                if ((newHour + hourDifference) > 12) {
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
        }
    }
}
