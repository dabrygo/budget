package routine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TestRoutine {
    @Nested
    final class TestDefault {
        @Test
        void indexEmpty() {
            final Routine.Default routine = new Routine.Default();
            final Task task = Task.Fake.duration(1, 1, "A");
            assertEquals(0, routine.index(task));
        }

        @Test
        void indexOfAfter() {
            final Task a = Task.Fake.duration(1, 1, "A");
            final Routine.Default routine = new Routine.Default(Arrays.asList(a));
            final Task b = Task.Fake.duration(2, 1, "B");
            assertEquals(1, routine.index(b));
        }

        @Test
        void indexOfBefore() {
            final Task a = Task.Fake.duration(2, 1, "A");
            final Routine.Default routine = new Routine.Default(Arrays.asList(a));
            final Task b = Task.Fake.duration(1, 1, "B");
            assertEquals(0, routine.index(b));
        }

        @Test
        void indexDuring() {
            final Task a = Task.Fake.duration(1, 1, "A");
            final Routine.Default routine = new Routine.Default(Arrays.asList(a));
            try {
                final Task b = Task.Fake.duration(1, 1, "B");
                routine.add(b);
                fail();
            } catch (final IllegalArgumentException e) {
            }
        }
    }
}
