package routine;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    }
}
