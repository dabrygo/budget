package routine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import routine.Routine.Time;

public class TestTime {

    @Nested
    final class TestDefault {
        @Test
        public void from() {
            final Time time = Time.Default.from("8:27");
            assertEquals(new Time.Default(8, 27), time);
        }

        @Test
        public void fromNotFound() {
            try {
                Time.Default.from("1008.87354");
                fail();
            } catch (final IllegalArgumentException e) {

            }
        }

        @Test
        public void time() {
            final Time time = Time.Default.from("8:27");
            assertEquals("08:27", time.time());
        }
    }
}
