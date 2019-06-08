package routine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import routine.Routine.Time;
import routine.Routine.Time.Standard.State;

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
    }
}
