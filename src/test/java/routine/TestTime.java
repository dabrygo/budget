package routine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import routine.Time.Standard.State;

public class TestTime {

    @Nested
    final class TestDefault {
        @Test
        public void from() {
            final Time time = Time.Standard.from("8:27 AM");
            assertEquals(new Time.Standard(8, 27, State.AM), time);
        }

        @Test
        public void fromNotFound() {
            try {
                Time.Standard.from("1008.87354");
                fail();
            } catch (final IllegalArgumentException e) {

            }
        }

        @Test
        public void time() {
            final Time time = Time.Standard.from("8:27 AM");
            assertEquals("08:27 AM", time.time());
        }

        @Test
        public void plusMinutes() {
            final Time time = Time.Standard.from("8:27 AM");
            final Time newTime = time.plusMinutes(3);
            assertEquals(new Time.Standard(8, 30, State.AM), newTime);
        }

        @Test
        public void plusMinutesCrossHour() {
            final Time time = Time.Standard.from("8:59 AM");
            final Time newTime = time.plusMinutes(2);
            assertEquals(new Time.Standard(9, 1, State.AM), newTime);
        }

        @Test
        public void plusMinutesCrossDay() {
            try {
                final Time time = Time.Standard.from("11:59 PM");
                time.plusMinutes(2);
                fail();
            } catch (final IllegalStateException e) {
            }
        }

        @Test
        public void plusMinutesCrossState() {
            final Time time = Time.Standard.from("11:59 AM");
            final Time newTime = time.plusMinutes(2);
            assertEquals(new Time.Standard(12, 1, State.PM), newTime);
        }

        @Test
        public void compareToEqual() {
            final Time a = Time.Standard.from("7:56 AM");
            final Time b = Time.Standard.from("7:56 AM");
            assertEquals(0, a.compareTo(b));
        }

        @Test
        public void compareToBothAm() {
            final Time a = Time.Standard.from("7:56 AM");
            final Time b = Time.Standard.from("9:01 AM");
            assertEquals(1, b.compareTo(a));
            assertEquals(-1, a.compareTo(b));
            // final Time d = Time.Standard.from("2:09 PM");
            // final Time e = Time.Standard.from("8:53 AM");
            //
            // assertEquals(1, )
        }

        @Test
        public void compareToOneAmOnePm() {
            final Time a = Time.Standard.from("7:56 PM");
            final Time b = Time.Standard.from("9:01 AM");
            assertEquals(-1, b.compareTo(a));
            assertEquals(1, a.compareTo(b));
        }

        @Test
        public void compareToBothPm() {
            final Time a = Time.Standard.from("7:56 PM");
            final Time b = Time.Standard.from("7:01 PM");
            assertEquals(-1, b.compareTo(a));
            assertEquals(1, a.compareTo(b));
        }
    }
}
