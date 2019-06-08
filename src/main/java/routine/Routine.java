package routine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Routine {
    Time start();

    interface Time {
        String time();

        final class Default implements Time {
            private final int mHour;
            private final int mMinute;

            public Default(final int hour, final int minute) {
                mHour = hour;
                mMinute = minute;
            }

            public static Time from(final String string) {
                final Pattern pattern = Pattern.compile("([01]?\\d):([0-5]\\d)");
                final Matcher matcher = pattern.matcher(string);
                final boolean found = matcher.find();
                if (!found) {
                    throw new IllegalArgumentException("Could not parse time '" + string);
                }
                final String hourString = matcher.group(1);
                final Integer hour = Integer.parseInt(hourString);
                final String minuteString = matcher.group(2);
                final Integer minute = Integer.parseInt(minuteString);
                return new Time.Default(hour, minute);
            }

            @Override
            public String time() {
                return String.format("%02d:%02d", mHour, mMinute);
            }

            @Override
            public boolean equals(final Object obj) {
                if (!(obj instanceof Default)) {
                    return false;
                }
                final Default that = (Default) obj;
                return (mHour == that.mHour) && (mMinute == that.mMinute);
            }
        }
    }
}
